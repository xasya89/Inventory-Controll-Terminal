package com.example.inventorycontroll.inventoryDatabase.entities.modifyEntities

import java.math.BigDecimal

data class BalanceAndInventoryCount(
    val goodId: Long,
    val name: String,
    val inventoryCount: BigDecimal,
    val balanceCount: BigDecimal
)
