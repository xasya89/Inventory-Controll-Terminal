package com.example.inventorycontroll.ui.inventoryEditor

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorycontroll.common.shopService.ShopService
import com.example.inventorycontroll.common.viewModels.ShopViewModel
import com.example.inventorycontroll.inventoryDatabase.dao.GoodDao
import com.example.inventorycontroll.ui.inventoryEditor.models.FindGoodModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
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
        val dbName = shopService.selectShop!!.dbName
        if(searchText=="") return
        viewModelScope.launch (getCoroutineExceptionHandler() + Dispatchers.IO){
            val goosSize = goodDao.getGoods(dbName).size
            val result = goodDao.findGoods(dbName, "%"+searchText+"%").filter { it.isDeleted==false}
            goods.postValue(result.map { FindGoodModel(it.id, it.name, it.price) })
        }
    }

    private fun getCoroutineExceptionHandler(): CoroutineExceptionHandler {
        return CoroutineExceptionHandler { context, throwable ->
            Log.e("FindGoodViewModel",throwable.toString())
        }
    }
}