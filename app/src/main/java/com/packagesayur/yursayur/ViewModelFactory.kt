package com.packagesayur.yursayur

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.packagesayur.yursayur.user.AuthViewModel
import com.packagesayur.yursayur.user.UserPreferences

class ViewModelFactory(private val pref: UserPreferences): ViewModelProvider.NewInstanceFactory() {
    private lateinit var _Application: Application

    fun setApplication(application: Application){
        _Application = application
    }

    @Suppress("UNCHECKED_CAST")
    override fun<T: ViewModel> create(modelClass: Class<T>): T{
        return when(modelClass){
            AuthViewModel::class.java -> AuthViewModel(pref) as T
            else -> throw  IllegalAccessException("Unknown ViewModel class: "+ modelClass.name)
        }
    }
}