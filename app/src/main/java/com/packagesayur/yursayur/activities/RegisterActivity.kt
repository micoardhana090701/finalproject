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

import com.packagesayur.yursayur.user.AuthViewModel

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

        binding.btnRegisterR.setOnClickListener {
            val email = binding.etEmailR.text.toString()
            val nama = binding.etNamaR.text.toString()
            val password = binding.etPasswordR.text.toString()
            val repassword = binding.etPasswordRType.text.toString()
            if (canRegister()){
                authViewModel.registerUser(name = nama, email = email, password = password)
            } else{
                authViewModel.authorizationInfo.observe(this){
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

