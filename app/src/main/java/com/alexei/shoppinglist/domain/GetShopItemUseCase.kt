package com.alexei.shoppinglist.domain

class GetShopItemUseCase(private val shopListRepository: ShopListRepository) {
    suspend fun getShopItem(itemId:Int):ShopItem{
        return shopListRepository.getShopItem(itemId)
    }
}