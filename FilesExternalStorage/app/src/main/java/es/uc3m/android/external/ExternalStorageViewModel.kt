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

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ExternalStorageViewModel(
    application: Application,
    private val externalStorageHelper: ExternalStorageHelper
) : AndroidViewModel(application) {

    private val _fileContent = MutableStateFlow("")
    val fileContent: StateFlow<String> = _fileContent.asStateFlow()

    // App-specific Storage
    fun writeToAppSpecific(fileName: String, content: String) {
        viewModelScope.launch {
            if (externalStorageHelper.writeToAppSpecificStorage(fileName, content)) {
                _fileContent.value = getApplication<Application>().getString(R.string.saved_app_specific)
            } else {
                _fileContent.value = getApplication<Application>().getString(R.string.error_app_specific)
            }
        }
    }

    fun readFromAppSpecific(fileName: String) {
        viewModelScope.launch {
            _fileContent.value = externalStorageHelper.readFromAppSpecificStorage(fileName)
        }
    }

    // MediaStore
    fun saveImageToMediaStore(bitmap: Bitmap, displayName: String) {
        viewModelScope.launch {
            val uri = externalStorageHelper.saveImageToMediaStore(bitmap, displayName)
            _fileContent.value = if (uri != null) {
                getApplication<Application>().getString(R.string.saved_mediastore, uri.toString())
            } else {
                getApplication<Application>().getString(R.string.error_mediastore)
            }
        }
    }

    // SAF (Storage Access Framework)
    fun writeToUri(uri: Uri, content: String) {
        viewModelScope.launch {
            if (externalStorageHelper.writeToUri(uri, content)) {
                _fileContent.value = getApplication<Application>().getString(R.string.saved_saf)
            } else {
                _fileContent.value = getApplication<Application>().getString(R.string.error_saf)
            }
        }
    }

    fun readFromUri(uri: Uri) {
        viewModelScope.launch {
            _fileContent.value = externalStorageHelper.readFromUri(uri)
        }
    }
}
