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
package es.uc3m.android.directions

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.PendingResult
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.model.DirectionsResult
import com.google.maps.model.DirectionsRoute
import com.google.maps.model.TravelMode
import es.uc3m.android.directions.ui.theme.MyAppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    RouteMapperApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteMapperApp(modifier: Modifier = Modifier) {
    var origin by remember { mutableStateOf("") }
    var destination by remember { mutableStateOf("") }
    var routes by remember { mutableStateOf<List<DirectionsRoute>?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = origin,
            onValueChange = { origin = it },
            label = { Text(stringResource(R.string.origin)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = destination,
            onValueChange = { destination = it },
            label = { Text(stringResource(R.string.destination)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (origin.isNotEmpty() && destination.isNotEmpty()) {
                    isLoading = true
                    error = null
                    getDirections(origin, destination) { result, err ->
                        isLoading = false
                        if (err != null) {
                            routes = null
                            error = err
                        } else {
                            routes = result?.routes?.toList()
                            error = null
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading && origin.isNotEmpty() && destination.isNotEmpty()
        ) {
            Text(if (isLoading) stringResource(R.string.loading) else stringResource(R.string.get_directions))
        }

        error?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.error, it), color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        routes?.let {
            RouteMapView(routes = it)
        }
    }
}

@Composable
fun RouteMapView(routes: List<DirectionsRoute>) {
    // Default to first route's start location
    val firstRoute = routes.first()
    val startLocation = LatLng(
        firstRoute.legs[0].startLocation.lat,
        firstRoute.legs[0].startLocation.lng
    )

    // Camera position state
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(startLocation, 12f)
    }

    // For route selection if multiple routes exist
    var selectedRouteIndex by remember { mutableStateOf(0) }

    Column(Modifier.fillMaxSize()) {
        // Route selector if multiple routes
        if (routes.size > 1) {
            RouteSelector(routes, selectedRouteIndex) { index ->
                selectedRouteIndex = index
            }
        }

        // Render map
        GoogleMap(
            modifier = Modifier.weight(1f),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(mapType = MapType.NORMAL)
        ) {
            // Draw the selected route
            val selectedRoute = routes[selectedRouteIndex]
            DrawRoute(selectedRoute)

            // Add markers for start and end
            Marker(
                state = MarkerState(
                    position = LatLng(
                        selectedRoute.legs[0].startLocation.lat,
                        selectedRoute.legs[0].startLocation.lng
                    )
                ),
                title = stringResource(R.string.start, selectedRoute.legs[0].startAddress)
            )
            Marker(
                state = MarkerState(
                    position = LatLng(
                        selectedRoute.legs[0].endLocation.lat,
                        selectedRoute.legs[0].endLocation.lng
                    )
                ),
                title = stringResource(R.string.end, selectedRoute.legs[0].endAddress)
            )
        }
    }
}

@Composable
private fun DrawRoute(route: DirectionsRoute) {
    // Convert the polyline points to LatLng objects
    val pathPoints = remember(route) {
        route.overviewPolyline.decodePath().map { LatLng(it.lat, it.lng) }
    }

    // Draw the polyline
    Polyline(
        points = pathPoints,
        color = Color.Blue,
        width = 8f
    )
}

@Composable
private fun RouteSelector(
    routes: List<DirectionsRoute>,
    selectedIndex: Int,
    onSelectionChanged: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        routes.forEachIndexed { index, route ->
            Button(
                onClick = { onSelectionChanged(index) },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (index == selectedIndex) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.secondary
                )
            ) {
                Text(
                    text = stringResource(R.string.route_label, index + 1),
                    maxLines = 1
                )
            }
            if (index < routes.size - 1) Spacer(modifier = Modifier.width(4.dp))
        }
    }
}

fun getDirections(
    origin: String, destination: String, callback: (DirectionsResult?, String?) -> Unit
) {
    val context = GeoApiContext.Builder().apiKey(BuildConfig.MAPS_API_KEY).build()

    DirectionsApi.newRequest(context).mode(TravelMode.DRIVING).origin(origin)
        .destination(destination).alternatives(true)
        .setCallback(object : PendingResult.Callback<DirectionsResult> {
            override fun onResult(result: DirectionsResult) {
                callback(result, null)
            }

            override fun onFailure(e: Throwable) {
                callback(null, e.message)
            }
        })
}