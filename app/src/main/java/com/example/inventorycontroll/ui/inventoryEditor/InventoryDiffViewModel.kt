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
import com.example.inventorycontroll.inventoryDatabase.dao.GoodGroupDao
import com.example.inventorycontroll.inventoryDatabase.dao.InventoryDao
import com.example.inventorycontroll.inventoryDatabase.dao.InventoryGroupingDao
import com.example.inventorycontroll.inventoryDatabase.entities.GoodGroup
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
    private val invnetoryGroupingDao: InventoryGroupingDao,
    private val goodGroupsDao: GoodGroupDao
): ViewModel() {
    val inventoryId = MutableLiveData<Long>(0)
    val balance = MutableLiveData<List<BalanceDiffItemModel>>(listOf())
    val isLoadingState = MutableLiveData<Boolean>(false)
    val groups = MutableLiveData<List<GoodGroup>>(listOf())
    val selectGoodGroup = MutableLiveData<GoodGroup?>(null)

    init {
        viewModelScope.launch(Dispatchers.IO + getCoroutineExceptionHandler()) {
            val dbName = shopService.selectShop!!.dbName
            val _groups = goodGroupsDao.get(dbName)
            groups.postValue(_groups)
        }
    }

    fun getDiff(){
        val dbName = shopService.selectShop!!.dbName
        isLoadingState.value = false
        viewModelScope.launch (getCoroutineExceptionHandler() + Dispatchers.IO){
            val items = invnetoryGroupingDao.getCountBalanceAndInventory(inventoryId.value!!, dbName)
                .filter { selectGoodGroup.value==null || it.groupId==selectGoodGroup.value?.id }
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