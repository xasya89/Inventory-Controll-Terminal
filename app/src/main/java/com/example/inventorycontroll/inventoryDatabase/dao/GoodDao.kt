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
    @Query("UPDATE goods SET groupId=:groupId, price=:price, isDeleted=:isDeleted WHERE id=:id")
    fun updateGood(id: Long, groupId: Long, price: BigDecimal, isDeleted: Boolean)

    @Delete
    fun daleteGood(good: Good)

    @Query("DELETE FROM goods")
    fun deleteAllGoods()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertBarcode(barcode: Barcode): Long

    @Query("DELETE FROM barcodes WHERE goodId IN (SELECT id FROM goods WHERE shopDbName=:shopDbName)")
    fun deleteAllBarcodes(shopDbName: String)
    @Delete
    fun deleteBarcode(barcode: Barcode)
    @Delete
    fun deleteBarcodes(barcodes: List<Barcode>)

    @Query("SELECT * FROM goods WHERE shopDbName=:shopDbName")
    fun getGoods(shopDbName: String): List<Good>

    @Query("SELECT * FROM goods WHERE name LIKE :findText AND shopDbName=:shopDbName")
    fun findGoods(shopDbName: String, findText: String): List<Good>

    @Query("SELECT * FROM goods WHERE shopDbName=:shopDbName")
    fun getGoodsWithBarcodes(shopDbName: String): List<GoodWithBarcodes>
}