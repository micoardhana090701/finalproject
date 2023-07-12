package com.packagesayur.yursayur.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.packagesayur.yursayur.R
import com.packagesayur.yursayur.ViewModelFactory
import com.packagesayur.yursayur.adapter.AllProductAdapter
import com.packagesayur.yursayur.adapter.CartAdapter
import com.packagesayur.yursayur.databinding.ActivityCartBinding
import com.packagesayur.yursayur.etc.GridSpacingItemDecoration
import com.packagesayur.yursayur.response.AddCartItem
import com.packagesayur.yursayur.response.AddCartResponse
import com.packagesayur.yursayur.response.DataItem
import com.packagesayur.yursayur.user.UserPreferences
import com.packagesayur.yursayur.viewmodel.AddCartViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CartActivity : AppCompatActivity() {

    private var _binding: ActivityCartBinding? = null
    private val binding get() = _binding!!
    private lateinit var cartAdapter: CartAdapter
    private lateinit var addCartViewModel: AddCartViewModel
    private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "user_key")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lifecycleScope.launch {
            getAllCart()
        }
    }
    private suspend fun getAllCart(){
        val pref = UserPreferences.getInstance(dataStore)
        val viewModelFactory = ViewModelFactory(pref)
        viewModelFactory.setApplication(application)
        addCartViewModel = ViewModelProvider(this, viewModelFactory)[AddCartViewModel::class.java]
        addCartViewModel.getAllCartList()
        addCartViewModel.getAllCart.observe(this){
            cartRecyclerViewInitialize(it.result?.cartItem as List<AddCartItem>)
        }
    }

    private fun cartRecyclerViewInitialize(cartItem: List<AddCartItem>) {
        cartAdapter = CartAdapter(cartItem)
        binding.rvCart.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@CartActivity, LinearLayoutManager.VERTICAL, false)
            adapter = cartAdapter
        }
    }
}