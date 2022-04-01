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
        //----пременная для работы из активити
        get() = _shopItem

    fun getShopItem(itemId: Int) {
        val item = getShopItemUseCase.getShopItem(itemId)
        _shopItem.value = item//полученый елемент устанавливаем в LiveData
    }

    fun addShopItem(name: String?, count: String?) {
        val name = parseName(name)
        val count = parseCount(count)
        val valid = validateField(name, count)
        if (valid) {
            val newItem = ShopItem(name, count, true)
            addItemUseCase.addItem(newItem)
            finishWork()
        }
    }

    fun editShopItem(name: String?, count: String?) {
        val name = parseName(name)
        val count = parseCount(count)
        val valid = validateField(name, count)
        if (valid) {
            _shopItem.value?.let {//---------let выпоняем действия если не содержит null
                val item =it.copy(name=name,count=count)//--копируем существующий объект и изменяем поля
                editShopItemUseCase.editItem(item)//--------отправка в метод editItem класса editShopItemUseCase который в конструкторе получает класс ListRepositoryImpl в котором имплементируются все методы интерфейса ShopListRepository
                finishWork()
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