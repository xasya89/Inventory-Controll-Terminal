package com.example.inventorycontroll.inventoryDatabase.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.inventorycontroll.inventoryDatabase.Converters

@Entity(tableName = "goodgroups", indices = [Index(value = ["shopDbName"])])
@TypeConverters(Converters::class)
data class GoodGroup(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val idServer: Long,
    val shopDbName: String,
    val name: String
)
