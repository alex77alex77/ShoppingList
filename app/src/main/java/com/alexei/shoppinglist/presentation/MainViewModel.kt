package com.alexei.shoppinglist.presentation

import androidx.lifecycle.ViewModel
import com.alexei.shoppinglist.data.ShopListRepositoryImpl
import com.alexei.shoppinglist.domain.DelShopItemUseCase
import com.alexei.shoppinglist.domain.EditShopItemUseCase
import com.alexei.shoppinglist.domain.GetShopListUseCase
import com.alexei.shoppinglist.domain.ShopItem


class MainViewModel : ViewModel() {
    private val repository = ShopListRepositoryImpl//!!!!

    private val getShopListUseCase = GetShopListUseCase(repository)
    private val delShopItemUseCase = DelShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)


    val shopList = getShopListUseCase.getShopList()

    fun deleteShopItem(item: ShopItem) {
        delShopItemUseCase.deleteItem(item)
    }

    fun changeEnableState(item: ShopItem) {
        val newItem = item.copy(enabled = !item.enabled)
        editShopItemUseCase.editItem(newItem)
    }
}