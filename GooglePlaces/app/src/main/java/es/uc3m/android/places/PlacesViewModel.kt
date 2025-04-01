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
package es.uc3m.android.places

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPhotoRequest
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlacesViewModel : ViewModel() {
    private lateinit var placesClient: PlacesClient

    private val _predictions = MutableStateFlow<List<PlaceAutocomplete>>(emptyList())
    val predictions: StateFlow<List<PlaceAutocomplete>> = _predictions

    private val _selectedPlace = MutableStateFlow<PlaceDetails?>(null)
    val selectedPlace: StateFlow<PlaceDetails?> = _selectedPlace

    fun initializePlaces(context: Context) {
        if (!Places.isInitialized()) {
            Places.initialize(context, BuildConfig.MAPS_API_KEY)
        }
        placesClient = Places.createClient(context)
    }

    fun searchPlaces(query: String) {
        viewModelScope.launch {
            val request = FindAutocompletePredictionsRequest.builder().setQuery(query).build()

            placesClient.findAutocompletePredictions(request).addOnSuccessListener { response ->
                _predictions.value = response.autocompletePredictions.map { prediction ->
                    PlaceAutocomplete(
                        placeId = prediction.placeId,
                        primaryText = prediction.getPrimaryText(null).toString(),
                        secondaryText = prediction.getSecondaryText(null).toString()
                    )
                }
            }.addOnFailureListener { exception ->
                exception.printStackTrace()
            }
        }
    }

    fun getPlaceDetails(placeId: String) {
        viewModelScope.launch {
            val placeFields = listOf(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG,
                Place.Field.PHOTO_METADATAS
            )
            val request = FetchPlaceRequest.builder(placeId, placeFields).build()

            placesClient.fetchPlace(request).addOnSuccessListener { response ->
                val place = response.place
                place.photoMetadatas?.first()?.let {
                    val photoRequest = FetchPhotoRequest.builder(it).build()
                    placesClient.fetchPhoto(photoRequest).addOnSuccessListener { response ->
                            _selectedPlace.value = PlaceDetails(
                                name = place.displayName ?: "Unknown",
                                address = place.formattedAddress ?: "No address",
                                latLng = place.location ?: LatLng(0.0, 0.0),
                                bitmap = response.bitmap
                            )
                        }.addOnFailureListener { exception ->
                            exception.printStackTrace()
                        }
                }
            }.addOnFailureListener { exception ->
                exception.printStackTrace()
            }
        }
    }

}


data class PlaceAutocomplete(
    val placeId: String, val primaryText: String, val secondaryText: String
)

data class PlaceDetails(
    val name: String, val address: String, val latLng: LatLng, var bitmap: Bitmap
)