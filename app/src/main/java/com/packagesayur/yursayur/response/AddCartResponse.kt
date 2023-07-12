package com.packagesayur.yursayur.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class AddCartResponse(

	@field:SerializedName("result")
	val result: ResultAddCart? = null,

	@field:SerializedName("meta")
	val meta: MetaAddCart? = null
) : Parcelable

@Parcelize
data class AddCartItem(

	@field:SerializedName("quantity")
	val quantity: Int? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("is_selected")
	val isSelected: Int? = null,

	@field:SerializedName("product_id")
	val productId: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
) : Parcelable

@Parcelize
data class ResultAddCart(

	@field:SerializedName("cart_item")
	val cartItem: AddCartItem? = null
) : Parcelable

@Parcelize
data class MetaAddCart(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable
