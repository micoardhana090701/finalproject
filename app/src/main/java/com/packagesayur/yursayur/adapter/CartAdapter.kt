package com.packagesayur.yursayur.adapter

import android.text.Layout
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.packagesayur.yursayur.databinding.ItemCartBinding
import com.packagesayur.yursayur.response.AddCartItem
import com.packagesayur.yursayur.user.UserPreferences

class CartAdapter(private val cartList: List<AddCartItem>): RecyclerView.Adapter<CartAdapter.ViewHolder>(){

    inner class ViewHolder(private val binding: ItemCartBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(cartItem: AddCartItem){
            with(binding){
                tvNamaSayurCart.setText(cartItem.productId.toString())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapter.ViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartAdapter.ViewHolder, position: Int) {
        val cartItem = cartList[position]
        holder.bind(cartItem)
    }

    override fun getItemCount(): Int {
        return cartList.size
    }


}