package com.packagesayur.yursayur.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.packagesayur.yursayur.api.ApiConfig
import com.packagesayur.yursayur.response.AddCartResponse
import com.packagesayur.yursayur.response.CartResponse
import com.packagesayur.yursayur.user.UserPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CartViewModel(private val preferences: UserPreferences): ViewModel() {

    private val _getAllCart = MutableLiveData<CartResponse?>()
    val getAllCart : LiveData<CartResponse?> = _getAllCart

    suspend fun getAllCart(){
        viewModelScope.launch {
            val accessToken = "Bearer ${preferences.getUserKey().first()}"
            ApiConfig.apiInstance.getAllCart(accessToken).let {
                if (it.isSuccessful){
                    val data = it.body()
                    if (data!= null){
                        _getAllCart.postValue(data)
                    }
                } else{
                    val errorResponse = Gson().fromJson(
                        it.errorBody()?.charStream(),
                        CartResponse::class.java
                    )
                    _getAllCart.postValue(errorResponse)
                }
            }
        }
    }
}