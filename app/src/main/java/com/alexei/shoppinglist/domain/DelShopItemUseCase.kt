package com.alexei.shoppinglist.domain

class DelShopItemUseCase(private val shopListRepository: ShopListRepository) {
    fun deleteItem(shopItem: ShopItem) {
        shopListRepository.deleteShopItem(shopItem)
    }
}