package com.example.inventorycontroll.inventoryDatabase

import androidx.room.TypeConverter
import com.example.inventorycontroll.inventoryDatabase.entities.GoodUnit
import com.example.inventorycontroll.inventoryDatabase.entities.SpecilType
import java.math.BigDecimal

class Converters {
    @TypeConverter
    fun fromLong(value: Long?): BigDecimal? {
        return if (value == null) null else BigDecimal(value).divide(BigDecimal(100))
    }

    @TypeConverter
    fun toLong(bigDecimal: BigDecimal?): Long? {
        return if (bigDecimal == null) {
            null
        } else {
            bigDecimal.multiply(BigDecimal(100)).longValueExact()
        }
    }

    @TypeConverter
    fun toSpecialType(value: Int) = enumValues<SpecilType>()[value]

    @TypeConverter
    fun fromSpecialType(value: SpecilType) = value.ordinal



    @TypeConverter
    fun toUnitType(value: Int) = GoodUnit.getGoodUnit(value)

    @TypeConverter
    fun fromUnitType(value: GoodUnit) = value.value
}