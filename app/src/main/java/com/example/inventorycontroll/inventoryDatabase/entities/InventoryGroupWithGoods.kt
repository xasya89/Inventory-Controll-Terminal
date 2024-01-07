package com.example.inventorycontroll.inventoryDatabase.entities

import androidx.room.Embedded
import androidx.room.Relation

data class InventoryGroupWithGoods (
    @Embedded val group: InventoryGroup,
    @Relation(
        parentColumn = "id",
        entityColumn = "groupId"
    )
    val goods: List<InventoryGood>
)

data class InvnetoryPositionWithGood (
    @Embedded val position: InventoryGood,
    @Relation(
        parentColumn = "goodId",
        entityColumn = "id"
    )
    val good: Good
)