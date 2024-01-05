package com.example.inventorycontroll.inventoryDatabase.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.inventorycontroll.inventoryDatabase.entities.InventoryGoodGroupingItem
import com.example.inventorycontroll.inventoryDatabase.entities.modifyEntities.BalanceAndInventoryCount

@Dao
interface InventoryGroupingDao {
    @Query("SELECT g.goodId, SUM(g.count) AS countFact FROM inventorygroups gr INNER JOIN inventorygoods g ON gr.id=g.groupId WHERE gr.inventoryId=:inventoryId GROUP BY g.goodId")
    fun getCountGroupingGood(inventoryId: Long): List<InventoryGoodGroupingItem>
    /*
    @Query("SELECT b.goodId, IFNULL(t1.inventoryCount, 0) AS inventoryCount, b.balanceCount FROM " +
            "(SELECT goodId, balanceCount FROM balance WHERE shopDbName=:shopDbName) b " +
            "LEFT JOIN (SELECT g.goodId, COUNT(g.count) AS inventoryCount FROM inventorygroups gr INNER JOIN inventorygoods g ON gr.id=g.groupId WHERE gr.inventoryId=:inventoryId GROUP BY g.goodId) t1 " +
            "ON t1.goodId=b.goodId " +
            "WHERE IFNULL(t1.inventoryCount, 0)<>b.balanceCount ")
    */
    @Query("SELECT g.name, t2.* FROM " +
            "    ( " +
            "    SELECT b.goodId, IFNULL(t1.inventoryCount, 0) AS inventoryCount, b.balanceCount FROM " +
            "    (SELECT goodId, balanceCount FROM balance WHERE shopDbName=:shopDbName) b " +
            "    LEFT JOIN (SELECT g.goodId, COUNT(g.count) AS inventoryCount FROM inventorygroups gr INNER JOIN inventorygoods g ON gr.id=g.groupId WHERE gr.inventoryId=:inventoryId GROUP BY g.goodId) t1 " +
            "    ON b.goodId=t1.goodId " +
            "    WHERE IFNULL(t1.inventoryCount, 0)<>b.balanceCount " +
            "    ) t2 " +
            "INNER JOIN goods g " +
            "ON t2.goodId=g.id " +
            "ORDER BY g.name")
    fun getCountBalanceAndInventory(inventoryId: Long, shopDbName:String): List<BalanceAndInventoryCount>
}