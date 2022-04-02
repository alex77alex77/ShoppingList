package com.alexei.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.alexei.shoppinglist.R
import com.alexei.shoppinglist.domain.ShopItem
import com.google.android.material.textfield.TextInputLayout

class ShopItemActivity : AppCompatActivity() {
    private lateinit var viewModel: ShopItemViewModel

    private lateinit var tilName: TextInputLayout
    private lateinit var tilCount: TextInputLayout
    private lateinit var etName: EditText
    private lateinit var etCount: EditText
    private lateinit var btnSave: Button

    private var screenMode = MODE_UNKNOWN
    private var screenItemId = ShopItem.UNDEFINED_ID

    companion object {
        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val EXTRA_SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val MODE_UNKNOWN = ""

        fun intentAddShopItem(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
            return intent
        }

        fun intentEditShopItem(context: Context, itemId: Int): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
            intent.putExtra(EXTRA_SHOP_ITEM_ID, itemId)
            return intent
        }
    }

    private fun initViews() {
        tilName = findViewById(R.id.tilName)
        etName = findViewById(R.id.etName)
        tilCount = findViewById(R.id.tilCount)
        etCount = findViewById(R.id.etCount)
        btnSave = findViewById(R.id.btnSave)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)
        // ---получаем данные из интента
        parseIntent()
        // ---инициализация ViewModel
        viewModel = ViewModelProvider(this).get(ShopItemViewModel::class.java)
        // ---инициализация View элементов
        initViews()
        // ---слушатели ввода текста(для сброса ошибки)
        changeFieldsTextListener()
        // ---запуск правильного режима экрана
        setCorrectMode()
        // ---подписка на все объекты во ViewModel(ShopItemViewModel)
        observeViewModel()

    }

    private fun observeViewModel() {
        viewModel.inputCountError.observe(this) {
            val message = if (it) {
                getString(R.string.err_input_count)
            } else {
                null
            }
            tilCount.error = message
        }

        viewModel.inputNameError.observe(this) {
            val message = if (it) {
                getString(R.string.err_input_name)
            } else {
                null
            }
            tilName.error = message
        }

        viewModel.closeActivity.observe(this) {//слушатель сработает, когда поток закончит задачу после btnSave.setOnClickListener определенных в разных режимах Activity
            finish()
        }
    }

    private fun setCorrectMode() {
        when (screenMode) {//---------------выбор режима для инициализации елементов в активити
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }
    }

    private fun changeFieldsTextListener() {
        etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorName()
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        etCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorCount()
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }

    private fun launchEditMode() {
        viewModel.getShopItem(screenItemId)//получаем елемент по его id
        //подписка на этот элемент
        viewModel.shopItem.observe(this) {
            //  заполнить поля ввода
            etName.setText(it.name)
            etCount.setText(it.count.toString())
        }
        btnSave.setOnClickListener {
            viewModel.editShopItem(etName.text?.toString(), etCount.text?.toString())
        }

    }

    private fun launchAddMode() {
        btnSave.setOnClickListener {
            viewModel.addShopItem(etName.text?.toString(), etCount.text?.toString())
        }
    }

    private fun parseIntent() {
        if (!intent.hasExtra(EXTRA_SCREEN_MODE)) {
            throw RuntimeException("Параметр режим экрана отсутствует")
        }

        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)
        if (mode != MODE_EDIT && mode != MODE_ADD) {
            throw RuntimeException("Неизвестный режим экрана $mode")
        }

        screenMode = mode
        if (screenMode == MODE_EDIT) {
            if (!intent.hasExtra(EXTRA_SHOP_ITEM_ID)) {

                throw RuntimeException("Параметр идентификатор товара отсутствует")
            }
            screenItemId = intent.getIntExtra(EXTRA_SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }
}