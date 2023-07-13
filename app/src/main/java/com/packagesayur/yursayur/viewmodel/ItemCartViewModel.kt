package com.packagesayur.yursayur.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.packagesayur.yursayur.api.ApiConfig
import com.packagesayur.yursayur.response.CartResponse
import com.packagesayur.yursayur.response.ItemCartResponse
import com.packagesayur.yursayur.user.UserPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.RequestBody

class ItemCartViewModel(private val preferences: UserPreferences): ViewModel() {

    private val _updateCart = MutableLiveData<ItemCartResponse>()
    val updateCart : LiveData<ItemCartResponse> = _updateCart

    suspend fun updateCartItem(id: Int, quantity: RequestBody, isSelected: RequestBody){
        viewModelScope.launch {
            val accessToken = "Bearer ${preferences.getUserKey().first()}"
            ApiConfig.apiInstance.updateCartItem(accessToken, id = id, quantity = quantity, is_selected = isSelected).let {
                if (it.isSuccessful){
                    val data = it.body()
                    if (data!= null){
                        _updateCart.postValue(data!!)
                    }
                } else{
                    val errorResponse = Gson().fromJson(
                        it.errorBody()?.charStream(),
                        ItemCartResponse::class.java
                    )
                    _updateCart.postValue(errorResponse)
                }
            }
        }
    }
}