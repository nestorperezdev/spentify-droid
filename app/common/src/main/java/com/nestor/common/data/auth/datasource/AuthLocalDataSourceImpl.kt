package com.nestor.common.data.auth.datasource

import android.content.SharedPreferences
import com.auth0.android.jwt.JWT
import com.nestor.common.data.auth.model.TokenPayload
import com.nestor.common.data.auth.model.toUserClaim
import com.nestor.database.data.encryptedpreferences.EncryptedPreferences
import com.nestor.database.data.user.UserDao
import com.nestor.database.data.user.UserEntity
import com.nestor.uikit.util.CoroutineContextProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Named

private const val TOKEN_KEY = "TOKEN_KEY"

class AuthLocalDataSourceImpl @Inject constructor(
    @Named(EncryptedPreferences.ENCRYPTED_PREFERENCES) private val encryptedPreferences: SharedPreferences,
    private val userDao: UserDao,
    private val contextProvider: CoroutineContextProvider
) : AuthLocalDataSource {
    private val _coroutineScope = CoroutineScope(contextProvider.io() + SupervisorJob())
    private val _rawToken = MutableStateFlow(this.retrieveToken())
    private val _tokenPayload = _rawToken.transform { emit(it.decryptToken()) }

    override suspend fun storeUser(userEntity: UserEntity) {
        userDao.insertUser(userEntity)
    }

    override fun tokenContents(): SharedFlow<TokenPayload?> = _tokenPayload.shareIn(
        _coroutineScope,
        SharingStarted.Lazily,
        1
    )

    override fun userDetails(): SharedFlow<UserEntity?> =
        userDao.getUser().shareIn(
            _coroutineScope,
            SharingStarted.Lazily,
            1
        )

    override fun rawToken(): String? {
        return _rawToken.value
    }

    override suspend fun clearToken() {
        userDao.clearUsers()
        encryptedPreferences.edit().remove(TOKEN_KEY).apply()
        _rawToken.update { null }
    }

    override suspend fun clearUsers() {
        userDao.clearUsers()
    }

    override suspend fun updateUserCurrency(currencyCode: String) {
        userDao.updateCurrency(currencyCode)
    }

    override suspend fun storeToken(token: String) {
        encryptedPreferences.edit().putString(TOKEN_KEY, token).apply()
        _rawToken.update { token }
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
}
