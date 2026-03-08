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
package es.uc3m.android.cache.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.uc3m.android.cache.model.CacheFileHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MyViewModel(private val cacheFileHelper: CacheFileHelper) : ViewModel() {

    private val _fileContent = MutableStateFlow("")
    val fileContent: StateFlow<String> get() = _fileContent

    private val _fileList = MutableStateFlow<List<String>>(emptyList())
    val fileList: StateFlow<List<String>> get() = _fileList

    init {
        refreshFileList()
    }

    fun writeToCache(fileName: String, content: String) {
        viewModelScope.launch {
            val success = cacheFileHelper.writeToCache(fileName, content)
            if (success) {
                _fileContent.value = "File saved successfully!"
                refreshFileList()
            } else {
                _fileContent.value = "Failed to save file."
            }
        }
    }

    fun readFromCache(fileName: String) {
        viewModelScope.launch {
            _fileContent.value = cacheFileHelper.readFromCache(fileName)
        }
    }

    fun refreshFileList() {
        viewModelScope.launch {
            _fileList.value = cacheFileHelper.listFiles().toList()
        }
    }

    fun deleteFromCache(fileName: String) {
        viewModelScope.launch {
            val success = cacheFileHelper.deleteFromCache(fileName)
            if (success) {
                _fileContent.value = "File deleted successfully!"
                refreshFileList()
            } else {
                _fileContent.value = "Failed to delete file."
            }
        }
    }
}