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
package es.uc3m.android.rest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.uc3m.android.rest.model.DummyJsonClient
import es.uc3m.android.rest.model.Recipe
import es.uc3m.android.rest.model.Todo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RestViewModel : ViewModel() {
    private val _todos = MutableStateFlow<List<Todo>>(emptyList())
    val todos: StateFlow<List<Todo>> = _todos.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _snackMessage = MutableStateFlow<String?>(null)
    val snackMessage: StateFlow<String?> = _snackMessage.asStateFlow()

    fun fetchTodos() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = DummyJsonClient.apiService.getTodos()
                if (response.isSuccessful) {
                    _todos.value = response.body()?.todos ?: emptyList()
                }
            } catch (e: Exception) {
                _snackMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addRecipe(recipe: Recipe) {
        viewModelScope.launch {
            try {
                val response = DummyJsonClient.apiService.addRecipe(recipe)
                _snackMessage.value = response.code().toString() + " " + response.message()
            } catch (e: Exception) {
                _snackMessage.value = e.message
            }
        }
    }

    fun setSnackMessage(message: String?) {
        _snackMessage.value = message
    }

}