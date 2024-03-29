package com.example.inventorycontroll.inventoryDatabase.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.inventorycontroll.inventoryDatabase.Converters
import java.math.BigDecimal

@Entity(tableName = "goods", indices = [Index(value = ["shopDbName"])])
@TypeConverters(Converters::class)
data class Good (
    @PrimaryKey(autoGenerate = true)
    val id:Long,
    val shopDbName: String,
    val groupId: Long,
    val uuid: String,
    val name: String,
    val unit: GoodUnit,
    val specialType: SpecilType,
    val price: BigDecimal,
    val isDeleted: Boolean = false

)