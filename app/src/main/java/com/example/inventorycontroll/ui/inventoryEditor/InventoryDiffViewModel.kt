package com.example.inventorycontroll.ui.inventoryEditor

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.inventorycontroll.common.shopService.ShopService
import com.example.inventorycontroll.common.viewModels.ShopViewModel
import com.example.inventorycontroll.communication.BalanceApiService
import com.example.inventorycontroll.inventoryDatabase.dao.BalanceDao
import com.example.inventorycontroll.inventoryDatabase.dao.GoodDao
import com.example.inventorycontroll.inventoryDatabase.dao.InventoryDao
import com.example.inventorycontroll.inventoryDatabase.dao.InventoryGroupingDao
import com.example.inventorycontroll.ui.inventoryEditor.bizLogic.SynchBalance
import com.example.inventorycontroll.ui.inventoryEditor.models.BalanceDiffItemModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class InventoryDiffViewModel @Inject constructor(
    private val shopService: ShopService,
    private val invnetoryGroupingDao: InventoryGroupingDao
): ViewModel() {
    val balance = MutableLiveData<List<BalanceDiffItemModel>>(listOf())
    val isLoadingState = MutableLiveData<Boolean>(false)
    fun getDiff(inventoryId: Long){
        val dbName = shopService.selectShop!!.dbName
        isLoadingState.value = false
        viewModelScope.launch (getCoroutineExceptionHandler() + Dispatchers.IO){
            val items = invnetoryGroupingDao.getCountBalanceAndInventory(inventoryId, dbName)
            balance.postValue(items.map { BalanceDiffItemModel(
                it.goodId,
                it.name,
                it.inventoryCount,
                it.balanceCount
            ) })
            isLoadingState.postValue(true)
        }
    }

    private fun getCoroutineExceptionHandler(): CoroutineExceptionHandler {
        return CoroutineExceptionHandler { context, throwable ->
            Log.e(this.toString(),throwable.toString())
        }
    }
}