package com.example.inventorycontroll.inventoryDatabase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.inventorycontroll.inventoryDatabase.entities.Barcode
import com.example.inventorycontroll.inventoryDatabase.entities.Good
import com.example.inventorycontroll.inventoryDatabase.entities.GoodWithBarcodes
import org.jetbrains.annotations.NotNull

@Dao
interface GoodDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertGood(good:Good)

    @Query("SELECT * FROM goods")
    fun getGoods(): List<Good>

}