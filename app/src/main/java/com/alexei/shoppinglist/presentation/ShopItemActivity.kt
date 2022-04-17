package com.alexei.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.alexei.shoppinglist.R
import com.alexei.shoppinglist.domain.ShopItem
import com.alexei.shoppinglist.presentation.fragment.ShopItemFragment

class ShopItemActivity : AppCompatActivity(),ShopItemFragment.OnEditingFinishedListener {

    private var screenMode = MODE_UNKNOWN
    private var shopItemId = ShopItem.UNDEFINED_ID


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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)

        parseIntent()

        if (savedInstanceState==null){
            launchMode()
        }
    }

    private fun launchMode() {
        val fragment = when (screenMode) {
                MODE_EDIT -> ShopItemFragment.instanceEditItem(shopItemId)
                MODE_ADD -> ShopItemFragment.instanceAddItem()
                else -> throw RuntimeException("Неизвестный режим экрана $screenMode")
            }
        supportFragmentManager.beginTransaction()
            .replace(R.id.shopItemContainer, fragment)
            .commit()
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
            shopItemId = intent.getIntExtra(EXTRA_SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)

        }
    }

    override fun onEditingFinishedListener() {
        finish()
    }
}