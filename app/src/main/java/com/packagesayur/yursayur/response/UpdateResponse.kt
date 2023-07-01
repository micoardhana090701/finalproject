package com.packagesayur.yursayur.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class UpdateResponse(

	@field:SerializedName("result")
	val result: ResultUpdateUser? = null,

	@field:SerializedName("meta")
	val meta: MetaUpdateUser? = null
) : Parcelable

@Parcelize
data class UserUpdate(

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
) : Parcelable

@Parcelize
data class MetaUpdateUser(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable

@Parcelize
data class ResultUpdateUser(

	@field:SerializedName("user")
	val user: UserUpdate? = null
) : Parcelable
