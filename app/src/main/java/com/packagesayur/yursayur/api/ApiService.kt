package com.packagesayur.yursayur.api

import com.packagesayur.yursayur.etc.Resource
import com.packagesayur.yursayur.response.ProductResponse
import com.packagesayur.yursayur.response.ProfileResponse
import com.packagesayur.yursayur.response.UpdateResponse
import com.packagesayur.yursayur.user.login.LoginRequest
import com.packagesayur.yursayur.user.login.LoginResponse
import com.packagesayur.yursayur.user.register.RegisterRequest
import com.packagesayur.yursayur.user.register.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {
    @POST("auth/login")
    fun login(@Body requestBody: LoginRequest): Call<LoginResponse>

    @POST("auth/register")
    fun register(@Body requestBody: RegisterRequest): Call<RegisterResponse>

    @GET("profile")
    fun getProfile(
        @Header("Authorization") access_token: String,
    ): Call<ProfileResponse>
    @Multipart
    @POST("profile")
    suspend fun updateAll(
        @Header("Authorization") access_token: String,
        @Part avatar: MultipartBody.Part?,
        @Part ("email") email: RequestBody,
        @Part ("name") name: RequestBody
    ): Response<UpdateResponse>
    @GET("product")
    suspend fun getProduct(
        @Header("Authorization") access_token: String,
        @Query("search") search: String? = null,
        @Query("limit") limit: Int? = null,
    ) : ProductResponse
}