package com.packagesayur.yursayur.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.packagesayur.yursayur.ViewModelFactory
import com.packagesayur.yursayur.databinding.ActivityUserUpdateBinding
import com.packagesayur.yursayur.etc.Resource
import com.packagesayur.yursayur.user.UserPreferences
import com.packagesayur.yursayur.viewmodel.ProfileViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class UserUpdateActivity : AppCompatActivity() {

    private var _binding : ActivityUserUpdateBinding? = null
    private val binding get() = _binding!!
    private lateinit var profileViewModel : ProfileViewModel
    private var getFile: File? = null
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_key")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityUserUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)


        lifecycleScope.launch {
            setupView()
        }

        binding.btnUpdateProfile.setOnClickListener {
            lifecycleScope.launch {
                update()
            }
        }
    }
    private suspend fun setupView(){
        val pref = UserPreferences.getInstance(dataStore)
        val viewModelFactory = ViewModelFactory(pref)
        viewModelFactory.setApplication(application)

        profileViewModel = ViewModelProvider(this, viewModelFactory)[ProfileViewModel::class.java]

        val accessToken = pref.getUserKey().first()
        profileViewModel.getProfile(access_token = "Bearer $accessToken")
        profileViewModel.profileUser.observe(this){
            when(it){
                is Resource.Success -> {
                    if (it != null){
                        val userIdIntent = Intent(this, AddStoreActivity::class.java)
                        userIdIntent.putExtra(AddStoreActivity.EXTRA_USER_ID, it.data?.result?.user?.id)
                        binding.etNamaUpdate.setText(it.data?.result?.user?.name)
                        binding.etAlamatUpdate.setText(it.data?.result?.user?.address)
                        binding.etPhoneUpdate.setText(it.data?.result?.user?.phone)
                    }
                }
                is Resource.Loading -> {
                }
                is Resource.Error -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
                else -> {
                }
            }
        }
    }
    private suspend fun update() {
        val pref = UserPreferences.getInstance(dataStore)
        val viewModelFactory = ViewModelFactory(pref)
        viewModelFactory.setApplication(application)

        profileViewModel = ViewModelProvider(this, viewModelFactory)[ProfileViewModel::class.java]
        val nama = binding.etNamaUpdate.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val address = binding.etAlamatUpdate.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val phone = binding.etPhoneUpdate.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())

        profileViewModel.updateData(name = nama, address = address, phone = phone)
        Toast.makeText(this, "Akun Telah di Update", Toast.LENGTH_SHORT).show()
    }
}