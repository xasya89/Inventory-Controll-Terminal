package com.example.inventorycontroll.communication

import com.example.inventorycontroll.communication.model.BalanceModelApi
import retrofit2.http.GET
import retrofit2.http.Query

interface BalanceApiService {
    @GET("balance")
    suspend fun getBalance(@Query("skip") skip: Int, @Query("take") take: Int):BalanceModelApi
}