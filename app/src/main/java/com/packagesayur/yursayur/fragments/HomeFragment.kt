package com.packagesayur.yursayur.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.packagesayur.yursayur.R
import com.packagesayur.yursayur.ViewModelFactory
import com.packagesayur.yursayur.activities.ProductActivity
import com.packagesayur.yursayur.adapter.ProductAdapter
import com.packagesayur.yursayur.databinding.FragmentHomeBinding
import com.packagesayur.yursayur.etc.Resource
import com.packagesayur.yursayur.response.DataItem
import com.packagesayur.yursayur.user.AuthViewModel
import com.packagesayur.yursayur.user.UserPreferences
import com.packagesayur.yursayur.viewmodel.ProductViewModel
import com.packagesayur.yursayur.viewmodel.ProfileViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var authViewModel: AuthViewModel
    private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "user_key")
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var productAdapter: ProductAdapter
    private lateinit var productViewModel: ProductViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root : View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            setupProfileView(view)
            getProductItem()
        }
        binding.btnSelengkapnya.setOnClickListener {
            startActivity(Intent(requireContext(), ProductActivity::class.java))
        }
        binding.btnSelengkapnya.setOnClickListener {
            startActivity(Intent(requireContext(), ProductActivity::class.java))
        }
    }
    private suspend fun getProductItem(){
        val pref = UserPreferences.getInstance(requireContext().dataStore)
        val viewModelFactory = ViewModelFactory(pref)
        viewModelFactory.setApplication(requireActivity().application)
        val accessToken = pref.getUserKey().first()
        val searchQuery = "%%"
        productViewModel = ViewModelProvider(this, viewModelFactory)[ProductViewModel::class.java]
        productViewModel.getAllProduct(accessToken = "Bearer $accessToken", searchQuery = searchQuery)
        productViewModel.getProduct.observe(viewLifecycleOwner){
            productAdapterInitialize(product = it.result?.data as List<DataItem>)
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun productAdapterInitialize(product: List<DataItem>){
        productAdapter = ProductAdapter(product)
        binding.rvSayur.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = productAdapter
        }
        binding.rvBuah.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = productAdapter
        }

        productAdapter.notifyDataSetChanged()

    }

    private suspend fun setupProfileView(view: View){
        val pref = UserPreferences.getInstance(requireContext().dataStore)
        val viewModelFactory = ViewModelFactory(pref)
        viewModelFactory.setApplication(requireActivity().application)

        profileViewModel = ViewModelProvider(this, viewModelFactory)[ProfileViewModel::class.java]
        authViewModel = ViewModelProvider(this, ViewModelFactory(pref))[AuthViewModel::class.java]

        val accessToken = pref.getUserKey().first()
        profileViewModel.getProfile(access_token = "Bearer $accessToken")
        profileViewModel.profileUser.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success -> {
                    if (it != null){
                        val nama = it.data?.result?.user?.name
                        binding.tvnama.setText("Hallo,\n$nama")
                        val ivProfileHome = binding.ivFotoProfil
                        if (it.data?.result?.user?.avatar == null){
                            Glide.with(view.context)
                                .load(R.drawable.outline_account_circle_24)
                                .centerCrop()
                                .into(ivProfileHome)
                        }
                        else{
                            Glide.with(view.context)
                                .load("https://yursayur.projects.my.id/storage/" + it.data.result.user.avatar)
                                .centerCrop()
                                .into(ivProfileHome)
                        }
                    }
                    else{
                        val ivProfileHome = binding.ivFotoProfil
                        binding.tvnama.setText(it?.data?.result?.user?.name)
                        Glide.with(view.context)
                            .load(R.drawable.outline_account_circle_24)
                            .centerCrop()
                            .into(ivProfileHome)
                    }
                }
                is Resource.Loading -> {
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
                else -> {
                }
            }
        }
    }
}