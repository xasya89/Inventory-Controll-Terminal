package com.example.inventorycontroll.ui.inventoryEditor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.inventorycontroll.communication.BalanceApiService
import com.example.inventorycontroll.inventoryDatabase.dao.BalanceDao
import com.example.inventorycontroll.inventoryDatabase.dao.GoodDao
import com.example.inventorycontroll.inventoryDatabase.dao.InventoryDao
import com.example.inventorycontroll.inventoryDatabase.dao.InventoryGroupingDao
import com.example.inventorycontroll.ui.inventoryEditor.bizLogic.SynchBalance
import com.example.inventorycontroll.ui.inventoryEditor.models.BalanceDiffItemModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class InventoryDiffViewModel @Inject constructor(
    private val goodDao: GoodDao,
    private val inventoryDao: InventoryDao,
    private val invnetoryGroupingDao: InventoryGroupingDao,
    private val balanceDao: BalanceDao,
    private val balanceApiService: BalanceApiService
): ViewModel() {
    val balance = MutableLiveData<List<BalanceDiffItemModel>>(listOf())

    fun recalcBalance(){
        viewModelScope.launch (Dispatchers.IO) {
            SynchBalance("Shop3",balanceApiService, balanceDao, goodDao).synch()
        }
    }
    fun getDiff(inventoryId: Long){
        viewModelScope.launch (Dispatchers.IO){
            val inventory = inventoryDao.getInventoryById(inventoryId)!!
            val _balance = balanceDao.getBalance(inventory.shopDbName)
            val goods = goodDao.getGoods(inventory.shopDbName)
            val inventoryGoods = invnetoryGroupingDao.getCountGroupingGood(inventoryId)

            val result = inventoryGoods.map { BalanceDiffItemModel(
                it.goodId,
                goods.find { g-> it.goodId==g.id }!!.name,
                it.countFact,
                _balance.firstOrNull{b->it.goodId==b.goodId}?.Balance ?: BigDecimal(0)
            ) }
            if(_balance.count()>inventoryGoods.count())
                result.plus(_balance
                    .filter { !result.any{r->r.goodId==it.goodId} }
                    .map { BalanceDiffItemModel(
                        it.goodId,
                        goods.find { g-> it.goodId==g.id }!!.name,
                        BigDecimal(0),
                        it.Balance
                    ) }
                )

            balance.postValue(result.filter { it.countFact!=it.countFromServer })
        }
    }
}