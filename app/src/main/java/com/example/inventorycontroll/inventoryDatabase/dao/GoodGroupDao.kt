package com.example.inventorycontroll.inventoryDatabase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.inventorycontroll.inventoryDatabase.entities.GoodGroup

@Dao
interface GoodGroupDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(goodGroup: GoodGroup): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(goodGroups: List<GoodGroup>)

    @Query("UPDATE goodgroups SET name=:name WHERE shopDbName=:shopDbName AND idServer=:idServer")
    fun update(name: String, shopDbName: String, idServer: Long)

    @Query("SELECT * FROM goodgroups WHERE shopDbName=:shopDbName ORDER BY name")
    fun get(shopDbName: String): List<GoodGroup>

    @Query("SELECT * FROM goodgroups WHERE shopDbName=:shopDbName AND idServer=:idServer")
    fun get(idServer: Long, shopDbName: String): GoodGroup?

    @Query("DELETE FROM goodgroups WHERE id=:id")
    fun delete(id: Long)
}