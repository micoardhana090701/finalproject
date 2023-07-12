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
import com.packagesayur.yursayur.etc.Resource
import com.packagesayur.yursayur.response.StoreResponse
import com.packagesayur.yursayur.user.UserPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddStoreViewModel(private val preferences: UserPreferences): ViewModel() {

    private val _uploadStore = MutableLiveData<StoreResponse>()
    val upload: LiveData<StoreResponse> = _uploadStore

    private val _getUserStore = MutableLiveData<StoreResponse>()
    val getUserStore : LiveData<StoreResponse> = _getUserStore

    suspend fun getUserStore(accessToken: String, userId: Int){
        ApiConfig.apiInstance.getStore(accessToken = accessToken, userId = userId).enqueue(object : Callback<StoreResponse> {
            override fun onResponse(
                call: Call<StoreResponse>,
                response: Response<StoreResponse>
            ) {
                if (response.isSuccessful){
                    val data = response.body()
                    if (data != null){
                        _getUserStore.postValue(data!!)
                    }
                }
                else{
                    val errorResponse = Gson().fromJson(
                        response.errorBody()?.charStream(),
                        StoreResponse::class.java
                    )
                    _getUserStore.postValue(errorResponse)
                }
            }
            override fun onFailure(call: Call<StoreResponse>, t: Throwable) {
                Log.d(ContentValues.TAG, "onFailure: ${t.message}")
            }
        })
    }

    suspend fun addStore(
        logo: MultipartBody.Part?,
        banner: MultipartBody.Part?,
        storeName: RequestBody,
        address: RequestBody,
        description: RequestBody,
    ){
        val accessToken = "Bearer ${preferences.getUserKey().first()}"
        viewModelScope.launch {
            ApiConfig.apiInstance.addStore(access_token = accessToken, logo = logo, banner = banner, storeName = storeName, address = address, description = description).let{
                if (it.isSuccessful){
                    val data = it.body()
                    if (data != null){
                        _uploadStore.postValue(data!!)
                    }
                }else{
                    val errorResponse = Gson().fromJson(
                        it.errorBody()?.charStream(),
                        StoreResponse::class.java
                    )
                    _uploadStore.postValue(errorResponse)
                }
            }
        }
    }

}