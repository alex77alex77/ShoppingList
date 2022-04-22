package com.alexei.shoppinglist.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shop_items")
data class ShopItemDBModel(
    @PrimaryKey(autoGenerate = true)
    var id: Int ,
    val name: String,
    val count: Int,
    val enabled: Boolean
)