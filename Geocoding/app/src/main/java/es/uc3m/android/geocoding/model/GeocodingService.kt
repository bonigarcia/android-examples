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
package es.uc3m.android.geocoding.model

import android.content.Context
import android.location.Address
import android.location.Geocoder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale

class GeocodingService(context: Context) {

    private val appContext = context.applicationContext

    suspend fun geocodeAddress(address: String): Result<Pair<Double, Double>> =
        withContext(Dispatchers.IO) {
            try {
                if (!Geocoder.isPresent()) {
                    return@withContext Result.failure(
                        IllegalStateException("Geocoder not available")
                    )
                }

                val geocoder = Geocoder(appContext, Locale.getDefault())
                val results = geocoder.getFromLocationName(address, 1)

                if (results.isNullOrEmpty()) {
                    Result.failure(NoSuchElementException("No results found"))
                } else {
                    val location = results[0]
                    Result.success(location.latitude to location.longitude)
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    suspend fun reverseGeocode(lat: Double, lng: Double): Result<String> =
        withContext(Dispatchers.IO) {
            try {
                if (!Geocoder.isPresent()) {
                    return@withContext Result.failure(
                        IllegalStateException("Geocoder not available")
                    )
                }

                val geocoder = Geocoder(appContext, Locale.getDefault())
                val results = geocoder.getFromLocation(lat, lng, 1)

                if (results.isNullOrEmpty()) {
                    Result.failure(NoSuchElementException("No results found"))
                } else {
                    val address = results[0]
                    val addressText = buildAddressText(address)
                    Result.success(addressText)
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    private fun buildAddressText(address: Address): String {
        return if (address.maxAddressLineIndex >= 0) {
            (0..address.maxAddressLineIndex).joinToString("\n") { index ->
                address.getAddressLine(
                    index
                )
            }
        } else {
            listOfNotNull(
                address.thoroughfare, address.locality, address.adminArea, address.countryName
            ).joinToString(", ")
        }
    }
}