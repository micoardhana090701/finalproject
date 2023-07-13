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
import com.packagesayur.yursayur.ViewModelFactory
import com.packagesayur.yursayur.databinding.ActivityLoginBinding
import com.packagesayur.yursayur.etc.Resource
import com.packagesayur.yursayur.user.AuthViewModel
import com.packagesayur.yursayur.user.UserPreferences

class LoginActivity : AppCompatActivity() {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_key")
    lateinit var binding : ActivityLoginBinding
    private var mShouldFinish = false
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.tvRegister.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        userLogin()

        binding.btnLogin.setOnClickListener{
            if (canLogin()){
                val email = binding.etEmail.text.toString()
                val password = binding.etPassword.text.toString()
                authViewModel.loginUser(email, password)
            } else{
                authViewModel.authorizationInfo.observe(this){
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun userLogin() {
        val pref = UserPreferences.getInstance(dataStore)
        authViewModel = ViewModelProvider(this, ViewModelFactory(pref))[AuthViewModel::class.java]
        authViewModel.authorizationInfo.observe(this){
            when(it){
                is Resource.Success ->{
                    startActivity(Intent(this, HomeActivity::class.java))
                    mShouldFinish = true
                }
                is Resource.Loading -> {}
                is Resource.Error ->{
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
    }
    override fun onStop() {
        super.onStop()
        if (mShouldFinish) {
            finish()
        }
    }
    private fun canLogin() =
        binding.etEmail.error == null && binding.etEmail.error == null &&
                !binding.etEmail.text.isNullOrEmpty() && !binding.etEmail.text.isNullOrEmpty()

}