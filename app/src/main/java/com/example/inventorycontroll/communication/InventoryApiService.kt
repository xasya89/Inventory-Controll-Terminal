package com.example.inventorycontroll.communication

import com.example.inventorycontroll.communication.model.InventoryGoodModelApi
import com.example.inventorycontroll.communication.model.InventoryModelApi
import com.example.inventorycontroll.communication.model.MessageResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface InventoryApiService {

    @POST("inventory")
    suspend fun SendInventory(@Header("shop-name") shopDbName: String, @Body() inventory: InventoryModelApi)
}