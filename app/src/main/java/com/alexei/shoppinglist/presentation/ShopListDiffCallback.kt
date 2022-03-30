package com.alexei.shoppinglist.presentation

import androidx.recyclerview.widget.DiffUtil
import com.alexei.shoppinglist.domain.ShopItem

class ShopListDiffCallback(private val oldList: List<ShopItem>, private val newList: List<ShopItem>):DiffUtil.Callback() {
    override fun getOldListSize(): Int {
      return  oldList.size
    }

    override fun getNewListSize(): Int {
        return  newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id==newList[newItemPosition].id //тотже ли объект
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {

        return oldList[oldItemPosition]==newList[newItemPosition]
    }
}