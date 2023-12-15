package com.example.inventorycontroll.ui.inventoryEditor

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorycontroll.inventoryDatabase.dao.BarcodeDao
import com.example.inventorycontroll.inventoryDatabase.entities.Good
import com.example.inventorycontroll.inventoryDatabase.entities.Inventory
import com.example.inventorycontroll.inventoryDatabase.entities.InventoryGood
import com.example.inventorycontroll.inventoryDatabase.entities.InventoryGroup
import com.example.inventorycontroll.ui.inventoryEditor.models.InventoryPositionModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class InventoryEditorViewModel @Inject constructor(
): ViewModel() {
    private val groups = MutableLiveData<List<InventoryGroup>>(listOf())
    private val selectGroup = MutableLiveData<InventoryGroup>(null)
    private val positions = MutableLiveData<List<InventoryPositionModel>>(listOf())

    fun addPosition(good: Good, count: BigDecimal) {
        val find = positions.value?.any { it.goodId == good.id } ?: false
        if (!find)
            positions.postValue(
                positions.value!!.plus(
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

}