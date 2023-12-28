package com.example.inventorycontroll.communication.model

import java.math.BigDecimal
import java.util.Date

data class InventoryModelApi(
    val uuid: String,
    val start: Date,
    val cashMoneyFact: BigDecimal,
    val groups: List<InvnetoryGroupModelApi>
)

data class InvnetoryGroupModelApi(
    val name: String,
    val goods: List<InventoryGoodModelApi>
)

data class InventoryGoodModelApi(
    val uuid: String,
    val price: BigDecimal,
    val count: BigDecimal
)