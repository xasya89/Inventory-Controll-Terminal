package com.example.inventorycontroll.ui.inventoryEditor

import android.provider.ContactsContract.Groups
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.inventorycontroll.common.shopService.ShopService
import com.example.inventorycontroll.common.viewModels.ShopViewModel
import com.example.inventorycontroll.communication.BalanceApiService
import com.example.inventorycontroll.communication.InventoryApiService
import com.example.inventorycontroll.communication.model.InventoryGoodModelApi
import com.example.inventorycontroll.communication.model.InventoryModelApi
import com.example.inventorycontroll.communication.model.InvnetoryGroupModelApi
import com.example.inventorycontroll.inventoryDatabase.dao.BalanceDao
import com.example.inventorycontroll.inventoryDatabase.dao.BarcodeDao
import com.example.inventorycontroll.inventoryDatabase.dao.GoodDao
import com.example.inventorycontroll.inventoryDatabase.dao.InventoryDao
import com.example.inventorycontroll.inventoryDatabase.entities.Good
import com.example.inventorycontroll.inventoryDatabase.entities.Inventory
import com.example.inventorycontroll.inventoryDatabase.entities.InventoryGood
import com.example.inventorycontroll.inventoryDatabase.entities.InventoryGroup
import com.example.inventorycontroll.ui.inventoryEditor.bizLogic.SynchBalance
import com.example.inventorycontroll.ui.inventoryEditor.models.FindGoodModel
import com.example.inventorycontroll.ui.inventoryEditor.models.InventoryPositionModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.util.Date
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class InventoryEditorViewModel @Inject constructor(
    private val dao: InventoryDao,
    private val goodsDao: GoodDao,
    private val barcodeDao: BarcodeDao,
    private val balanceDao: BalanceDao,
    val shopService: ShopService,
    private val inventoryApi: InventoryApiService,
    private val balanceApiService: BalanceApiService
): ViewModel() {
    val inventory = MutableLiveData<Inventory?>(null)
    val groups = MutableLiveData<List<InventoryGroup>>(listOf())
    val selectGroup = MutableLiveData<InventoryGroup?>(null)
    val positions = MutableLiveData<List<InventoryPositionModel>>(listOf())
    val isSaveState = MutableLiveData<Boolean>(false)

    fun getInventory(){
        val selectDbName = shopService.selectShop!!.dbName
        viewModelScope.launch(getCoroutineExceptionHandler() + Dispatchers.IO) {
            val result = dao.getExistInventory(selectDbName).firstOrNull()
            inventory.postValue(result)
            if(result!=null){
                var _groups = dao.getGroups(result.id)
                groups.postValue(_groups)
                val group = _groups.firstOrNull()
                selectGroup.postValue(group)
                val result = dao.getGoods(group!!.id)
                positions.postValue(result)
            }
        }
    }

    fun createIncentory(money: BigDecimal, onSuccess: ()->Unit){
        val selectDbName = shopService.selectShop!!.dbName
        viewModelScope.launch(getCoroutineExceptionHandler() + Dispatchers.IO) {
            if(inventory.value!=null){
                inventory.value?.isCancel=true
            }
            dao.canceledActiveInventory(selectDbName)
            var id = dao.insertInventory(Inventory(0, Date(), selectDbName, money))
            val newInventory = dao.getInventoryById(id)
            inventory.postValue(newInventory)
            var groupId = dao.insertGroup(InventoryGroup(0, id, "Группа 1", BigDecimal(0)))
            var _groups = dao.getGroups(id)
            groups.postValue(_groups)
            selectGroup.postValue(_groups.firstOrNull())
            positions.postValue(listOf())

            SynchBalance(selectDbName, balanceApiService, balanceDao, goodsDao).synch()

            viewModelScope.launch(Dispatchers.Main) { onSuccess.invoke() }
        }
    }

    fun addGroup(groupName: String){
        if(isSaveState.value==true) return
        viewModelScope.launch (getCoroutineExceptionHandler() + Dispatchers.IO){
            val newGroup = InventoryGroup(0, inventory.value!!.id, groupName, BigDecimal(0))
            val id = dao.insertGroup(newGroup)
            newGroup.id = id
            val _groups = mutableListOf<InventoryGroup>(newGroup)
            _groups.addAll(groups.value!!)
            groups.postValue(_groups)
            selectGroup.postValue(newGroup)
            positions.postValue(listOf())
        }
    }
    fun changeSelectGroup(group: InventoryGroup){
        if(selectGroup.value==group) return
        selectGroup.value = group
        viewModelScope.launch (getCoroutineExceptionHandler() + Dispatchers.IO){
            val result = dao.getGoods(group.id)
            positions.postValue(result)
        }
    }


    fun getGood(barcode: String, onFind: (good: Good)->Unit){
        val selectDbName = shopService.selectShop!!.dbName
        viewModelScope.launch (getCoroutineExceptionHandler() + Dispatchers.IO) {
            val good = barcodeDao.getGoodByBarcode(selectDbName, barcode).firstOrNull()
                ?: return@launch
            viewModelScope.launch(Dispatchers.Main) {
                onFind.invoke(good)
            }
        }
    }

    fun addPosition(good: Good, count: BigDecimal) {
        val groupId = selectGroup.value?.id
        if(groupId==null) return
        val position = positions.value?.find { it.goodId == good.id }
        if (position == null)
            positions.postValue(
                positions.value?.plus(
                    InventoryPositionModel(0, groupId, good.id, good.name, good.price, count)
                )
            )
        else{
            positions.postValue(positions.value!!.map {
                if (it.goodId == good.id)
                    return@map InventoryPositionModel(it.id, it.groupId, it.goodId, it.goodName, good.price, count)
                return@map it
            })
        }

        onChangeSum(position?.count, count, good.price)
        isSaveState.postValue(true)
    }

    private fun onChangeSum(oldCount: BigDecimal?, newCount: BigDecimal, price: BigDecimal){
        var sum = inventory.value!!.goodsSum+  (newCount - (oldCount ?: BigDecimal(0))) * price
        inventory.value!!.goodsSum = sum
        inventory.postValue(inventory.value)

        groups.postValue(
        groups.value?.map {
            if(selectGroup.value!=null && it.id == selectGroup.value!!.id)
                it.sum = it.sum +  (newCount - (oldCount ?: BigDecimal(0))) * price
            return@map it
        })
    }

    fun addPositions(goods: List<FindGoodModel>){
        viewModelScope.launch (getCoroutineExceptionHandler() + Dispatchers.Main){
            val list = mutableListOf<InventoryPositionModel>()
            goods.forEach { good->
                val position = positions.value?.find { it.goodId==good.id }
                if(position==null) list.add(InventoryPositionModel(0, selectGroup.value!!.id, good.id, good.name, good.price, BigDecimal(0) ))
            }
            list.addAll(positions.value!!)
            positions.postValue(list)
            isSaveState.postValue(true)
        }
    }

    fun savePositions(){
        viewModelScope.launch (getCoroutineExceptionHandler() + Dispatchers.IO){
            _savePositions()
        }
    }

    suspend fun _savePositions(){
        val list = mutableListOf<InventoryPositionModel>()
        positions.value?.forEach {
            if(it.id==0L){
                val id = dao.insertGood(InventoryGood(0, it.groupId, it.goodId, it.count, it.price))
                it.id=id
            }
            else{
                val result = dao.getGood(it.id)
                result.count = it.count
                result.price = it.price
                dao.updateGood(result)
            }
            list.add(it)
        }
        positions.postValue(list)
        isSaveState.postValue(false)

        dao.updateInventory(inventory.value!!)
        dao.updateGroups(groups.value!!)
    }

    fun changeCountInPosition(goodId: Long, count: BigDecimal){
        positions.value = positions.value?.map {
            if(it.goodId==goodId){
                onChangeSum(it.count, count, it.price)
                it.count = count
            }

            return@map it
        }
        isSaveState.value = true
    }

    fun send(onSuccess: () -> Unit){
        val selectDbName = shopService.selectShop!!.dbName
        viewModelScope.launch (getCoroutineExceptionHandler() + Dispatchers.IO){
            _savePositions()
            val _inventory = inventory.value!!
            val goodsDb = goodsDao.getGoods(selectDbName)
            val groups = dao.getGroupsWithGoods(_inventory.id).map {
                InvnetoryGroupModelApi(
                    it.group.groupName,
                    it.goods.map { pos ->
                        InventoryGoodModelApi(
                            goodsDb.find { it.id==pos.goodId }!!.uuid,
                            pos.price,
                            pos.count
                        )
                    }
                )
            }
            inventoryApi.SendInventory(selectDbName, InventoryModelApi(
                UUID.randomUUID().toString(),
                inventory.value!!.dateCreate,
                inventory.value!!.startCashMoney,
                groups
                ))
            inventory.value?.isSendToServer = true
            inventory.value?.isStop = true
            dao.updateInventory(inventory.value!!)
            viewModelScope.launch(Dispatchers.Main){
                onSuccess.invoke()
            }
        }
    }

    private fun getCoroutineExceptionHandler(): CoroutineExceptionHandler {
        return CoroutineExceptionHandler { context, throwable ->
            Log.e(this.toString(), throwable.message.toString())
        }
    }
}