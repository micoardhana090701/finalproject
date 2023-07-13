package com.packagesayur.yursayur.api

import androidx.room.Update
import com.packagesayur.yursayur.etc.Resource
import com.packagesayur.yursayur.response.AddCartResponse
import com.packagesayur.yursayur.response.CartResponse
import com.packagesayur.yursayur.response.ItemCartResponse
import com.packagesayur.yursayur.response.ProductResponse
import com.packagesayur.yursayur.response.ProfileResponse
import com.packagesayur.yursayur.response.StoreResponse
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
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
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
        @Part ("name") name: RequestBody,
        @Part ("address") address: RequestBody,
        @Part ("phone") phone: RequestBody,
    ): Response<UpdateResponse>
    @Multipart
    @POST("profile")
    suspend fun updateData(
        @Header("Authorization") access_token: String,
        @Part ("name") name: RequestBody,
        @Part ("address") address: RequestBody,
        @Part ("phone") phone: RequestBody,
    ): Response<UpdateResponse>

    @GET("product")
    suspend fun getProduct(
        @Header("Authorization") access_token: String,
        @Query("search") search: String? = null,
        @Query("limit") limit: Int? = null,
    ) : Response<ProductResponse>
    @GET("store")
    suspend fun getStore(
        @Header("Authorization") accessToken: String,
        @Path("user_id") userId : Int
    ): Call<StoreResponse>
    @Multipart
    @POST("store")
    suspend fun addStore(
        @Header("Authorization") access_token: String,
        @Part banner: MultipartBody.Part?,
        @Part logo: MultipartBody.Part?,
        @Part("name") storeName: RequestBody,
        @Part("address") address: RequestBody,
        @Part("description") description: RequestBody
    ): Response<StoreResponse>

    @Multipart
    @POST("cart")
    suspend fun addToCart(
        @Header("Authorization") access_token: String,
        @Part ("product_id") productId: Int,
        @Part ("quantity") quantity: Int,
    ): Response<AddCartResponse>

    @GET("cart")
    suspend fun getAllCart(
        @Header("Authorization") access_token: String
    ): Response<CartResponse>

    @Multipart
    @POST("cart/{id}")
    suspend fun updateCartItem(
        @Header("Authorization") access_token: String,
        @Path ("id") id : Int,
        @Part ("quantity") quantity: RequestBody,
        @Part ("is_selected") is_selected: RequestBody,
    ): Response<ItemCartResponse>

    @DELETE("cart/{id}")
    suspend fun deleteCartItem(
        @Header("Authorization") access_token: String,
        @Path ("id") id : Int,
    ): Response<ItemCartResponse>
}