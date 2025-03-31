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
package es.uc3m.android.geocoding

import android.content.Context
import android.location.Geocoder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Locale

class GeocodingViewModel : ViewModel() {
    private val _address = MutableStateFlow("")
    val address: StateFlow<String> = _address

    private val _coordinates = MutableStateFlow("")
    val coordinates: StateFlow<String> = _coordinates

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    fun geocodeAddress(context: Context, address: String) {
        viewModelScope.launch {
            try {
                val geocoder = Geocoder(context, Locale.getDefault())
                val addresses = geocoder.getFromLocationName(address, 1)
                if (addresses?.isNotEmpty() == true) {
                    val location = addresses[0]
                    val lat = location.latitude
                    val lng = location.longitude
                    _coordinates.value = "Lat: $lat, Lng: $lng"
                    _errorMessage.value = ""
                }
            } catch (e: Exception) {
                setErrorMessage(e.message)
            }
        }
    }

    fun reverseGeocode(context: Context, lat: Double, lng: Double) {
        viewModelScope.launch {
            try {
                val geocoder = Geocoder(context, Locale.getDefault())
                val addresses = geocoder.getFromLocation(lat, lng, 1)
                if (addresses?.isNotEmpty() == true) {
                    val address = addresses[0]
                    val addressText = (0..address.maxAddressLineIndex).joinToString("\n") {
                        address.getAddressLine(it)
                    }
                    _address.value = addressText
                    _errorMessage.value = ""
                }
            } catch (e: Exception) {
                setErrorMessage(e.message)
            }
        }
    }

    fun setErrorMessage(message: String?) {
        _errorMessage.value = message.orEmpty()
    }
}