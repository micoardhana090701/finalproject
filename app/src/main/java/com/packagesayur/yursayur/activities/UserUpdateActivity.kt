package com.packagesayur.yursayur.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.packagesayur.yursayur.R
import com.packagesayur.yursayur.databinding.ActivityUserUpdateBinding

class UserUpdateActivity : AppCompatActivity() {

    private var _binding : ActivityUserUpdateBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityUserUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}