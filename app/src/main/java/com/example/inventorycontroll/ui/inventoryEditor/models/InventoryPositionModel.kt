package com.example.inventorycontroll.ui.inventoryEditor.models

import java.math.BigDecimal

data class InventoryPositionModel(
    val id:Long,
    val groupId: Long,
    val goodId: Long,
    val goodName: String,
    val price: BigDecimal,
    val count: BigDecimal
)
