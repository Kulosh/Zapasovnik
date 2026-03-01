package com.example.zapasovnik.model

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.datastore by preferencesDataStore(
    name = "user_data"
)

class UserData (
    private val context: Context
) {
    companion object {
        val USER_ID_KEY = intPreferencesKey("USER_ID")
        val USERNAME_KEY = stringPreferencesKey("USERNAME")
        val USER_EMAIL_KEY = stringPreferencesKey("USER_EMAIL")
        val JWT_TOKEN_KEY = stringPreferencesKey("JWT_TOKEN")
        val JWT_EXPIRE_KEY = intPreferencesKey("JWT_EXPIRE")
        val ADMIN_KEY = booleanPreferencesKey("ADMIN")
    }

    suspend fun storeUser(userId: Int, username: String, email: String, jwtToken: String, jwtExpire: Int, admin: Boolean) {
        context.datastore.edit {
            it[USER_ID_KEY] = userId
            it[USERNAME_KEY] = username
            it[USER_EMAIL_KEY] = email
            it[JWT_TOKEN_KEY] = jwtToken
            it[JWT_EXPIRE_KEY] = jwtExpire
            it[ADMIN_KEY] = admin
        }
    }

    val usernameFlow: Flow<String> = context.datastore.data.map {
        it[USERNAME_KEY] ?: ""
    }

    val emailFlow: Flow<String> = context.datastore.data.map {
        it[USER_EMAIL_KEY] ?: ""
    }

    val jwtTokenFlow: Flow<String> = context.datastore.data.map {
        it[JWT_TOKEN_KEY] ?: ""
    }

    val userIdFlow: Flow<Int> = context.datastore.data.map {
        it[USER_ID_KEY] ?: -1
    }

    val jwtExpireFlow: Flow<Int> = context.datastore.data.map {
        it[JWT_EXPIRE_KEY] ?: 0
    }

    val adminFlow: Flow<Boolean> = context.datastore.data.map {
        it[ADMIN_KEY] ?: false
    }
}