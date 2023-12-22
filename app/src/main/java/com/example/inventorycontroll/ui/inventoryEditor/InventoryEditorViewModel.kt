package com.example.inventorycontroll.ui.inventoryEditor

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorycontroll.common.shopService.ShopService
import com.example.inventorycontroll.inventoryDatabase.dao.BarcodeDao
import com.example.inventorycontroll.inventoryDatabase.dao.GoodDao
import com.example.inventorycontroll.inventoryDatabase.dao.InventoryDao
import com.example.inventorycontroll.inventoryDatabase.entities.Good
import com.example.inventorycontroll.inventoryDatabase.entities.Inventory
import com.example.inventorycontroll.inventoryDatabase.entities.InventoryGood
import com.example.inventorycontroll.inventoryDatabase.entities.InventoryGroup
import com.example.inventorycontroll.ui.inventoryEditor.models.FindGoodModel
import com.example.inventorycontroll.ui.inventoryEditor.models.InventoryPositionModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class InventoryEditorViewModel @Inject constructor(
    private val dao: InventoryDao,
    private val goodsDao: GoodDao,
    private val barcodeDao: BarcodeDao,
    private val shopService: ShopService
): ViewModel() {
    private val inventory = MutableLiveData<Inventory?>(null)
    private val groups = MutableLiveData<List<InventoryGroup>>(listOf())
    private val selectGroup = MutableLiveData<InventoryGroup?>(null)
    val positions = MutableLiveData<List<InventoryPositionModel>>(listOf())

    suspend fun getExistInventory(): Inventory?{
        val result = dao.getExistInventory(shopService.getSelectShop().dbName).firstOrNull()
        inventory.postValue(result)
        if(result!=null){
            var _groups = dao.getGroups(result.id)
            groups.postValue(_groups)
        }
        return result
    }

    fun getGroups(): List<InventoryGroup>{
        return groups.value!!.toList()
    }
    fun addGroup(groupName: String, onComplite: ()->Unit){
        viewModelScope.launch (Dispatchers.IO){
            val newGroup = InventoryGroup(0, inventory.value!!.id, groupName, BigDecimal(0))
            val id = dao.insertGroup(newGroup)
            newGroup.id = id
            val _groups = mutableListOf<InventoryGroup>(newGroup)
            _groups.addAll(groups.value!!)
            groups.postValue(_groups)
            viewModelScope.launch (Dispatchers.Main){
                onComplite.invoke()
            }
        }
    }

    suspend fun createInventory(startCashMoney: BigDecimal): Inventory{
        val newInventory = Inventory(0, Date(), shopService.getSelectShop().dbName, startCashMoney, BigDecimal(0), false, false)
        val id = dao.insertInventory(newInventory)
        newInventory.id = id
        inventory.postValue(newInventory)
        return newInventory
    }

    fun getGood(barcode: String, onFind: (good: Good)->Unit){
        viewModelScope.launch (Dispatchers.IO) {
            val good = barcodeDao.getGoodByBarcode(shopService.getSelectShop().dbName, barcode).firstOrNull()
            if(good==null) return@launch
            viewModelScope.launch(Dispatchers.Main) {
                onFind.invoke(good)
            }
        }
    }

    fun addPosition(good: Good, count: BigDecimal) {
        val find = positions.value?.any { it.goodId == good.id } ?: false
        if (!find)
            positions.postValue(
                positions.value?.plus(
                    InventoryPositionModel(0, 0, good.id, good.name, good.price, count)
                )
            )
        else
            positions.postValue(positions.value!!.map {
                if (it.goodId == good.id)
                    return@map InventoryPositionModel(it.id, it.groupId, it.goodId, it.goodName, good.price, count)
                return@map it
            })
    }

    fun addPositions(goods: List<FindGoodModel>){
        val list = mutableListOf<InventoryPositionModel>()
        goods.forEach { good->
            val position = positions.value?.find { it.goodId==good.id }
            if(position==null) list.add(InventoryPositionModel(0, 0, good.id, good.name, good.price, BigDecimal(0) ))
        }
        list.addAll(positions.value!!)
        positions.value = list
    }

}