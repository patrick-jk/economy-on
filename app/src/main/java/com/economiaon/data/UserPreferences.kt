package com.economiaon.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserPreferences(val context: Context) {
    private val appContext = context.applicationContext
    val userId: Flow<Long?>
    get() = appContext.dataStore.data.map { prefs ->
        prefs[USER_ID_KEY]
    }
    val isUserLogged: Flow<Boolean?>
    get() = appContext.dataStore.data.map { prefs ->
        prefs[IS_USER_LOGGED]
    }

    suspend fun saveUserId(userId: Long) {
        appContext.dataStore.edit { prefs ->
            prefs[USER_ID_KEY] = userId
        }
    }

    suspend fun saveIsUserLogged(loggedUser: Boolean) {
        appContext.dataStore.edit { prefs ->
            prefs[IS_USER_LOGGED] = loggedUser
        }
    }

    companion object {
        private val USER_ID_KEY = longPreferencesKey("user_id")
        private val IS_USER_LOGGED = booleanPreferencesKey("user_logged")
    }
}