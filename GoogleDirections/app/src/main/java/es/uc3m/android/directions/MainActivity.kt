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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.PendingResult
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
            label = { Text("Origin") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = destination,
            onValueChange = { destination = it },
            label = { Text("Destination") },
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
                            error = err
                            routes = null
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
    Column {
        Text(
            text = stringResource(R.string.route_information),
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        routes.forEachIndexed { index, route ->
            Text(
                text = stringResource(R.string.route, index + 1, route.summary),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = stringResource(R.string.distance, route.legs[0].distance),
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = stringResource(R.string.duration, route.legs[0].duration),
                style = MaterialTheme.typography.bodySmall
            )

            if (index < routes.size - 1) {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

fun getDirections(
    origin: String, destination: String, callback: (DirectionsResult?, String?) -> Unit
) {
    val context = GeoApiContext.Builder().apiKey(BuildConfig.MAPS_API_KEY).build()

    DirectionsApi.newRequest(context).mode(TravelMode.DRIVING).origin(origin)
        .destination(destination).setCallback(object : PendingResult.Callback<DirectionsResult> {
            override fun onResult(result: DirectionsResult) {
                callback(result, null)
            }

            override fun onFailure(e: Throwable) {
                callback(null, e.message)
            }
        })
}