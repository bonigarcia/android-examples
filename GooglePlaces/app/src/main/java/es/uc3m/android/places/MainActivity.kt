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

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import es.uc3m.android.places.ui.theme.MyAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PlacesScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun PlacesScreen(modifier: Modifier = Modifier) {
    val viewModel: PlacesViewModel = viewModel()
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }

    // Initialize Places API
    LaunchedEffect(Unit) {
        viewModel.initializePlaces(context)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Search bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                viewModel.searchPlaces(it)
            },
            label = { Text(stringResource(R.string.search_for_places)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = { viewModel.searchPlaces(searchQuery) })
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Search results
        val predictions by viewModel.predictions.collectAsState()
        if (predictions.isNotEmpty()) {
            Text(
                stringResource(R.string.search_results),
                style = MaterialTheme.typography.titleMedium
            )
            LazyColumn {
                items(predictions) { prediction ->
                    PredictionItem(prediction) {
                        viewModel.getPlaceDetails(it)
                    }
                }
            }
        }

        // Selected place details
        val selectedPlace by viewModel.selectedPlace.collectAsState()
        selectedPlace?.let { place ->
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                stringResource(R.string.selected_place),
                style = MaterialTheme.typography.titleMedium
            )
            PlaceDetailsCard(place)
        }
    }
}

@Composable
fun PredictionItem(
    prediction: PlaceAutocomplete, onItemClick: (String) -> Unit
) {
    Card(
        onClick = { onItemClick(prediction.placeId) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(prediction.primaryText, style = MaterialTheme.typography.bodyLarge)
            Text(prediction.secondaryText, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun PlaceDetailsCard(place: PlaceDetails) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(place.name, style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text(place.address, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                stringResource(R.string.location, place.latLng.latitude, place.latLng.longitude),
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Image(
                bitmap = place.bitmap.asImageBitmap(),
                contentDescription = place.name
            )
        }
    }
}