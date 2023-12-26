package com.example.inventorycontroll.di.dataModules

import com.example.inventorycontroll.communication.GoodsApiService
import com.example.inventorycontroll.communication.ShopApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    private const val BASE_URL = "https://notebook.exp-tech.com/api-inventory/"//"http://192.168.1.200:5156/api/"

    @Singleton
    @Provides
    fun providesOkHttpClient(): OkHttpClient =
        OkHttpClient
            .Builder()
            .build()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    fun providerGoodsApiService(retrofit: Retrofit): GoodsApiService = retrofit.create(GoodsApiService::class.java)

    @Singleton
    @Provides
    fun providerShopApiService(retrofit: Retrofit): ShopApiService = retrofit.create(ShopApiService::class.java)
}