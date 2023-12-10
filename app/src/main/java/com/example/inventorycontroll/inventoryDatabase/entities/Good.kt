package com.example.inventorycontroll.inventoryDatabase.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.inventorycontroll.inventoryDatabase.Converters
import java.math.BigDecimal

@Entity(tableName = "goods")
@TypeConverters(Converters::class)
data class Good (
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val Name: String,
    val unit: GoodUnit,
    val specialType: SpecilType,
    val price: BigDecimal
)