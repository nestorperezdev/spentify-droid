package com.nestor.auth.data.datasource

import android.content.SharedPreferences
import com.auth0.android.jwt.JWT
import com.nestor.auth.data.model.AuthState
import com.nestor.auth.data.model.TokenPayload
import com.nestor.auth.data.model.toUserClaim
import com.nestor.database.data.encryptedpreferences.EncryptedPreferences
import com.nestor.database.data.user.UserDao
import com.nestor.database.data.user.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Named

private const val TOKEN_KEY = "TOKEN_KEY"

class AuthLocalDataSourceImpl @Inject constructor(
    @Named(EncryptedPreferences.ENCRYPTED_PREFERENCES) private val encryptedPreferences: SharedPreferences,
    private val userDao: UserDao
) : AuthLocalDataSource {
    private val _authState = MutableStateFlow(this.getAuthStatus())
    private val _rawToken = MutableStateFlow(this.retrieveToken())
    private val _tokenPayload = _rawToken.transform { emit(it.decryptToken()) }
    override fun userDetailsFlow(): Flow<TokenPayload?> = _tokenPayload
    override fun userDetails(): TokenPayload? {
        return _rawToken.value?.decryptToken()
    }

    override fun isUserLoggedIn(): StateFlow<AuthState> = _authState
    override fun getRawToken(): String? {
        return _rawToken.value
    }

    override suspend fun storeUserToken(token: String, name: String, email: String) {
        encryptedPreferences.edit().putString(TOKEN_KEY, token).apply()
        _authState.update { AuthState.AUTHENTICATED }
        _rawToken.update { token }
    }

    override suspend fun getUserDetails(uuid: String): UserEntity? {
        return userDao.getUser(uuid)
    }

    override suspend fun storeUser(userEntity: UserEntity) {
        userDao.insertUser(userEntity)
    }

    override suspend fun clearUsers() {
        userDao.clearUsers()
    }

    private fun retrieveToken(): String? {
        return this.encryptedPreferences.getString(TOKEN_KEY, null)
    }

    private fun String?.decryptToken(): TokenPayload? {
        this ?: return null
        val jwt = JWT(this)
        val name = jwt.getClaim("username").asString()
        val sub = jwt.getClaim("sub").asString()
        val claims = jwt.getClaim("claims").asList(String::class.java)
        if (name == null || sub == null || claims.isNullOrEmpty()) return null
        return TokenPayload(sub, name, claims.map { it.toUserClaim() })
    }

    private fun getAuthStatus(): AuthState {
        return if (this.retrieveToken() != null) {
            AuthState.AUTHENTICATED
        } else {
            AuthState.ANONYMOUS
        }
    }
}
