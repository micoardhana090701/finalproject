package com.packagesayur.yursayur.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.packagesayur.yursayur.api.ApiConfig
import com.packagesayur.yursayur.response.ProductResponse
import com.packagesayur.yursayur.user.UserPreferences
import kotlinx.coroutines.launch

class ProductViewModel (private val preferences: UserPreferences): ViewModel(){
    private val _getProduct = MutableLiveData<ProductResponse>()
    val getProduct : LiveData<ProductResponse> = _getProduct

    suspend fun getAllProduct(accessToken: String, searchQuery : String){
        viewModelScope.launch {
            ApiConfig.apiInstance.getProduct(accessToken, search = searchQuery, limit = 5).let{
                _getProduct.postValue(it)
            }
        }
    }
}