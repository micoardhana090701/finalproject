package com.packagesayur.yursayur.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class ProfileResponse(
	val result: ResultProfile? = null,
	val meta: MetaProfile? = null
) : Parcelable

@Parcelize
data class MetaProfile(
	val code: Int? = null,
	val message: String? = null,
	val status: String? = null
) : Parcelable

@Parcelize
data class UserProfile(
	val profilePhotoUrl: String? = null,
	val address: String? = null,
	val role: String? = null,
	val disabledAt: String? = null,
	val createdAt: String? = null,
	val emailVerifiedAt: String? = null,
	val currentTeamId: String? = null,
	val avatar: String? = null,
	val updatedAt: String? = null,
	val phone: String? = null,
	val name: String? = null,
	val id: Int? = null,
	val profilePhotoPath: String? = null,
	val twoFactorConfirmedAt: String? = null,
	val email: String? = null
) : Parcelable

@Parcelize
data class ResultProfile(
	val user: UserProfile? = null
) : Parcelable
