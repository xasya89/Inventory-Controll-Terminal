package com.example.inventorycontroll.ui.synchronization

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorycontroll.common.shopService.ShopService
import com.example.inventorycontroll.communication.GoodsApiService
import com.example.inventorycontroll.communication.model.BarcodeModelApi
import com.example.inventorycontroll.inventoryDatabase.dao.GoodDao
import com.example.inventorycontroll.inventoryDatabase.entities.Barcode
import com.example.inventorycontroll.inventoryDatabase.entities.Good
import com.example.inventorycontroll.inventoryDatabase.entities.GoodUnit
import com.example.inventorycontroll.inventoryDatabase.entities.SpecilType
import com.example.inventorycontroll.inventoryDatabase.repositories.GoodsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SynchronizationViewModel @Inject constructor(
    private val dao: GoodDao,
    private val api: GoodsApiService,
    private val shopService: ShopService
): ViewModel() {

    val compliteSynchronization = MutableLiveData<Boolean>(false)

    fun synchronization(){
        viewModelScope.launch (Dispatchers.IO) {
            val shopDbName = shopService.getSelectShop().dbName
            var goodsDb = dao.getGoodsWithBarcodes()

            var _skip = 0
            val countGoods = api.getGoods( shopDbName,0, 1).count
            do {
                var goods = api.getGoods(shopDbName, _skip, 200).goods
                goods.forEach { g->
                    val goodDb= goodsDb.firstOrNull { it.good.uuid == g.uuid }
                    if(goodDb==null) {
                        val id = dao.insertGood(
                            Good(
                                id = 0,
                                shopDbName,
                                uuid = g.uuid,
                                name = g.name,
                                unit = GoodUnit.PCE,
                                specialType = SpecilType.None,
                                price = g.price.toBigDecimal()
                            )
                        )

                        insertBarcodes(id, g.barcodes)
                    }
                    if(goodDb!=null){
                        dao.updateGood(goodDb.good.id, g.price.toBigDecimal())
                        dao.deleteBarcodes(goodDb.barcodes)
                        insertBarcodes(goodDb.good.id, g.barcodes)
                    }
                }

                _skip += 200
            } while (countGoods > _skip - 200)

            compliteSynchronization.postValue(true)
        }
    }

    private suspend fun insertBarcodes(goodId: Long, barcodes: List<BarcodeModelApi>){
        barcodes.forEach { b ->
            try {
                dao.insertBarcode(Barcode(0, goodId, b.code ?: ""))
            } catch (e: Exception) {
                Log.d("barcode added exception", b.code)
            }
        }
    }
}