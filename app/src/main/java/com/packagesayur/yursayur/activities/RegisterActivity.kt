package com.packagesayur.yursayur.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.packagesayur.yursayur.ViewModelFactory
import com.packagesayur.yursayur.databinding.ActivityRegisterBinding
import com.packagesayur.yursayur.etc.Resource

import com.packagesayur.yursayur.user.AuthViewModel
import com.packagesayur.yursayur.user.UserPreferences

class RegisterActivity : AppCompatActivity() {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_key")
    private var _binding: ActivityRegisterBinding? = null
    private val binding get() = _binding!!
    private var mShouldFinish = false
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.tvLogin.setOnClickListener{
            finish()
        }
        setupViewModel()
        binding.btnRegisterR.setOnClickListener {
            val email = binding.etEmailR.text.toString()
            val nama = binding.etNamaR.text.toString()
            val password = binding.etPasswordR.text.toString()
            val repassword = binding.etPasswordRType.text.toString()
            if (canRegister()){
                if (password.length < 8 && password.isEmpty()){
                    Toast.makeText(this, "Password Minimal 8 Karakter", Toast.LENGTH_SHORT).show()
                }
                if (password != repassword){
                    Toast.makeText(this, "Password tidak cocok", Toast.LENGTH_SHORT).show()
                }
                else{
                    authViewModel.registerUser(name = nama, email = email, password = password)
                }
            }
        }
    }
    private fun setupViewModel() {
        val pref = UserPreferences.getInstance(dataStore)
        authViewModel = ViewModelProvider(this, ViewModelFactory(pref))[AuthViewModel::class.java]
        authViewModel.authorizationInfo.observe(this){
            when(it){
                is Resource.Success -> {
                    Toast.makeText(this, "akun berhasil di buat", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finishAffinity()
                }
                is Resource.Loading -> {}
                is Resource.Error -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    override fun onStop() {
        super.onStop()
        if (mShouldFinish) {
            finish()
        }
    }

    private fun canRegister() =
        binding.etEmailR.error == null && binding.etPasswordR.error == null && binding.etPasswordRType.error == null && binding.etNamaR.error == null

}

