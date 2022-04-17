package com.alexei.shoppinglist.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.alexei.shoppinglist.databinding.ShopItemFragmentBinding
import com.alexei.shoppinglist.domain.ShopItem
import com.alexei.shoppinglist.presentation.ShopItemViewModel

class ShopItemFragment : Fragment() {
    private var _binding: ShopItemFragmentBinding? = null
    private val binding: ShopItemFragmentBinding
        get() = _binding ?: throw RuntimeException("ShopItemFragmentBinding == null")

    private lateinit var viewModel: ShopItemViewModel
    lateinit var onEditingFinishedListener: OnEditingFinishedListener//---------ссылка на интерфейс

    private var screenMode: String = MODE_UNKNOWN
    private var shopItemId: Int = ShopItem.UNDEFINED_ID

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnEditingFinishedListener) {
            onEditingFinishedListener = context
        } else {
            throw RuntimeException("Activity must implement OnEditingFinishedListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        parseParams()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ShopItemFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ShopItemViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

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

    interface OnEditingFinishedListener {
        fun onEditingFinishedListener()
    }

    private fun observeViewModel() {

        viewModel.closeActivity.observe(viewLifecycleOwner) {
            onEditingFinishedListener.onEditingFinishedListener()

        }
    }

    private fun setCorrectMode() {
        when (screenMode) {
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }
    }

    private fun changeFieldsTextListener() {
        binding.etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorName()
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        binding.etCount.addTextChangedListener(object : TextWatcher {
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
            binding.etName.setText(it.name)
            binding.etCount.setText(it.count.toString())
        }
        binding.btnSave.setOnClickListener {
            viewModel.editShopItem(
                binding.etName.text?.toString(),
                binding.etCount.text?.toString()
            )
        }

    }

    private fun launchAddMode() {
        binding.btnSave.setOnClickListener {
            viewModel.addShopItem(binding.etName.text?.toString(), binding.etCount.text?.toString())
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