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
    private val repository = ShopListRepositoryImpl//---------------изменить реализацию!

    private val getShopItemUseCase = GetShopItemUseCase(repository)
    private val addItemUseCase = AddShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)

    private val _inputNameError = MutableLiveData<Boolean>()//ссылка на MutableLiveData изменение значения здесь
    val inputNameError: LiveData<Boolean>
        //из активити подписываться на данную переменную(LiveData-устанавливатьз начения нельзя)
        get() = _inputNameError

    private val _inputCountError = MutableLiveData<Boolean>()//ссылка на MutableLiveData изменение значения здесь
    val inputCountError: LiveData<Boolean>
        //из активити подписываться на данную переменную(LiveData-устанавливатьз начения нельзя)
        get() = _inputCountError

    fun getShopItem(itemId: Int) {
        val item = getShopItemUseCase.getShopItem(itemId)

    }

    fun addShopItem(name: String?, count: String?) {
        val name = parseName(name)
        val count = parseCount(count)
        val valid = validateField(name, count)//----------------------validate полей
        if (valid) {
            val newItem = ShopItem(name, count, true)//-------создание модели
            addItemUseCase.addItem(newItem)
        }
    }

    fun editShopItem(shopItem: ShopItem) {
        editShopItemUseCase.editItem(shopItem)
    }

    private fun parseName(name: String?): String {//принимает нулабельную строку возврашает строку
        return name?.trim() ?: ""//если не null то обрезаем пробелы иначе ""
    }

    private fun parseCount(count: String?): Int {//принимает нулабельную строку возврашает число
        return try {
            count?.trim()?.toInt() ?: 0//преобразование а Int иначе вернуть ноль
        } catch (e: Exception) {
            0//вернуть ноль, если введено не число
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

    fun resetErrorName() {//если после показа ошибки пользователь начал ввод то сброс ошибки
        _inputNameError.value = false
    }
    fun resetErrorCount() {//если после показа ошибки пользователь начал ввод то сброс ошибки
        _inputCountError.value = false
    }
}