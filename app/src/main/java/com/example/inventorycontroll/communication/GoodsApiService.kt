package com.example.inventorycontroll.communication

import com.example.inventorycontroll.communication.model.GoodModelApi
import com.example.inventorycontroll.communication.model.GoodResponseModelApi
import retrofit2.http.GET
import retrofit2.http.Query

interface GoodsApiService {
    @GET("goods")
    suspend fun getGoods(@Query("skip") skip: Int?, @Query("take") take: Int?): GoodResponseModelApi
}