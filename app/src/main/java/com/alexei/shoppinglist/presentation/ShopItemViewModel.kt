package com.alexei.shoppinglist.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.alexei.shoppinglist.data.ShopListRepositoryImpl
import com.alexei.shoppinglist.domain.AddShopItemUseCase
import com.alexei.shoppinglist.domain.EditShopItemUseCase
import com.alexei.shoppinglist.domain.GetShopItemUseCase
import com.alexei.shoppinglist.domain.ShopItem
import kotlinx.coroutines.launch

class ShopItemViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ShopListRepositoryImpl(application)//!!!!!

    private val getShopItemUseCase = GetShopItemUseCase(repository)
    private val addItemUseCase = AddShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)


    private var _closeActivity = MutableLiveData<Unit>()
    val closeActivity: LiveData<Unit>
        get() = _closeActivity

    private val _inputNameError = MutableLiveData<Boolean>()
    val inputNameError: LiveData<Boolean>
        get() = _inputNameError

    private val _inputCountError = MutableLiveData<Boolean>()
    val inputCountError: LiveData<Boolean>
        get() = _inputCountError

    private val _shopItem = MutableLiveData<ShopItem>()
    val shopItem: LiveData<ShopItem>
        get() = _shopItem

    fun getShopItem(itemId: Int) {
        viewModelScope.launch {
            val item = getShopItemUseCase.getShopItem(itemId)
            _shopItem.value = item
        }
    }

    fun addShopItem(name: String?, count: String?) {

        val name = parseName(name)
        val count = parseCount(count)
        val valid = validateField(name, count)
        if (valid) {
            viewModelScope.launch {
                val newItem = ShopItem(name, count, true)
                addItemUseCase.addItem(newItem)
                finishWork()
            }
        }
    }

    fun editShopItem(name: String?, count: String?) {

        val name = parseName(name)
        val count = parseCount(count)
        val valid = validateField(name, count)
        if (valid) {
            _shopItem.value?.let {
                viewModelScope.launch {
                    val item = it.copy(name = name, count = count)
                    editShopItemUseCase.editItem(item)
                    finishWork()
                }
            }
        }
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
            _inputCountError.value = true
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

    private fun finishWork() {
        _closeActivity.value = Unit
    }

}