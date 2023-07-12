package com.packagesayur.yursayur.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.recyclerview.widget.GridLayoutManager
import com.packagesayur.yursayur.R
import com.packagesayur.yursayur.ViewModelFactory
import com.packagesayur.yursayur.adapter.AllProductAdapter
import com.packagesayur.yursayur.adapter.ProductAdapter
import com.packagesayur.yursayur.databinding.ActivityProductBinding
import com.packagesayur.yursayur.etc.GridSpacingItemDecoration
import com.packagesayur.yursayur.response.DataItem
import com.packagesayur.yursayur.user.AuthViewModel
import com.packagesayur.yursayur.user.UserPreferences
import com.packagesayur.yursayur.viewmodel.ProductViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class ProductActivity : AppCompatActivity() {

    private var _binding: ActivityProductBinding? = null
    private val binding get() = _binding!!
    private lateinit var productViewModel: ProductViewModel
    private lateinit var allProductAdapter : AllProductAdapter
    private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "user_key")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        lifecycleScope.launch {
            productItem()
        }
        binding.btnBackProductList.setOnClickListener {
            finish()
        }
    }
    private suspend fun productItem(){

        binding.btnSearch.setOnClickListener {
            lifecycleScope.launch {
                performSearch()
            }
        }
        val pref = UserPreferences.getInstance(dataStore)
        val viewModelFactory = ViewModelFactory(pref)
        viewModelFactory.setApplication(application)
        val accessToken = pref.getUserKey().first()
        productViewModel = ViewModelProvider(this, viewModelFactory)[ProductViewModel::class.java]
        val searchQuery = binding.etSearchAllProduct.text.toString().trim()

        productViewModel.getSearch(accessToken = "Bearer $accessToken", searchQuery = searchQuery)
        productViewModel.getProduct.observe(this){
            productRecyclerViewInitialize(product = it.result?.data as List<DataItem>)
        }

    }

    private suspend fun performSearch() {
        val pref = UserPreferences.getInstance(dataStore)
        val viewModelFactory = ViewModelFactory(pref)
        viewModelFactory.setApplication(application)
        val accessToken = pref.getUserKey().first()
        productViewModel = ViewModelProvider(this, viewModelFactory)[ProductViewModel::class.java]

        val searchQuery = binding.etSearchAllProduct.text.toString().trim()
        productViewModel.getSearch(accessToken = "Bearer $accessToken", searchQuery = searchQuery)
        productViewModel.getProduct.observe(this){
            productRecyclerViewInitialize(product = it.result?.data as List<DataItem>)
        }
    }


    private fun productRecyclerViewInitialize(product: List<DataItem>) {
        allProductAdapter = AllProductAdapter(product)
        binding.rvProduct.apply {
            setHasFixedSize(true)
            val spacingInPixels = resources.getDimensionPixelSize(R.dimen.spacing)
            layoutManager = GridLayoutManager(this@ProductActivity, 3)
            addItemDecoration(GridSpacingItemDecoration(3, spacingInPixels, false))
            adapter = allProductAdapter
        }
    }
}