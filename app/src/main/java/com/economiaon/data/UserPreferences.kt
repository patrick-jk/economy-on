package com.economiaon.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences(val context: Context) {
    private val Context._dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")
    val userId: Flow<Long?>
    get() = context._dataStore.data.map { prefs ->
        prefs[USER_ID_KEY]
    }

    suspend fun saveUserId(userId: Long) {
        context._dataStore.edit { prefs ->
            prefs[USER_ID_KEY] = userId
        }
    }

    companion object {
        private val USER_ID_KEY = longPreferencesKey("user_id")
    }
}