package com.example.zapasovnik.model

import android.content.Context
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
    }

    suspend fun storeUser(userId: Int, username: String, email: String, jwtToken: String, jwtExpire: Int) {
        context.datastore.edit {
            it[USER_ID_KEY] = userId
            it[USERNAME_KEY] = username
            it[USER_EMAIL_KEY] = email
            it[JWT_TOKEN_KEY] = jwtToken
            it[JWT_EXPIRE_KEY] = jwtExpire

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
        it[USER_ID_KEY]!!
    }

    val jwtExpireFlow: Flow<Int> = context.datastore.data.map {
        it[JWT_EXPIRE_KEY]!!
    }
}