package com.example.inventorycontroll.inventoryDatabase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.inventorycontroll.inventoryDatabase.entities.Balance

@Dao
interface BalanceDao {
    @Query("SELECT * FROM balance WHERE shopDbName=:shopDbName")
    fun getBalance(shopDbName: String):List<Balance>

    @Query("DELETE FROM balance WHERE shopDbName=:shopDbName")
    fun deleteAll(shopDbName: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(balance: Balance)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun InsertAll(balances: List<Balance>)
}