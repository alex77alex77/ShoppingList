package com.alexei.shoppinglist.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.alexei.shoppinglist.data.ShopListRepositoryImpl
import com.alexei.shoppinglist.domain.DelShopItemUseCase
import com.alexei.shoppinglist.domain.EditShopItemUseCase
import com.alexei.shoppinglist.domain.GetShopListUseCase
import com.alexei.shoppinglist.domain.ShopItem
import kotlinx.coroutines.launch


class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ShopListRepositoryImpl(application)//!!!!

    private val getShopListUseCase = GetShopListUseCase(repository)
    private val delShopItemUseCase = DelShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)

    val shopList = getShopListUseCase.getShopList()

    fun deleteShopItem(item: ShopItem) {
        viewModelScope.launch {
            delShopItemUseCase.deleteItem(item)
        }
    }

    fun changeEnableState(item: ShopItem) {
        viewModelScope.launch {
            val newItem = item.copy(enabled = !item.enabled)
            editShopItemUseCase.editItem(newItem)
        }
    }

}