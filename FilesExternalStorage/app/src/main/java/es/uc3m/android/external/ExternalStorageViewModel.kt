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
package es.uc3m.android.external

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ExternalStorageViewModel(private val externalStorageHelper: ExternalStorageHelper) : ViewModel() {

    private val _fileContent = MutableStateFlow("")
    val fileContent: StateFlow<String> get() = _fileContent

    fun writeToExternalStorage(fileName: String, content: String) {
        viewModelScope.launch {
            val success = externalStorageHelper.writeToExternalStorage(fileName, content)
            if (success) {
                _fileContent.value = "File saved successfully!"
            } else {
                _fileContent.value = "Failed to save file."
            }
        }
    }

    fun readFromExternalStorage(fileName: String) {
        viewModelScope.launch {
            _fileContent.value = externalStorageHelper.readFromExternalStorage(fileName)
        }
    }
}