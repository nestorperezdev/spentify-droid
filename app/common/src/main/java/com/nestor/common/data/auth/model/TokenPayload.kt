package com.nestor.common.data.auth.model

internal fun String.toUserClaim(): UserClaims {
    return when (this) {
        "USER" -> UserClaims.USER
        else -> UserClaims.UNKNOWN
    }
}

enum class UserClaims {
    USER,
    UNKNOWN
}

data class TokenPayload(val sub: String, val username: String, val claims: List<UserClaims>)