package com.example.inventorycontroll.communication.model

import com.example.inventorycontroll.inventoryDatabase.entities.SpecilType
import java.math.BigDecimal

data class GoodResponseModelApi(
    val count: Int,
    val goods: List<GoodModelApi>
)
data class GoodModelApi(
    val uuid: String,
    val name: String,
    val specialType: Int,
    val unit: Int,
    val price: Double,
    val isDeleted: Boolean,
    val barcodes: List<BarcodeModelApi>
)

data class BarcodeModelApi(
    val code: String
)