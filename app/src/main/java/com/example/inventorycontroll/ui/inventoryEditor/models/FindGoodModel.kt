package com.example.inventorycontroll.ui.inventoryEditor.models

import java.math.BigDecimal

data class FindGoodModel(val id: Long, val name: String, val price: BigDecimal, var isSelected: Boolean = false)
