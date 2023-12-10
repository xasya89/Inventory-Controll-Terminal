package com.example.inventorycontroll.inventoryDatabase.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "barcodes")
data class Barcode(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val goodId: Int,
    val code: String
)
