package com.packagesayur.yursayur.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.packagesayur.yursayur.api.ApiConfig
import com.packagesayur.yursayur.response.ProductResponse
import com.packagesayur.yursayur.user.UserPreferences
import kotlinx.coroutines.launch

class ProductViewModel (private val preferences: UserPreferences): ViewModel() {
    private val _getProduct = MutableLiveData<ProductResponse>()
    val getProduct: LiveData<ProductResponse> = _getProduct

    suspend fun getAllProduct(accessToken: String) {
        viewModelScope.launch {
            ApiConfig.apiInstance.getProduct(accessToken).let {
                if (it.isSuccessful) {
                    if (it.body() != null) {
                        val result = it.body()
                        _getProduct.postValue(result!!)
                    } else {
                        val errorResponse = Gson().fromJson(
                            it.errorBody()?.charStream(),
                            ProductResponse::class.java
                        )
                        _getProduct.postValue(errorResponse)
                    }
                }
            }
        }
    }
    suspend fun getSearch(accessToken: String, searchQuery: String) {
        viewModelScope.launch {
            ApiConfig.apiInstance.getProduct(access_token = accessToken, search = searchQuery).let {
                    if (it.isSuccessful) {
                        if (it.body() != null) {
                            val result = it.body()
                            _getProduct.postValue(result!!)
                        } else {
                            val errorResponse = Gson().fromJson(
                                it.errorBody()?.charStream(),
                                ProductResponse::class.java
                            )
                            _getProduct.postValue(errorResponse)
                        }
                    }
                }
        }
    }
}