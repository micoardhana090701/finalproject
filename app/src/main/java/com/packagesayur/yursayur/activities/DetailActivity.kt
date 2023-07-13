package com.packagesayur.yursayur.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.packagesayur.yursayur.R
import com.packagesayur.yursayur.ViewModelFactory
import com.packagesayur.yursayur.databinding.ActivityDetailBinding
import com.packagesayur.yursayur.response.DataItem
import com.packagesayur.yursayur.user.UserPreferences
import com.packagesayur.yursayur.viewmodel.AddCartViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class DetailActivity : AppCompatActivity() {

    private var _binding: ActivityDetailBinding? = null
    private val binding get() =  _binding!!
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_key")
    private lateinit var addCartViewModel: AddCartViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val product = intent.getParcelableExtra<DataItem>(EXTRA_DATA)
        setupView(product!!)
        var jumlah = binding.tvItemCount.text.toString().toInt()

        binding.btnTambahKeranjang.setOnClickListener{
            CoroutineScope(Dispatchers.IO).launch {
                addToCart(product!!)
            }
            Toast.makeText(this, "Barang telah ditambahkan", Toast.LENGTH_SHORT).show()
            val sellingPrice = product.sellingPrice
            binding.tvTotalHarga.setText("Rp.$sellingPrice")
            jumlah = 1
            binding.tvItemCount.setText(jumlah.toString())
        }
        binding.btnIncreament.setOnClickListener {
            jumlah++
            val total = jumlah*product.sellingPrice!!
            total.toString()
            binding.tvTotalHarga.setText("Rp."+total)
            binding.tvItemCount.setText(jumlah.toString())

        }
        binding.btnDecreament.setOnClickListener {
            if (jumlah > 1){
                jumlah--
                val total = jumlah*product.sellingPrice!!
                total.toString()
                binding.tvTotalHarga.setText("Rp."+total)
                binding.tvItemCount.setText(jumlah.toString())
            }
        }


    }
    private fun setupView(product: DataItem){
        val pref = UserPreferences.getInstance(dataStore)
        val viewModelFactory = ViewModelFactory(pref)
        viewModelFactory.setApplication(application)
        addCartViewModel = ViewModelProvider(this, viewModelFactory)[AddCartViewModel::class.java]
        with(binding){
            tvNamaSayurDetail.setText(product.name)
            tvDeskripsiDetail.setText(product.description)
            tvNamaToko.text = product.store?.name
            tvHargaDetail.text = product.sellingPrice.toString()
            val totalHarga = product.sellingPrice.toString()
            tvTotalHarga.text = "Rp.$totalHarga"
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
        binding.btnBackToHome.setOnClickListener {
            finish()
        }

    }
    private suspend fun addToCart(cartItem: DataItem){
        val productId = cartItem.id
        val quantity = binding.tvItemCount.text.toString().toInt()
        productId?.let { addCartViewModel.addToCart(quantity = quantity, productId = it) }
    }
    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}