package com.example.zapasovnik.model

import android.content.Context
import androidx.datastore.preferences.core.edit
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
        val USERNAME_KEY = stringPreferencesKey("USERNAME")
        val USER_EMAIL_KEY = stringPreferencesKey("USER_EMAIL")
        val LOGGED_IN_KEY = stringPreferencesKey("LOGGED_IN_STATUS")
    }

    suspend fun storeUser(username: String, email: String, loggedIn: String) {
        context.datastore.edit {
            it[USERNAME_KEY] = username
            it[USER_EMAIL_KEY] = email
            it[LOGGED_IN_KEY] = loggedIn
        }
    }

    val usernameFlow: Flow<String> = context.datastore.data.map {
        it[USERNAME_KEY] ?: ""
    }

    val emailFlow: Flow<String> = context.datastore.data.map {
        it[USER_EMAIL_KEY] ?: ""
    }

    val loggedInFlow: Flow<String> = context.datastore.data.map {
        it[LOGGED_IN_KEY] ?: ""
    }
}