package com.example.inventorycontroll.inventoryDatabase.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(tableName = "balance")
data class Balance(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val goodId:Long,
    val shopDbName: String,
    val balanceCount: BigDecimal
)
