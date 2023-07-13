package com.packagesayur.yursayur.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class CartResponse(
	@field:SerializedName("result")
	val result: ResultCart? = null,
	@field:SerializedName("meta")
	val meta: MetaCart? = null
) : Parcelable

@Parcelize
data class ResultCart(
	@field:SerializedName("cart_items")
	val cartItems: List<CartItemsItem?>? = null,
) : Parcelable

@Parcelize
data class MetaCart(
	@field:SerializedName("code")
	val code: Int? = null,
	@field:SerializedName("message")
	val message: String? = null,
	@field:SerializedName("status")
	val status: String? = null
) : Parcelable

@Parcelize
data class CartItemsItem(
	@field:SerializedName("quantity")
	val quantity: Int,
	@field:SerializedName("updated_at")
	val updatedAt: String,
	@field:SerializedName("user_id")
	val userId: Int,
	@field:SerializedName("is_selected")
	var isSelected: Int,
	@field:SerializedName("product_id")
	val productId: Int,
	@field:SerializedName("created_at")
	val createdAt: String,
	@field:SerializedName("id")
	val id: Int
) : Parcelable
