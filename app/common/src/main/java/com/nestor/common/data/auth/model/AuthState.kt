package com.nestor.common.data.auth.model

import com.nestor.database.data.user.UserEntity

sealed class AuthState {
    class Authenticated(val details: UserEntity): AuthState()
    object Anonymous: AuthState()
}

