package site.sayaz.ofindex.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import site.sayaz.ofindex.data.remote.ApiService
import site.sayaz.ofindex.data.remote.request.LoginRequest
import site.sayaz.ofindex.data.remote.request.RegisterRequest


class AuthRepository(
    private val apiService: ApiService,
    private val context: Context
) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

    companion object {
        val TOKEN_KEY = stringPreferencesKey("token")
    }

    suspend fun login(loginRequest: LoginRequest): Result<String> {
        return try {
            val response = apiService.login(loginRequest)
            val token = response.body()?.token

            if (response.code() == 200) {
                if (token != null) {
                    Result.success(token)
                } else {
                    Result.failure(Exception("Token is null"))
                }
            } else {
                Result.failure(Exception("Login failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(registerRequest: RegisterRequest): Result<String> {
        return try {
            val response = apiService.register(registerRequest)
            val userId = response.body()?.id
            if (response.code() == 200) {
                if (userId != null) {
                    Result.success(userId)
                } else {
                    Result.failure(Exception("UserId is null"))
                }
            } else {
                Result.failure(Exception("Register failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun storeToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    fun getToken(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[TOKEN_KEY]
        }
    }
}