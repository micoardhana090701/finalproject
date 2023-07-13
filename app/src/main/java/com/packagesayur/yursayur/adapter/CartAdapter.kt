package com.packagesayur.yursayur.adapter

import android.app.Application
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.packagesayur.yursayur.ViewModelFactory
import com.packagesayur.yursayur.databinding.ItemCartBinding
import com.packagesayur.yursayur.response.CartItemsItem
import com.packagesayur.yursayur.user.UserPreferences
import com.packagesayur.yursayur.viewmodel.CartViewModel
import com.packagesayur.yursayur.viewmodel.ItemCartViewModel
import kotlinx.coroutines.flow.first

class CartAdapter(
    private val cartList: List<CartItemsItem?>,
    private val application: Application,
    private val preferences: UserPreferences,
    private var itemCartViewModel: ItemCartViewModel,
    private val context: Context
    )
    : RecyclerView.Adapter<CartAdapter.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapter.ViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartAdapter.ViewHolder, position: Int) {
        val cartItem = cartList[position]
        if (cartItem != null) {
            holder.bind(cartItem)
            holder.binding.checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked){
                    cartItem.isSelected = 1
                } else{
                    cartItem.isSelected = 0
                    updateSelectedItemInDatabase(cartItem)
                }
            }
        }
    }
    inner class ViewHolder(val binding: ItemCartBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(cartItem: CartItemsItem?){
            with(binding){
                tvItemCount.setText(cartItem?.quantity.toString())
                tvNamaSayurCart.setText(cartItem?.productId.toString())
            }
        }
    }

    override fun getItemCount(): Int {
        return cartList.size
    }
    private fun updateSelectedItemInDatabase(cartItem: CartItemsItem) {
        val viewModelFactory = ViewModelFactory(preferences)
        viewModelFactory.setApplication(application)

        itemCartViewModel.updateCart
        itemCartViewModel.updateCart.observe(context){
            cartRecyclerViewInitialize(cartItem = it?.result?.cartItems as List<CartItemsItem?>)
        }

    }
}