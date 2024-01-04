package com.example.inventorycontroll.inventoryDatabase.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.inventorycontroll.inventoryDatabase.entities.InventoryGoodGroupingItem

@Dao
interface InventoryGroupingDao {
    @Query("SELECT g.goodId, SUM(g.count) AS countFact FROM inventorygroups gr INNER JOIN inventorygoods g ON gr.id=g.groupId WHERE gr.inventoryId=:inventoryId GROUP BY g.goodId")
    fun getCountGroupingGood(inventoryId: Long): List<InventoryGoodGroupingItem>
}