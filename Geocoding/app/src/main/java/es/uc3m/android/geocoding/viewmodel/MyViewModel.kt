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
package es.uc3m.android.geocoding.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import es.uc3m.android.geocoding.model.GeocodingService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MyViewModel(
    application: Application,
    private val geocodingService: GeocodingService
) : AndroidViewModel(application) {

    private val _address = MutableStateFlow("")
    val address: StateFlow<String> = _address.asStateFlow()

    private val _coordinates = MutableStateFlow("")
    val coordinates: StateFlow<String> = _coordinates.asStateFlow()

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage.asStateFlow()

    // Helper method to access strings from resources
    private fun getString(resId: Int, vararg formatArgs: Any): String =
        getApplication<Application>().getString(resId, *formatArgs)

    fun geocodeAddress(address: String) {
        viewModelScope.launch {
            val result = geocodingService.geocodeAddress(address)

            result.onSuccess { (lat, lng) ->
                _coordinates.value = "Lat: $lat, Lng: $lng"
                _errorMessage.value = ""
            }.onFailure { error ->
                _coordinates.value = ""
                _errorMessage.value = error.message ?: "Unknown error"
            }
        }
    }

    fun reverseGeocode(lat: Double, lng: Double) {
        viewModelScope.launch {
            val result = geocodingService.reverseGeocode(lat, lng)

            result.onSuccess { addressText ->
                _address.value = addressText
                _errorMessage.value = ""
            }.onFailure { error ->
                _address.value = ""
                _errorMessage.value = error.message ?: "Unknown error"
            }
        }
    }

    fun setErrorMessage(message: String?) {
        _errorMessage.value = message.orEmpty()
    }
}