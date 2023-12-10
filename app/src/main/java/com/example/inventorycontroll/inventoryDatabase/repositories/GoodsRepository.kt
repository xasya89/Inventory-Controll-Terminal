package com.example.inventorycontroll.inventoryDatabase.repositories

import com.example.inventorycontroll.inventoryDatabase.dao.GoodDao
import com.example.inventorycontroll.inventoryDatabase.entities.Barcode
import com.example.inventorycontroll.inventoryDatabase.entities.Good
import javax.inject.Inject

class GoodsRepository @Inject constructor(
    private val goodsDao: GoodDao
) {
    suspend fun addGood(good:Good){
        goodsDao.insertGood(good)
    }
    suspend fun addBarcode(barcode: Barcode){
        //goodsDao.in(barcode)
    }
}