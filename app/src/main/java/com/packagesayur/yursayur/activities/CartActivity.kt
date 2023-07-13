package com.packagesayur.yursayur.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Context
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.packagesayur.yursayur.ViewModelFactory
import com.packagesayur.yursayur.adapter.CartAdapter
import com.packagesayur.yursayur.api.ApiConfig
import com.packagesayur.yursayur.databinding.ActivityCartBinding
import com.packagesayur.yursayur.response.CartItemsItem
import com.packagesayur.yursayur.user.UserPreferences
import com.packagesayur.yursayur.viewmodel.CartViewModel
import com.packagesayur.yursayur.viewmodel.ItemCartViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CartActivity : AppCompatActivity() {

    private var _binding: ActivityCartBinding? = null
    private val binding get() = _binding!!
    private lateinit var cartAdapter: CartAdapter
    private lateinit var cartViewModel: CartViewModel
    private lateinit var itemCartViewModel: ItemCartViewModel

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
        cartViewModel = ViewModelProvider(this, viewModelFactory)[CartViewModel::class.java]
        cartViewModel.getAllCart()
        cartViewModel.getAllCart.observe(this){
            cartRecyclerViewInitialize(cartItem = it?.result?.cartItems as List<CartItemsItem?>)
        }
    }

    private fun cartRecyclerViewInitialize(cartItem: List<CartItemsItem?>) {
        val pref = UserPreferences.getInstance(dataStore)

        cartAdapter = CartAdapter(cartItem, application, pref, itemCartViewModel, this)
        if (cartItem == null){
            Toast.makeText(this, "tidak ada data tersimpan", Toast.LENGTH_SHORT).show()
        } else{
            binding.rvCart.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(this@CartActivity, LinearLayoutManager.VERTICAL, false)
                adapter = cartAdapter
            }
        }
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                return makeMovementFlags(0, ItemTouchHelper.RIGHT)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val viewModelFactory = ViewModelFactory(pref)
                viewModelFactory.setApplication(application)
                CoroutineScope(Dispatchers.IO).launch {
                    val accessToken = pref.getUserKey().first()
                    ApiConfig.apiInstance.deleteCartItem(access_token = "Bearer $accessToken", id = cartItem[viewHolder.adapterPosition]?.id!!.toInt())
                }
            }

            override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                super.onSelectedChanged(viewHolder, actionState)
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
                    viewHolder?.itemView?.alpha = 0.5f
                }
            }

        })
        itemTouchHelper.attachToRecyclerView(binding.rvCart)
    }
}