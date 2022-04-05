package com.alexei.shoppinglist.presentation.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.alexei.shoppinglist.R
import com.alexei.shoppinglist.domain.ShopItem
import com.alexei.shoppinglist.presentation.ShopItemActivity
import com.alexei.shoppinglist.presentation.ShopItemViewModel
import com.google.android.material.textfield.TextInputLayout

class ShopItemFragment : Fragment() {

    private lateinit var viewModel: ShopItemViewModel
    private lateinit var tilName: TextInputLayout
    private lateinit var tilCount: TextInputLayout
    private lateinit var etName: EditText
    private lateinit var etCount: EditText
    private lateinit var btnSave: Button

    private var screenMode: String = MODE_UNKNOWN
    private var shopItemId: Int = ShopItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        parseParams()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.shop_item_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel = ViewModelProvider(this).get(ShopItemViewModel::class.java)

        initViews(view)

        changeFieldsTextListener()

        setCorrectMode()

        observeViewModel()

    }


    companion object {
        private const val SCREEN_MODE = "extra_mode"
        private const val SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val MODE_UNKNOWN = ""


        fun instanceAddItem(): ShopItemFragment {

            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_ADD)
                }
            }
        }

        fun instanceEditItem(itemId: Int): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_EDIT)
                    putInt(SHOP_ITEM_ID, itemId)
                }
            }
        }
    }

    private fun initViews(view: View) {
        tilName = view.findViewById(R.id.tilName)
        etName = view.findViewById(R.id.etName)
        tilCount = view.findViewById(R.id.tilCount)
        etCount = view.findViewById(R.id.etCount)
        btnSave = view.findViewById(R.id.btnSave)
    }

    private fun observeViewModel() {
        viewModel.inputCountError.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.err_input_count)
            } else {
                null
            }
            tilCount.error = message
        }

        viewModel.inputNameError.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.err_input_name)
            } else {
                null
            }
            tilName.error = message
        }

        viewModel.closeActivity.observe(viewLifecycleOwner) {
            activity?.onBackPressed()
        }
    }

    private fun setCorrectMode() {
        when (screenMode) {
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
        viewModel.getShopItem(shopItemId)

        viewModel.shopItem.observe(viewLifecycleOwner) {
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

    private fun parseParams() {
        val args = requireArguments()
        if (!args.containsKey(SCREEN_MODE)) {
            throw RuntimeException("Параметр - режим экрана отсутствует")
        }
        val mode = args.getString(SCREEN_MODE)
        if (mode != MODE_EDIT && mode != MODE_ADD) {
            throw RuntimeException("Неизвестный режим экрана $mode")
        }
        screenMode = mode
        if (screenMode == MODE_EDIT) {
            if (!args.containsKey(SHOP_ITEM_ID)) {
                throw RuntimeException("Параметр - идентификатор товара отсутствует")
            }
        }
        shopItemId = args.getInt(SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
    }
}