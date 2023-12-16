package com.example.inventorycontroll.inventoryDatabase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.inventorycontroll.inventoryDatabase.entities.Inventory
import com.example.inventorycontroll.inventoryDatabase.entities.InventoryGood
import com.example.inventorycontroll.inventoryDatabase.entities.InventoryGroup

@Dao
interface InventoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertInventory(inventory: Inventory): Long
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertGroup(inventoryGroup: InventoryGroup): Long
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertGood(inventoryGood: InventoryGood): Long

    @Query("SELECT * FROM inventories WHERE shopDbName=:shopDbName AND isStop=0")
    fun getExistInventory(shopDbName: String):List<Inventory>

    @Query("SELECT * FROM inventorygroups WHERE inventoryId=:inventoryId")
    fun getGroups(inventoryId:Long): List<InventoryGroup>
}