package com.example.inventorycontroll.inventoryDatabase.entities

import java.math.BigDecimal

data class InventoryGoodGroupingItem(
    val goodId: Long,
    val countFact: BigDecimal
)
