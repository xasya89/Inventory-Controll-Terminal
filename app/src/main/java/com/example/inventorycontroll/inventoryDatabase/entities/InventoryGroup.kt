package com.example.inventorycontroll.inventoryDatabase.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.inventorycontroll.inventoryDatabase.Converters
import java.math.BigDecimal

@Entity(tableName = "inventorygroups")
@TypeConverters(Converters::class)
data class InventoryGroup(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    val inventoryId: Long,
    val groupName: String,
    var sum: BigDecimal
)
