package com.example.inventorycontroll.di.dataModules

import android.content.Context
import androidx.room.Room
import com.example.inventorycontroll.inventoryDatabase.InventoryDatabase
import com.example.inventorycontroll.inventoryDatabase.MIGRATION_1_2
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DtabaseModule {

    @Singleton
    @Provides
    fun providerInventoryDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        InventoryDatabase::class.java,
        "inventory-database"
    )
        .build()

    @Singleton
    @Provides
    fun  providerGoodsDao(db: InventoryDatabase) = db.goodDao()

    @Singleton
    @Provides
    fun providerGoodGroupsDao(db: InventoryDatabase) = db.goodGroupDao()

    @Singleton
    @Provides
    fun providerBarcodeDao(db: InventoryDatabase) = db.barcodeDao()

    @Singleton
    @Provides
    fun providerInventoryDao(db: InventoryDatabase) = db.inventoryDao()

    @Singleton
    @Provides
    fun providerBalance(db: InventoryDatabase) = db.balanceDao()

    @Singleton
    @Provides
    fun providerInventoryGroupingDao(db: InventoryDatabase) = db.inventoryGroupingDao()
}