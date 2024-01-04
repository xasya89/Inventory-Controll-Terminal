package com.example.inventorycontroll.communication.model

import java.math.BigDecimal

data class BalanceModelApi(
    val count: Int,
    val items: List<BalanceItemModelApi>
)

data class BalanceItemModelApi(
    val uuid: String,
    val balance: BigDecimal
)