package com.packagesayur.yursayur.user

import com.packagesayur.yursayur.user.register.MetaRegister

data class ErrorResponse(
    val meta: MetaRegister,
    val errors: ErrorData
)

data class ErrorData(
    val email: List<String>?
)