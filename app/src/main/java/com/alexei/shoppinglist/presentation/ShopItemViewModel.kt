package com.alexei.shoppinglist.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alexei.shoppinglist.data.ShopListRepositoryImpl
import com.alexei.shoppinglist.domain.AddShopItemUseCase
import com.alexei.shoppinglist.domain.EditShopItemUseCase
import com.alexei.shoppinglist.domain.GetShopItemUseCase
import com.alexei.shoppinglist.domain.ShopItem

class ShopItemViewModel : ViewModel() {
    private val repository = ShopListRepositoryImpl//!!!!!

    private val getShopItemUseCase = GetShopItemUseCase(repository)
    private val addItemUseCase = AddShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)

    private val _inputNameError = MutableLiveData<Boolean>()
    val inputNameError: LiveData<Boolean>
        get() = _inputNameError

    private val _inputCountError = MutableLiveData<Boolean>()
    val inputCountError: LiveData<Boolean>
        get() = _inputCountError

    fun getShopItem(itemId: Int) {
        val item = getShopItemUseCase.getShopItem(itemId)

    }

    fun addShopItem(name: String?, count: String?) {
        val name = parseName(name)
        val count = parseCount(count)
        val valid = validateField(name, count)
        if (valid) {
            val newItem = ShopItem(name, count, true)
            addItemUseCase.addItem(newItem)
        }
    }

    fun editShopItem(shopItem: ShopItem) {
        editShopItemUseCase.editItem(shopItem)
    }

    private fun parseName(name: String?): String {
        return name?.trim() ?: ""
    }

    private fun parseCount(count: String?): Int {
        return try {
            count?.trim()?.toInt() ?: 0
        } catch (e: Exception) {
            0
        }
    }

    private fun validateField(name: String, count: Int): Boolean {
        var result = true
        if (name.isBlank()) {
            _inputNameError.value = true
            return false
        }
        if (count <= 0) {
            _inputCountError.value=true
            return false
        }
        return result
    }

    fun resetErrorName() {
        _inputNameError.value = false
    }
    fun resetErrorCount() {
        _inputCountError.value = false
    }
}