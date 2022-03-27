package com.alexei.shoppinglist.domain

class AddShopItemUseCase(private val shopListRepository: ShopListRepository) {
    fun addItem(shopItem: ShopItem) {
        shopListRepository.addShopItem(shopItem)
    }
}