package com.packagesayur.yursayur.user

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.packagesayur.yursayur.api.ApiConfig
import com.packagesayur.yursayur.etc.Resource
import com.packagesayur.yursayur.user.login.LoginRequest
import com.packagesayur.yursayur.user.login.LoginResponse
import com.packagesayur.yursayur.user.register.RegisterRequest
import com.packagesayur.yursayur.user.register.RegisterResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthViewModel (private val preferences: UserPreferences): ViewModel(){

    private val _authorizationInfo = MutableLiveData<Resource<String>>()
    val authorizationInfo: LiveData<Resource<String>>  = _authorizationInfo

    fun loginUser(email: String, password: String){
        _authorizationInfo.postValue(Resource.Loading())
        ApiConfig.apiInstance.login(LoginRequest(email, password)).enqueue(object : Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful){
                    val loginResult = response.body()?.result?.accessToken
                    loginResult?.let {
                        saveUserKey(it)
                    }
                    _authorizationInfo.postValue(Resource.Success(loginResult))
                } else{
                    val errorResponse = Gson().fromJson(
                        response.errorBody()?.charStream(),
                        LoginResponse::class.java
                    )
                    _authorizationInfo.postValue(Resource.Error(errorResponse.meta?.message.toString()))
                }

            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _authorizationInfo.postValue(Resource.Error(t.message))
            }
        })

    }

    fun registerUser(name: String, email: String, password: String){
        _authorizationInfo.postValue(Resource.Loading())
        val client = ApiConfig.apiInstance.register(RegisterRequest(name, email, password))

        client.enqueue(object : Callback<RegisterResponse>{
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful){
                    val registerResponse = response.body()?.meta?.message
                    _authorizationInfo.postValue(Resource.Success(registerResponse))
                }else{
                    val errorResponse = Gson().fromJson(
                        response.errorBody()?.charStream(),
                        ErrorResponse::class.java
                    )
                    if (errorResponse != null){
                        val emailError = errorResponse.errors.email
                        if (emailError != null){
                            for (error in emailError){
                                Log.e("RegisterActivity", "Email Error: $error")
                                _authorizationInfo.postValue(Resource.Error(error))
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _authorizationInfo.postValue(Resource.Error(t.message))
            }
        })
    }


    fun getUserKey() = preferences.getUserKey().asLiveData()
    fun logout() = deleteUserKey()

    private fun saveUserKey(key: String) {
        viewModelScope.launch {
            preferences.saveUserKey(key)
        }
    }

    private fun deleteUserKey() {
        viewModelScope.launch {
            preferences.deleteUserKey()
        }
    }
}