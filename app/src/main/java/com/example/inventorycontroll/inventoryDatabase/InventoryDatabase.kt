package com.example.inventorycontroll.inventoryDatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.inventorycontroll.inventoryDatabase.dao.BarcodeDao
import com.example.inventorycontroll.inventoryDatabase.dao.GoodDao
import com.example.inventorycontroll.inventoryDatabase.entities.Barcode
import com.example.inventorycontroll.inventoryDatabase.entities.Good
import com.example.inventorycontroll.inventoryDatabase.entities.Inventory
import com.example.inventorycontroll.inventoryDatabase.entities.InventoryGood
import com.example.inventorycontroll.inventoryDatabase.entities.InventoryGroup

@Database(entities = [Good::class, Barcode::class, Inventory::class, InventoryGroup::class, InventoryGood::class], version = 1)
@TypeConverters(Converters::class)
abstract class InventoryDatabase: RoomDatabase() {

    abstract fun goodDao(): GoodDao
    abstract fun barcodeDao(): BarcodeDao
}