package com.packagesayur.yursayur.api

import com.packagesayur.yursayur.user.login.LoginRequest
import com.packagesayur.yursayur.user.login.LoginResponse
import com.packagesayur.yursayur.user.register.RegisterRequest
import com.packagesayur.yursayur.user.register.RegisterResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("auth/login")
    fun login(@Body requestBody: LoginRequest): Call<LoginResponse>

    @POST("auth/register")
    fun register(@Body requestBody: RegisterRequest): Call<RegisterResponse>
}