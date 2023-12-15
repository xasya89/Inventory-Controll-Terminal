package com.example.inventorycontroll.inventoryDatabase

import androidx.room.TypeConverter
import com.example.inventorycontroll.inventoryDatabase.entities.GoodUnit
import com.example.inventorycontroll.inventoryDatabase.entities.SpecilType
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Date

class Converters {
    @TypeConverter
    fun fromLong(value: Long?): BigDecimal? {
        return if (value == null) null else BigDecimal(value).divide(BigDecimal(1000))
    }

    @TypeConverter
    fun toLong(bigDecimal: BigDecimal?): Long? {
        return if (bigDecimal == null) {
            null
        } else {
            bigDecimal.multiply(BigDecimal(1000)).longValueExact()
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

    @TypeConverter
    fun toDate(value: String?): Date?{
        if(value==null) return null
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")//.ofPattern("yyyy-MM-dd HH:mm:ss")
        return format.parse(value)
    }

    @TypeConverter
    fun fromDate(value: Date?): String?{
        if(value==null) return null
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return format.format(value)
    }
}