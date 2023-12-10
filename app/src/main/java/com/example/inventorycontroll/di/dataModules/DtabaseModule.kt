package com.example.inventorycontroll.di.dataModules

import android.content.Context
import androidx.room.Room
import com.example.inventorycontroll.inventoryDatabase.InventoryDatabase
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
    ).build()

    @Singleton
    @Provides
    fun  providerGoodsDao(db: InventoryDatabase) = db.goodDao()
}