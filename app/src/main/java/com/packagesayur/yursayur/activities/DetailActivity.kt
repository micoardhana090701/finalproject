package com.packagesayur.yursayur.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.text.InputFilter
import android.text.Spanned
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.packagesayur.yursayur.R
import com.packagesayur.yursayur.databinding.ActivityCartBinding
import com.packagesayur.yursayur.databinding.ActivityDetailBinding
import com.packagesayur.yursayur.response.DataItem
import java.util.Date


class DetailActivity : AppCompatActivity() {

    private var _binding: ActivityDetailBinding? = null
    private val binding get() =  _binding!!
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_key")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val product = intent.getParcelableExtra<DataItem>(EXTRA_DATA)
        setupView(product!!)
    }
    private fun setupView(product: DataItem){
        with(binding){
            tvNamaSayurDetail.setText(product.name)
            tvDeskripsiDetail.setText(product.description)
            tvNamaToko.text = product.store?.name
            tvHargaDetail.text = product.sellingPrice.toString()
            tvLokasi.text = product.store?.address
            val loadImage = product.productImages?.first()?.image
            if(loadImage != null){
                Glide.with(this@DetailActivity)
                    .load("https://yursayur.projects.my.id/storage/$loadImage")
                    .centerCrop()
                    .into(ivSayurDetail)
            } else{
                Glide.with(this@DetailActivity)
                    .load(R.drawable.ic_baseline_account_circle_24)
                    .centerCrop()
                    .into(ivSayurDetail)
            }
        }
    }
    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}