package com.example.inventorycontroll.inventoryDatabase.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.inventorycontroll.inventoryDatabase.entities.Barcode
import com.example.inventorycontroll.inventoryDatabase.entities.Good
import com.example.inventorycontroll.inventoryDatabase.entities.GoodWithBarcodes
import org.jetbrains.annotations.NotNull
import java.math.BigDecimal

@Dao
interface GoodDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertGood(good:Good): Long
    @Query("UPDATE goods SET price=:price WHERE id=:id")
    fun updateGood(id: Long, price: BigDecimal)

    @Delete
    fun daleteGood(good: Good)

    @Query("DELETE FROM goods")
    fun deleteAllGoods()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertBarcode(barcode: Barcode): Long

    @Query("DELETE FROM barcodes")
    fun deleteAllBarcodes()
    @Delete
    fun deleteBarcode(barcode: Barcode)
    @Delete
    fun deleteBarcodes(barcodes: List<Barcode>)

    @Query("SELECT * FROM goods")
    fun getGoods(): List<Good>

    @Query("SELECT * FROM goods")
    fun getGoodsWithBarcodes(): List<GoodWithBarcodes>
}