package com.example.inventorycontroll.inventoryDatabase.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.inventorycontroll.inventoryDatabase.Converters
import java.math.BigDecimal
import java.util.Date

@Entity(tableName = "inventories", indices = [Index(value = ["shopDbName"])])
@TypeConverters(Converters::class)
data class Inventory(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    val dateCreate: Date,
    val shopDbName: String,
    val startCashMoney: BigDecimal,
    var goodsSum: BigDecimal = BigDecimal(0),
    var isStop: Boolean = false,
    var isSendToServer: Boolean = false,
    var isCancel: Boolean = false
)
