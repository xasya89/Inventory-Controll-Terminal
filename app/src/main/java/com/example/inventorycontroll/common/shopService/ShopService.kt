package com.example.inventorycontroll.common.shopService

import com.example.inventorycontroll.communication.ShopApiService

class ShopService(private val api: ShopApiService) {

    private var selectShop: ShopModel? = null
    val shops: MutableList<ShopModel> = mutableListOf()
    suspend fun getShops(){
        val _shops = api.getShops()
        shops.addAll(_shops)

    }

    fun selectShop(name: String){
        selectShop = shops.firstOrNull { it.name==name }
    }

    fun selectShop(pos: Int): ShopModel{
        selectShop = shops.get(pos)
        return shops.get(pos)
    }

    fun getSelectShop():ShopModel = selectShop!!
}