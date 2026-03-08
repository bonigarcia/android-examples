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
package es.uc3m.android.internal.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.uc3m.android.internal.model.InternalStorageHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MyViewModel(private val internalStorageHelper: InternalStorageHelper) : ViewModel() {

    private val _fileContent = MutableStateFlow("")
    val fileContent: StateFlow<String> = _fileContent.asStateFlow()

    private val _fileList = MutableStateFlow<List<String>>(emptyList())
    val fileList: StateFlow<List<String>> = _fileList.asStateFlow()

    fun writeToFile(fileName: String, content: String) {
        viewModelScope.launch {
            val success = internalStorageHelper.writeToFile(fileName, content)
            if (success) {
                refreshFileList()
            }
        }
    }

    fun readFromFile(fileName: String) {
        viewModelScope.launch {
            _fileContent.value = internalStorageHelper.readFromFile(fileName)
        }
    }

    fun refreshFileList() {
        viewModelScope.launch {
            _fileList.value = internalStorageHelper.listFiles().toList()
        }
    }

    fun deleteFile(fileName: String) {
        viewModelScope.launch {
            val success = internalStorageHelper.deleteFile(fileName)
            if (success) {
                refreshFileList()
            }
        }
    }
}