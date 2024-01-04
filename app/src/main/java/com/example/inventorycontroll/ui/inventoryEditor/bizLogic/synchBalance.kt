package com.example.inventorycontroll.ui.inventoryEditor.bizLogic

import com.example.inventorycontroll.communication.BalanceApiService
import com.example.inventorycontroll.communication.model.BalanceItemModelApi
import com.example.inventorycontroll.communication.model.BalanceModelApi
import com.example.inventorycontroll.inventoryDatabase.dao.BalanceDao
import com.example.inventorycontroll.inventoryDatabase.dao.GoodDao
import com.example.inventorycontroll.inventoryDatabase.entities.Balance
import com.example.inventorycontroll.inventoryDatabase.entities.Good

//Получение текущего баланса
class SynchBalance(private val shopDbName: String, private val api: BalanceApiService, private val dao: BalanceDao, private val goodDao: GoodDao) {
    suspend fun synch(){
        dao.deleteAll(shopDbName)

        val goods = goodDao.getGoods(shopDbName)

        var response = api.getBalance(0, 400)
        var skip = 200
        val count = response.count
        insertBalance(goods, response.items)

        while (skip<count){
            response = api.getBalance(skip, 400)
            insertBalance(goods, response.items)
            skip+=400
        }
    }

    private suspend fun insertBalance(goods: List<Good>, items: List<BalanceItemModelApi>){
        dao.InsertAll(items.map { balance ->
            Balance(0, goods.first { it.uuid==balance.uuid }.id, shopDbName, balance.balance)
        })
    }
}