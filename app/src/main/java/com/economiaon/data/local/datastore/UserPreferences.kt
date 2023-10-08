package com.economiaon.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserPreferences(val context: Context) {
    val userId: Flow<String?>
    get() = context.dataStore.data.map { prefs ->
        prefs[USER_ID_KEY]
    }
    val isUserLogged: Flow<Boolean?>
    get() = context.dataStore.data.map { prefs ->
        prefs[IS_USER_LOGGED]
    }

    suspend fun saveUserId(userId: String) {
        context.dataStore.edit { prefs ->
            prefs[USER_ID_KEY] = userId
        }
    }

    suspend fun saveIsUserLogged(loggedUser: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[IS_USER_LOGGED] = loggedUser
        }
    }

    companion object {
        private val USER_ID_KEY = stringPreferencesKey("user_id")
        private val IS_USER_LOGGED = booleanPreferencesKey("user_logged")
    }
}