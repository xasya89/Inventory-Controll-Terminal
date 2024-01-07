package com.example.inventorycontroll.di.dataModules

import com.example.inventorycontroll.communication.BalanceApiService
import com.example.inventorycontroll.communication.GoodsApiService
import com.example.inventorycontroll.communication.InventoryApiService
import com.example.inventorycontroll.communication.ShopApiService
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    private const val BASE_URL = "https://notebook.exp-tech.com/api-inventory/"
    //private const val BASE_URL = "http://192.168.1.200:5156/api-inventory/"

    @Singleton
    @Provides
    fun providesOkHttpClient(): OkHttpClient =
        OkHttpClient
            .Builder()
            .build()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val gsonCOnverter =GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gsonCOnverter))
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun providerGoodsApiService(retrofit: Retrofit): GoodsApiService = retrofit.create(GoodsApiService::class.java)

    @Singleton
    @Provides
    fun providerShopApiService(retrofit: Retrofit): ShopApiService = retrofit.create(ShopApiService::class.java)

    @Singleton
    @Provides
    fun providerInventoryApiService(retrofit: Retrofit): InventoryApiService = retrofit.create(InventoryApiService::class.java)
    @Singleton
    @Provides
    fun providerBalanceApiService(retrofit: Retrofit): BalanceApiService = retrofit.create(BalanceApiService::class.java)
}