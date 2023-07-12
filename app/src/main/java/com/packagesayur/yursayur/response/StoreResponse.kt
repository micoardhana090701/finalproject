package com.packagesayur.yursayur.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class StoreResponse(

	@field:SerializedName("result")
	val result: ResultStore? = null,

	@field:SerializedName("meta")
	val meta: MetaStore? = null
): Parcelable

@Parcelize
data class StoreInfo(

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("disabled_at")
	val disabledAt: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("logo")
	val logo: String? = null,

	@field:SerializedName("banner")
	val banner: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("deleted_at")
	val deletedAt: String? = null,

	@field:SerializedName("user")
	val user: UserStore? = null
): Parcelable

@Parcelize
data class UserStore(

	@field:SerializedName("profile_photo_url")
	val profilePhotoUrl: String? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("role")
	val role: String? = null,

	@field:SerializedName("disabled_at")
	val disabledAt: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("email_verified_at")
	val emailVerifiedAt: String? = null,

	@field:SerializedName("current_team_id")
	val currentTeamId: String? = null,

	@field:SerializedName("avatar")
	val avatar: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("profile_photo_path")
	val profilePhotoPath: String? = null,

	@field:SerializedName("two_factor_confirmed_at")
	val twoFactorConfirmedAt: String? = null,

	@field:SerializedName("email")
	val email: String? = null
): Parcelable

@Parcelize
data class ResultStore(

	@field:SerializedName("store")
	val store: StoreInfo? = null
): Parcelable

@Parcelize
data class MetaStore(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
): Parcelable
