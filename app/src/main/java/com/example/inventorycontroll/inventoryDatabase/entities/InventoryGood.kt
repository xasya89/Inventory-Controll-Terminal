package com.example.inventorycontroll.inventoryDatabase.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.inventorycontroll.inventoryDatabase.Converters
import java.math.BigDecimal

@Entity(tableName = "inventorygoods")
@TypeConverters(Converters::class)
data class InventoryGood(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val groupId: Long,
    val goodId: Long,
    var count: BigDecimal,
    var price: BigDecimal
)
