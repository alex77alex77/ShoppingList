package com.alexei.shoppinglist.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alexei.shoppinglist.domain.ShopItem
import com.alexei.shoppinglist.domain.ShopListRepository

object ShopListRepositoryImpl : ShopListRepository {
    private val shopListLD = MutableLiveData<List<ShopItem>>()


    private val shopItemList = sortedSetOf<ShopItem>({ o1, o2 -> o1.id.compareTo(o2.id) })//сортировка по ид
    private var autoIncrementId = 0

    init {
        for (i in 0 until 20) {
            var item = ShopItem("Product$i", i, true)
            addShopItem(item)
        }
    }

    override fun addShopItem(shopItem: ShopItem) {
        if (shopItem.id == ShopItem.UNDEFINED_ID) {
            shopItem.id = autoIncrementId++
        }

        shopItemList.add(shopItem)
        updateList()
    }

    override fun deleteShopItem(shopItem: ShopItem) {
        shopItemList.remove(shopItem)
        updateList()
    }

    override fun editShopItem(shopItem: ShopItem) {
        val oldItem = getShopItem(shopItem.id)
        shopItemList.remove(oldItem)
        addShopItem(shopItem)
    }

    override fun getShopItem(itemId: Int): ShopItem {
        return shopItemList.find {
            it.id == itemId
        } ?: throw RuntimeException("Элемент с id - $itemId не найден!")//обработка null
    }

    override fun getShopList(): LiveData<List<ShopItem>> {
        return shopListLD
    }

    private fun updateList() {
        shopListLD.value = shopItemList.toList()//помещаем в LiveData
    }
}