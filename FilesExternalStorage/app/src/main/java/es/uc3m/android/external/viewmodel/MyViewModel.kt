/*
 * (C) Copyright 2026 Boni Garcia (https://bonigarcia.github.io/)
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
package es.uc3m.android.external.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import es.uc3m.android.external.R
import es.uc3m.android.external.model.ExternalStorageHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MyViewModel(
    application: Application, private val externalStorageHelper: ExternalStorageHelper
) : AndroidViewModel(application) {

    private val _fileContent = MutableStateFlow("")
    val fileContent: StateFlow<String> get() = _fileContent

    private val _loadedImage = MutableStateFlow<Bitmap?>(null)
    val loadedImage: StateFlow<Bitmap?> get() = _loadedImage

    val externalFilesDirectory: String =
        externalStorageHelper.getExternalFilesDirectory()?.absolutePath ?: "N/A"

    private var lastSavedImageUri: Uri? = null

    // Helper method to access strings from resources
    private fun getString(resId: Int, vararg formatArgs: Any): String =
        getApplication<Application>().getString(resId, *formatArgs)

    // App-specific storage
    fun writeToAppSpecific(fileName: String, content: String) {
        viewModelScope.launch {
            if (externalStorageHelper.writeToAppSpecificStorage(fileName, content)) {
                _fileContent.value = getString(R.string.saved_app_specific)
            } else {
                _fileContent.value = getString(R.string.error_app_specific)
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
            lastSavedImageUri = uri
            _fileContent.value = if (uri != null) {
                getString(R.string.saved_media_store, uri.toString())
            } else {
                getString(R.string.error_media_store)
            }
        }
    }

    fun readImageFromMediaStore() {
        viewModelScope.launch {
            lastSavedImageUri?.let { uri ->
                val bitmap = externalStorageHelper.readImageFromUri(uri)
                _loadedImage.value = bitmap
                if (bitmap != null) {
                    _fileContent.value = getString(R.string.image_loaded)
                } else {
                    _fileContent.value = getString(R.string.load_image_error)
                }
            } ?: run {
                _fileContent.value = getString(R.string.save_image_first)
            }
        }
    }

    // SAF (Storage Access Framework)
    fun writeToUri(uri: Uri, content: String) {
        viewModelScope.launch {
            if (externalStorageHelper.writeToUri(uri, content)) {
                _fileContent.value = getString(R.string.saved_saf)
            } else {
                _fileContent.value = getString(R.string.error_saf)
            }
        }
    }

    fun readFromUri(uri: Uri) {
        viewModelScope.launch {
            _fileContent.value = externalStorageHelper.readFromUri(uri)
        }
    }
}