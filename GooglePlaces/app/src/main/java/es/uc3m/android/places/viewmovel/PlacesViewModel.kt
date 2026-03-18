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
package es.uc3m.android.places.viewmovel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchResolvedPhotoUriRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import es.uc3m.android.places.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.net.URL

class PlacesViewModel : ViewModel() {
    private lateinit var placesClient: PlacesClient

    private val _predictions = MutableStateFlow<List<PlaceAutocomplete>>(emptyList())
    val predictions: StateFlow<List<PlaceAutocomplete>> = _predictions

    private val _selectedPlace = MutableStateFlow<PlaceDetails?>(null)
    val selectedPlace: StateFlow<PlaceDetails?> = _selectedPlace

    fun initializePlaces(context: Context) {
        if (!Places.isInitialized()) {
            Places.initializeWithNewPlacesApiEnabled(
                context.applicationContext, BuildConfig.MAPS_API_KEY
            )
        }
        placesClient = Places.createClient(context)
    }

    fun searchPlaces(query: String) {
        viewModelScope.launch {
            try {
                val request = FindAutocompletePredictionsRequest.builder().setQuery(query).build()
                val response = placesClient.findAutocompletePredictions(request).await()
                _predictions.value = response.autocompletePredictions.map { prediction ->
                    PlaceAutocomplete(
                        placeId = prediction.placeId,
                        primaryText = prediction.getPrimaryText(null).toString(),
                        secondaryText = prediction.getSecondaryText(null).toString()
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getPlaceDetails(placeId: String) {
        viewModelScope.launch {
            try {
                val placeFields = listOf(
                    Place.Field.ID,
                    Place.Field.DISPLAY_NAME,
                    Place.Field.FORMATTED_ADDRESS,
                    Place.Field.LOCATION,
                    Place.Field.PHOTO_METADATAS
                )
                val request = FetchPlaceRequest.builder(placeId, placeFields).build()
                val response = placesClient.fetchPlace(request).await()
                val place = response.place

                place.photoMetadatas?.firstOrNull()?.let { photoMetadata ->
                    val photoRequest = FetchResolvedPhotoUriRequest.builder(photoMetadata).build()
                    val photoResponse = placesClient.fetchResolvedPhotoUri(photoRequest).await()
                    val photoUri = photoResponse.uri

                    val bitmap = photoUri?.let { uri ->
                        withContext(Dispatchers.IO) {
                            URL(uri.toString()).openStream().use {
                                BitmapFactory.decodeStream(it)
                            }
                        }
                    }

                    if (bitmap != null) {
                        _selectedPlace.value = PlaceDetails(
                            name = place.displayName ?: "Unknown",
                            address = place.formattedAddress ?: "No address",
                            latLng = place.location ?: LatLng(0.0, 0.0),
                            bitmap = bitmap
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
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