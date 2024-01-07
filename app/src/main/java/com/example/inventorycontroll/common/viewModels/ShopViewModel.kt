package com.example.inventorycontroll.common.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorycontroll.common.shopService.ShopModel
import com.example.inventorycontroll.common.shopService.ShopService
import com.example.inventorycontroll.communication.ShopApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShopViewModel @Inject constructor(
    private val shopService: ShopService,
    private val shopApi: ShopApiService
): ViewModel() {
    val shops = MutableLiveData<List<ShopModel>>(listOf())
    val selectShop = MutableLiveData<ShopModel>(null)

    fun getShops(){
        viewModelScope.launch (getCoroutineExceptionHandler() + Dispatchers.IO){
            val list = shopApi.getShops()
            shops.postValue(list)
            shopService.selectShop = list.firstOrNull()
        }
    }

    fun setSelectShop(shopModel: ShopModel){
        selectShop.value = shopModel
        shopService.selectShop = shopModel
    }
    private fun getCoroutineExceptionHandler(): CoroutineExceptionHandler {
        return CoroutineExceptionHandler { context, throwable ->
            Log.e("Get shop list",throwable.toString())
        }
    }
}