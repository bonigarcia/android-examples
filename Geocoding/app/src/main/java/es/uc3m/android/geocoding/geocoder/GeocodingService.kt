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
package es.uc3m.android.geocoding.geocoder

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import es.uc3m.android.geocoding.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.Locale
import kotlin.coroutines.resume

@Suppress("DEPRECATION")
class GeocodingService(context: Context) {

    private val appContext = context.applicationContext
    private val geocoder = Geocoder(appContext, Locale.getDefault())

    suspend fun geocodeAddress(address: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val results = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                suspendCancellableCoroutine<List<Address>?> { cont ->
                    geocoder.getFromLocationName(address, 1) { list ->
                        if (cont.isActive) cont.resume(list)
                    }
                }
            } else {
                geocoder.getFromLocationName(address, 1)
            }
            if (results.isNullOrEmpty()) {
                Result.failure(NoSuchElementException(getString(R.string.no_results)))
            } else {
                val location = results[0]
                Result.success(
                    getString(
                        R.string.lat_lon, location.latitude, location.longitude
                    )
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun reverseGeocode(lat: Double, lng: Double): Result<String> =
        withContext(Dispatchers.IO) {
            try {
                val results = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    suspendCancellableCoroutine<List<Address>?> { cont ->
                        geocoder.getFromLocation(lat, lng, 1) { list ->
                            if (cont.isActive) cont.resume(list)
                        }
                    }
                } else {
                    geocoder.getFromLocation(lat, lng, 1)
                }
                if (results.isNullOrEmpty()) {
                    Result.failure(NoSuchElementException(getString(R.string.no_results)))
                } else {
                    val address = results[0]
                    val addressText = buildAddressText(address)
                    Result.success(addressText)
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    // Helper method to build text address from Address object
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

    // Helper method to access strings from resources
    private fun getString(resId: Int, vararg formatArgs: Any): String =
        appContext.getString(resId, *formatArgs)

}
