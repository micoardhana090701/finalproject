package com.packagesayur.yursayur.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.packagesayur.yursayur.api.ApiConfig
import com.packagesayur.yursayur.etc.Resource
import com.packagesayur.yursayur.response.ProfileResponse
import com.packagesayur.yursayur.response.UpdateResponse
import com.packagesayur.yursayur.user.UserPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel(private val preferences: UserPreferences) : ViewModel(){
    private val _profileUser = MutableLiveData<Resource<ProfileResponse>>()
    val profileUser : LiveData<Resource<ProfileResponse>> = _profileUser

    private val _updateUser = MutableLiveData<UpdateResponse>()
    val updateUser : LiveData<UpdateResponse> = _updateUser

    fun getProfile(access_token: String){
        _profileUser.postValue(Resource.Loading())
        ApiConfig.apiInstance.getProfile(access_token).enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(
                call: Call<ProfileResponse>,
                response: Response<ProfileResponse>
            ) {
                if (response.isSuccessful){
                    val data = response.body()
                    if (data != null){
                        _profileUser.postValue(Resource.Success(data))
                    }
                }
                else{
                    val errorResponse = Gson().fromJson(
                        response.errorBody()?.charStream(),
                        ProfileResponse::class.java
                    )
                    _profileUser.postValue(Resource.Error(errorResponse.toString()))
                }
            }
            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                Log.d(ContentValues.TAG, "onFailure: ${t.message}")
            }
        })

    }
    suspend fun updateAll(
        image: MultipartBody.Part?,
        name : RequestBody,
        email : RequestBody,
        address: RequestBody,
        phone: RequestBody,
    ){
        val accessToken = "Bearer ${preferences.getUserKey().first()}"
        viewModelScope.launch {
            ApiConfig.apiInstance.updateAll(accessToken, avatar = image, name = name, email = email, address = address, phone = phone).let { response ->
                if (response.isSuccessful){
                    if (response.body() != null){
                        val result = response.body()
                        _updateUser.postValue(result!!)
                    }
                }
                else{
                    val errorResponse = Gson().fromJson(
                        response.errorBody()?.charStream(),
                        UpdateResponse::class.java
                    )
                    _updateUser.postValue(errorResponse)
                }
            }
        }
    }
    suspend fun updateData(
        name : RequestBody,
        address: RequestBody,
        phone: RequestBody,
    ){
        val accessToken = "Bearer ${preferences.getUserKey().first()}"
        viewModelScope.launch {
            ApiConfig.apiInstance.updateData(accessToken, name = name, address = address, phone = phone).let { response ->
                if (response.isSuccessful){
                    if (response.body() != null){
                        val result = response.body()
                        _updateUser.postValue(result!!)
                    }
                }
                else{
                    val errorResponse = Gson().fromJson(
                        response.errorBody()?.charStream(),
                        UpdateResponse::class.java
                    )
                    _updateUser.postValue(errorResponse)
                }
            }
        }
    }
}