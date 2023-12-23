package com.example.inventorycontroll.ui.inventoryEditor.models

import java.math.BigDecimal

data class InventoryPositionModel(
    var id:Long,
    val groupId: Long,
    val goodId: Long,
    val goodName: String,
    var price: BigDecimal,
    var count: BigDecimal
)
