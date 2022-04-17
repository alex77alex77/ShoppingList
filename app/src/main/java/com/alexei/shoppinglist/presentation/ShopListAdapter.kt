package com.alexei.shoppinglist.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import com.alexei.shoppinglist.R
import com.alexei.shoppinglist.databinding.ShopDesableItemBinding
import com.alexei.shoppinglist.databinding.ShopEnabledItemBinding
import com.alexei.shoppinglist.domain.ShopItem

class ShopListAdapter : ListAdapter<ShopItem,ShopItemViewHolder>(ShopItemDiffCallback()) {


    var onShopItemLongClickListener: ((ShopItem) -> Unit)? =null
    var onShopItemClickListener: ((ShopItem) -> Unit)? =null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {

        val layout = when (viewType) {
            VIEW_TYPE_ENABLED -> R.layout.shop_enabled_item
            VIEW_TYPE_DISABLED -> R.layout.shop_desable_item
            else -> throw RuntimeException("Unknown view type: $viewType")
        }
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),layout, parent, false
        )

        return ShopItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        val item = getItem(position)
        val binding = holder.binding

        binding.root.setOnLongClickListener {
            onShopItemLongClickListener?.invoke(item)
            true
        }
        binding.root.setOnClickListener {
            onShopItemClickListener?.invoke(item)
            true
        }

        when(binding){
            is ShopDesableItemBinding->{
                binding.shopItem = item
            }
            is ShopEnabledItemBinding->{
                binding.shopItem = item
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return if (item.enabled) {
            VIEW_TYPE_ENABLED
        } else {
            VIEW_TYPE_DISABLED
        }
    }

    companion object {
        const val VIEW_TYPE_DISABLED = 0
        const val VIEW_TYPE_ENABLED = 1

        const val POOL_RECYCLERVIEW = 20
    }
}