package com.example.inventorycontroll.inventoryDatabase.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "barcodes")
data class Barcode(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val goodId: Long,
    val code: String
)
