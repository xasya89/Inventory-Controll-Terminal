package com.example.inventorycontroll.ui.inventoryEditor.models

import java.math.BigDecimal

data class BalanceDiffItemModel(
    val goodId: Long,
    val goodName: String,
    val countFact: BigDecimal,
    val countFromServer: BigDecimal
)
