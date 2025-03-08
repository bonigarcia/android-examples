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
package es.uc3m.android.internal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FileViewModel(private val fileHelper: FileHelper) : ViewModel() {

    private val _fileContent = MutableStateFlow("")
    val fileContent: StateFlow<String> get() = _fileContent

    private val _fileList = MutableStateFlow<List<String>>(emptyList())
    val fileList: StateFlow<List<String>> get() = _fileList

    fun writeToFile(fileName: String, content: String) {
        viewModelScope.launch {
            val success = fileHelper.writeToFile(fileName, content)
            if (success) {
                refreshFileList()
            }
        }
    }

    fun readFromFile(fileName: String) {
        viewModelScope.launch {
            _fileContent.value = fileHelper.readFromFile(fileName)
        }
    }

    fun refreshFileList() {
        viewModelScope.launch {
            _fileList.value = fileHelper.listFiles().toList()
        }
    }

    fun deleteFile(fileName: String) {
        viewModelScope.launch {
            val success = fileHelper.deleteFile(fileName)
            if (success) {
                refreshFileList()
            }
        }
    }
}