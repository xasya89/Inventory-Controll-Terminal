package com.example.inventorycontroll.inventoryDatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.inventorycontroll.inventoryDatabase.dao.GoodDao
import com.example.inventorycontroll.inventoryDatabase.entities.Good

@Database(entities = [Good::class], version = 1)
@TypeConverters(Converters::class)
abstract class InventoryDatabase: RoomDatabase() {

    abstract fun goodDao(): GoodDao
}