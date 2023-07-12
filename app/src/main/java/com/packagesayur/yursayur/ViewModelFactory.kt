package com.packagesayur.yursayur

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.packagesayur.yursayur.user.AuthViewModel
import com.packagesayur.yursayur.user.UserPreferences
import com.packagesayur.yursayur.viewmodel.AddCartViewModel
import com.packagesayur.yursayur.viewmodel.ProductViewModel
import com.packagesayur.yursayur.viewmodel.ProfileViewModel
import com.packagesayur.yursayur.viewmodel.AddStoreViewModel

class ViewModelFactory(private val pref: UserPreferences): ViewModelProvider.NewInstanceFactory() {
    private lateinit var _Application: Application

    fun setApplication(application: Application){
        _Application = application
    }

    @Suppress("UNCHECKED_CAST")
    override fun<T: ViewModel> create(modelClass: Class<T>): T{
        return when(modelClass){
            AuthViewModel::class.java -> AuthViewModel(pref) as T
            ProfileViewModel::class.java -> ProfileViewModel(pref) as T
            ProductViewModel::class.java -> ProductViewModel(pref) as T
            AddStoreViewModel::class.java -> AddStoreViewModel(pref) as T
            AddCartViewModel::class.java -> AddCartViewModel(pref) as T
            else -> throw  IllegalAccessException("Unknown ViewModel class: "+ modelClass.name)
        }
    }
}