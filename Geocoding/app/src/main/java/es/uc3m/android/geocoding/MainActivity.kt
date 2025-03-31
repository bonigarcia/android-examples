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
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import es.uc3m.android.geocoding.ui.theme.MyAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GeocodingApp(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun GeocodingApp(modifier: Modifier = Modifier, viewModel: GeocodingViewModel = viewModel()) {
    val context = LocalContext.current

    // State for text fields
    var addressInput by remember { mutableStateOf("") }
    var latInput by remember { mutableStateOf("") }
    var lngInput by remember { mutableStateOf("") }

    // Collect ViewModel state
    val address by viewModel.address.collectAsState()
    val coordinates by viewModel.coordinates.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Geocoding (address to coordinates)
        Text(
            text = stringResource(R.string.geocoding_label),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = addressInput,
            onValueChange = { addressInput = it },
            label = { Text(stringResource(R.string.enter_address)) },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                viewModel.geocodeAddress(context, addressInput)
            }, modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(stringResource(R.string.get_coordinates))
        }

        if (coordinates.isNotEmpty()) {
            Text(
                text = coordinates,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Reverse geocoding (coordinates to address)
        Text(
            text = stringResource(R.string.reverse_geocoding_label),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = latInput,
                onValueChange = { latInput = it },
                label = { Text(stringResource(R.string.latitude)) },
                modifier = Modifier.weight(1f)
            )

            OutlinedTextField(
                value = lngInput,
                onValueChange = { lngInput = it },
                label = { Text(stringResource(R.string.longitude)) },
                modifier = Modifier.weight(1f)
            )
        }

        Button(
            onClick = {
                try {
                    val lat = latInput.toDouble()
                    val lng = lngInput.toDouble()
                    viewModel.reverseGeocode(context, lat, lng)
                } catch (e: NumberFormatException) {
                    viewModel.setErrorMessage(e.message)
                }
            }, modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(stringResource(R.string.get_address))
        }

        if (address.isNotEmpty()) {
            Text(
                text = address,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        if (errorMessage.isNotEmpty()) {
            Text(
                text = stringResource(R.string.error, errorMessage),
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}