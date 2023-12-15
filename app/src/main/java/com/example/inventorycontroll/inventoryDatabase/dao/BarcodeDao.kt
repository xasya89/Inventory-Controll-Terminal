package com.example.inventorycontroll.inventoryDatabase.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.inventorycontroll.inventoryDatabase.entities.Good

@Dao
interface BarcodeDao {
    @Query("SELECT g.* FROM barcodes b INNER JOIN goods g ON b.goodId=g.id WHERE g.shopDbName=:shopDbName AND b.code=:barcode")
    fun getGoodByBarcode(shopDbName: String, barcode: String): List<Good>
}