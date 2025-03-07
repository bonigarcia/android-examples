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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SettingsViewModel(private val dataStoreHelper: DataStoreHelper) : ViewModel() {

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> get() = _userName

    private val _darkMode = MutableStateFlow(false)
    val darkMode: StateFlow<Boolean> get() = _darkMode

    init {
        // Observe DataStore changes
        viewModelScope.launch {
            dataStoreHelper.userName.collectLatest { name ->
                _userName.value = name
            }
        }
        viewModelScope.launch {
            dataStoreHelper.darkMode.collectLatest { enabled ->
                _darkMode.value = enabled
            }
        }
    }

    fun saveUserName(name: String) {
        viewModelScope.launch {
            dataStoreHelper.saveUserName(name)
        }
    }

    fun saveDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            dataStoreHelper.saveDarkMode(enabled)
        }
    }
}