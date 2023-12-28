package com.example.inventorycontroll.inventoryDatabase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.inventorycontroll.inventoryDatabase.entities.Inventory
import com.example.inventorycontroll.inventoryDatabase.entities.InventoryGood
import com.example.inventorycontroll.inventoryDatabase.entities.InventoryGroup
import com.example.inventorycontroll.inventoryDatabase.entities.InventoryGroupWithGoods
import com.example.inventorycontroll.ui.inventoryEditor.models.InventoryPositionModel

@Dao
interface InventoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertInventory(inventory: Inventory): Long
    @Update()
    fun  updateInventory(inventory: Inventory)
    @Query("SELECT * FROM inventories WHERE id=:id LIMIT 1")
    fun getInventoryById(id: Long): Inventory?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertGroup(inventoryGroup: InventoryGroup): Long
    @Update
    fun updateGroups(groups: List<InventoryGroup>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertGood(inventoryGood: InventoryGood): Long
    @Update
    fun updateGood(inventoryGood: InventoryGood)
    @Query("SELECT * FROM inventorygoods WHERE id=:id")
    fun getGood(id: Long): InventoryGood
    @Query("SELECT p.*, g.name AS goodName FROM inventorygoods p INNER JOIN goods g ON p.goodId=g.id WHERE p.groupId=:groupId")
    fun getGoods(groupId: Long): List<InventoryPositionModel>

    @Query("SELECT * FROM inventories WHERE shopDbName=:shopDbName AND isStop=0 AND isCancel=0")
    fun getExistInventory(shopDbName: String):List<Inventory>

    @Query("SELECT * FROM inventorygroups WHERE inventoryId=:inventoryId")
    fun getGroups(inventoryId:Long): List<InventoryGroup>

    @Query("SELECT * FROM inventorygroups WHERE inventoryId=:id")
    fun getGroupsWithGoods(id: Long):List<InventoryGroupWithGoods>
}