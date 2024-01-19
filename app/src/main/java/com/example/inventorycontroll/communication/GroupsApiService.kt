package com.example.inventorycontroll.communication

import com.example.inventorycontroll.communication.model.GoodGroupModelApi
import retrofit2.http.GET
import retrofit2.http.Header

interface GoodGroupsApiService {
    @GET("goodgroups")
    suspend fun getGroups(@Header("shop-name") shopDbName: String):List<GoodGroupModelApi>
}