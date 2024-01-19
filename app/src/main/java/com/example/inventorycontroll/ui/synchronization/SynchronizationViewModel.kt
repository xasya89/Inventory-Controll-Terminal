package com.example.inventorycontroll.ui.synchronization

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorycontroll.common.shopService.ShopService
import com.example.inventorycontroll.common.viewModels.ShopViewModel
import com.example.inventorycontroll.communication.GoodGroupsApiService
import com.example.inventorycontroll.communication.GoodsApiService
import com.example.inventorycontroll.communication.model.BarcodeModelApi
import com.example.inventorycontroll.inventoryDatabase.dao.GoodDao
import com.example.inventorycontroll.inventoryDatabase.dao.GoodGroupDao
import com.example.inventorycontroll.inventoryDatabase.entities.Barcode
import com.example.inventorycontroll.inventoryDatabase.entities.Good
import com.example.inventorycontroll.inventoryDatabase.entities.GoodGroup
import com.example.inventorycontroll.inventoryDatabase.entities.GoodUnit
import com.example.inventorycontroll.inventoryDatabase.entities.SpecilType
import com.example.inventorycontroll.inventoryDatabase.repositories.GoodsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.wait
import javax.inject.Inject

@HiltViewModel
class SynchronizationViewModel @Inject constructor(
    private val dao: GoodDao,
    private val groupsDao: GoodGroupDao,
    private val api: GoodsApiService,
    private val groupsApi: GoodGroupsApiService,
    val shopService: ShopService
): ViewModel() {
    val compliteSynchronization = MutableLiveData<Boolean>(true)
    val error = MutableLiveData<String?>(null)

    fun synchronization(){
        compliteSynchronization.value = false
        val selectDbName = shopService.selectShop!!.dbName
        viewModelScope.launch (Dispatchers.IO + getCoroutineExceptionHandler()) {
            val groupsServer = groupsApi.getGroups(selectDbName)
            groupsServer.forEach {
                val groupdId = it.id.toLong()
                val group = groupsDao.get(groupdId, selectDbName)
                if (group == null)
                    groupsDao.insert(GoodGroup(0, groupdId, selectDbName, it.name))
                if (group != null && group?.name != it.name)
                    groupsDao.update(it.name, selectDbName, groupdId)
            }

            val groupsDb = groupsDao.get(selectDbName)
            var goodsDb = dao.getGoodsWithBarcodes(selectDbName)

            var _skip = 0
            val countGoods = api.getGoods(selectDbName, 0, 1).count
            do {
                var goods = api.getGoods(selectDbName, _skip, 200).goods
                goods.forEach { g ->
                    val goodDb = goodsDb.firstOrNull { it.good.uuid == g.uuid }
                    val group = groupsDb.first { it.idServer == g.goodGroupId }
                    if (goodDb == null) {
                        val id = dao.insertGood(
                            Good(
                                id = 0,
                                selectDbName,
                                group.id,
                                uuid = g.uuid,
                                name = g.name,
                                unit = GoodUnit.PCE,
                                specialType = SpecilType.None,
                                price = g.price.toBigDecimal(),
                                isDeleted = g.isDeleted
                            )
                        )

                        insertBarcodes(id, g.barcodes)
                    }
                    if (goodDb != null) {
                        dao.updateGood(goodDb.good.id, group.id, g.price.toBigDecimal(), g.isDeleted)
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

    private fun getCoroutineExceptionHandler(): CoroutineExceptionHandler {
        return CoroutineExceptionHandler { context, throwable ->
            Log.e(this.toString(), throwable.message.toString())
            error.postValue("Ошибка синхронизации")
        }
    }

    private suspend fun synchGoods(shopDbName: String) = withContext(Dispatchers.IO) {


    }

    private suspend fun synchGroups(shopDbName: String) = withContext(Dispatchers.IO) {

    }
}