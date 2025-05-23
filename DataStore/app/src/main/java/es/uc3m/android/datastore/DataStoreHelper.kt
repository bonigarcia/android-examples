/*
 * (C) Copyright 2025 Boni Garcia (https://bonigarcia.github.io/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package es.uc3m.android.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

const val DATASTORE_NAME = "settings"
val USER_NAME_KEY = stringPreferencesKey("user_name")
val IS_ENABLED_KEY = booleanPreferencesKey("enabled")

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATASTORE_NAME)

class DataStoreHelper(private val context: Context) {

    suspend fun saveUserName(name: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_NAME_KEY] = name
        }
    }

    val userName: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[USER_NAME_KEY] ?: ""
        }

    suspend fun saveEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_ENABLED_KEY] = enabled
        }
    }

    val enabled: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[IS_ENABLED_KEY] ?: false
        }
}
