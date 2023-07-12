package com.packagesayur.yursayur.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.packagesayur.yursayur.R
import com.packagesayur.yursayur.activities.DetailActivity
import com.packagesayur.yursayur.databinding.SayurItemBigBinding
import com.packagesayur.yursayur.databinding.SayurItemBinding
import com.packagesayur.yursayur.databinding.SayurItemProductBinding
import com.packagesayur.yursayur.response.DataItem

class AllProductAdapter(private val productList: List<DataItem>): RecyclerView.Adapter<AllProductAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllProductAdapter.ViewHolder {
        val binding = SayurItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AllProductAdapter.ViewHolder, position: Int) {
        val product = productList[position]
        holder.bind(product)
    }

    inner class ViewHolder(private val binding: SayurItemProductBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(product: DataItem){
            with(binding){
                tvNamaSayur.setText(product.name)
                tvHarga.setText(product.sellingPrice.toString())
                tvUnit.setText(product.unit.toString())
                val loadImage = product.productImages?.first()?.image
                if (loadImage != null){
                    Glide.with(itemView.context)
                        .load("https://yursayur.projects.my.id/storage/$loadImage")
                        .centerCrop()
                        .into(ivSayur)
                } else{
                    Glide.with(itemView.context)
                        .load(R.drawable.img)
                        .centerCrop()
                        .into(ivSayur)
                }
                root.setOnClickListener {
                    val detailIntent = Intent(root.context, DetailActivity::class.java)
                    detailIntent.putExtra(DetailActivity.EXTRA_DATA, product)
                    root.context.startActivity(detailIntent)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }
}