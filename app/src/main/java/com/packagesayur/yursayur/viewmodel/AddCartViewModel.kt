package com.packagesayur.yursayur.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.common.api.Api
import com.google.gson.Gson
import com.packagesayur.yursayur.api.ApiConfig
import com.packagesayur.yursayur.response.AddCartResponse
import com.packagesayur.yursayur.response.StoreResponse
import com.packagesayur.yursayur.user.UserPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddCartViewModel(private val preferences: UserPreferences): ViewModel() {

    private val _addToCart = MutableLiveData<AddCartResponse>()
    val addToCart: LiveData<AddCartResponse> = _addToCart

    private val _getAllCart = MutableLiveData<AddCartResponse>()
    val getAllCart: LiveData<AddCartResponse> = _getAllCart

    suspend fun getAllCartList(){
        val accessToken = "Bearer ${preferences.getUserKey().first()}"
        ApiConfig.apiInstance.getAllCart(access_token=accessToken).let {
            if (it.isSuccessful){
                val data = it.body()
                if (data != null){
                    _addToCart.postValue(data!!)
                }
            } else{
                val errorResponse = Gson().fromJson(
                    it.errorBody()?.charStream(),
                    AddCartResponse::class.java
                )
                _addToCart.postValue(errorResponse)
            }
        }
    }
    suspend fun addToCart(
        productId: Int,
        quantity: Int
    ){
        viewModelScope.launch {
            val accessToken = "Bearer ${preferences.getUserKey().first()}"
            ApiConfig.apiInstance.addToCart(access_token = accessToken, productId = productId, quantity = quantity).let {
                if (it.isSuccessful){
                    val data = it.body()
                    if (data != null){
                        _addToCart.postValue(data!!)
                    }
                } else{
                    val errorResponse = Gson().fromJson(
                        it.errorBody()?.charStream(),
                        AddCartResponse::class.java
                    )
                    _addToCart.postValue(errorResponse)
                }
            }
        }
    }
}