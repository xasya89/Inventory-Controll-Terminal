package com.example.inventorycontroll.di.dataModules

import com.example.inventorycontroll.common.shopService.ShopService
import com.example.inventorycontroll.communication.ShopApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServicesModule {

    @Singleton
    @Provides
    fun providerShopServer() = ShopService()
}