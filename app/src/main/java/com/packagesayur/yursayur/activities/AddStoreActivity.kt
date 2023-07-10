package com.packagesayur.yursayur.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.packagesayur.yursayur.R
import com.packagesayur.yursayur.databinding.ActivityAddStoreBinding

class AddStoreActivity : AppCompatActivity() {
    private var _binding: ActivityAddStoreBinding? = null
    private val binding get() = _binding!!
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_key")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddStoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}