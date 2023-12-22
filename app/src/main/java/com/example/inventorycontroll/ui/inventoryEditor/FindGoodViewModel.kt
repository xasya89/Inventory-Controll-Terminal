package com.example.inventorycontroll.ui.inventoryEditor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorycontroll.common.shopService.ShopService
import com.example.inventorycontroll.inventoryDatabase.dao.GoodDao
import com.example.inventorycontroll.ui.inventoryEditor.models.FindGoodModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FindGoodViewModel @Inject constructor(
    private val shopService: ShopService,
    private val goodDao: GoodDao
): ViewModel(){
    var goods = MutableLiveData<List<FindGoodModel>>(listOf())

    fun find(searchText: String){
        if(searchText=="") return
        viewModelScope.launch (Dispatchers.IO){
            val result = goodDao.findGoods(shopService.getSelectShop().dbName, "%"+searchText+"%")
            goods.postValue(result.map { FindGoodModel(it.id, it.name, it.price) })
        }
    }
}