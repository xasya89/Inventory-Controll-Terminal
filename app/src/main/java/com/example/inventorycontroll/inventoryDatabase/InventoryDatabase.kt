package com.example.inventorycontroll.inventoryDatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.inventorycontroll.inventoryDatabase.dao.BalanceDao
import com.example.inventorycontroll.inventoryDatabase.dao.BarcodeDao
import com.example.inventorycontroll.inventoryDatabase.dao.GoodDao
import com.example.inventorycontroll.inventoryDatabase.dao.GoodGroupDao
import com.example.inventorycontroll.inventoryDatabase.dao.InventoryDao
import com.example.inventorycontroll.inventoryDatabase.dao.InventoryGroupingDao
import com.example.inventorycontroll.inventoryDatabase.entities.Balance
import com.example.inventorycontroll.inventoryDatabase.entities.Barcode
import com.example.inventorycontroll.inventoryDatabase.entities.Good
import com.example.inventorycontroll.inventoryDatabase.entities.GoodGroup
import com.example.inventorycontroll.inventoryDatabase.entities.Inventory
import com.example.inventorycontroll.inventoryDatabase.entities.InventoryGood
import com.example.inventorycontroll.inventoryDatabase.entities.InventoryGroup

@Database(entities = [GoodGroup::class, Good::class, Barcode::class, Inventory::class, InventoryGroup::class, InventoryGood::class, Balance::class], version = 1)
@TypeConverters(Converters::class)
abstract class InventoryDatabase: RoomDatabase() {

    abstract fun goodGroupDao(): GoodGroupDao
    abstract fun goodDao(): GoodDao
    abstract fun barcodeDao(): BarcodeDao
    abstract fun inventoryDao(): InventoryDao
    abstract fun balanceDao(): BalanceDao
    abstract fun inventoryGroupingDao(): InventoryGroupingDao
}

val MIGRATION_1_2 = object : Migration(1, 2) {

    override fun migrate(database: SupportSQLiteDatabase) {
        TODO("Not yet implemented")
    }
}