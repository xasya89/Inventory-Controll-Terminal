package com.example.inventorycontroll.communication

import com.example.inventorycontroll.common.shopService.ShopModel
import retrofit2.http.GET

interface ShopApiService {
    @GET("shops")
    suspend fun getShops(): List<ShopModel>
}