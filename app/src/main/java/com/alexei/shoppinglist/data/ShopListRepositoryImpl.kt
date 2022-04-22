package com.alexei.shoppinglist.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.alexei.shoppinglist.domain.ShopItem
import com.alexei.shoppinglist.domain.ShopListRepository

class ShopListRepositoryImpl(application: Application) : ShopListRepository {

    private val shopListDAO = AppDatabase.getInstance(application).shopListDAO()
    private val mapper = ShopListMapper()

    override suspend fun addShopItem(shopItem: ShopItem) {
        shopListDAO.addShopItem(mapper.mapEntityToDbModel(shopItem))
    }

    override suspend fun deleteShopItem(shopItem: ShopItem) {
        shopListDAO.deleteShopItem(shopItem.id)
    }

    override suspend fun editShopItem(shopItem: ShopItem) {
        shopListDAO.addShopItem(mapper.mapEntityToDbModel(shopItem))
    }

    override suspend fun getShopItem(itemId: Int): ShopItem {
        val dbModel = shopListDAO.getShopItem(itemId)
        return mapper.mapDbModelToEntity(dbModel)
    }

    override fun getShopList(): LiveData<List<ShopItem>> = Transformations.map(shopListDAO.getShopList()){
        mapper.mapListDbModelToListEntity(it)
    }

}