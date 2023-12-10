package com.example.inventorycontroll.inventoryDatabase.entities

import androidx.room.Embedded
import androidx.room.Relation

data class GoodWithBarcodes (
    @Embedded val good: Good,
    @Relation(
        parentColumn = "id",
        entityColumn = "goodId"
    )
    val barcodes:List<Barcode>
)