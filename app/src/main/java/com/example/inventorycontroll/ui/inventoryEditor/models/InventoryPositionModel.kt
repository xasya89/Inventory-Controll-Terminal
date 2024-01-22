package com.example.inventorycontroll.ui.inventoryEditor.models

import java.math.BigDecimal
import java.util.UUID

data class InventoryPositionModel(
    var uuid: UUID? = null,
    var id:Long,
    val groupId: Long,
    val goodId: Long,
    val goodName: String,
    var price: BigDecimal,
    var count: BigDecimal
)
