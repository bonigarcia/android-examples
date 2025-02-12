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
package es.uc3m.android.navigationresult

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import es.uc3m.android.navigationresult.ui.theme.MyAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "home"
                    ) {
                        composable("home") {
                            HomeScreen(navController)
                        }
                        composable("second") {
                            SecondScreen(navController)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreen(navController: NavController) {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.home_msg),
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = { navController.navigate("second") },
        ) {
            Text(stringResource(R.string.button_msg))
        }
    }
    val secondScreenResult = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getLiveData<String>("message")?.observeAsState()
    secondScreenResult?.value?.let {
        Toast.makeText(
            LocalContext.current,
            secondScreenResult.value,
            Toast.LENGTH_SHORT
        ).show()
    }
}

@Composable
fun SecondScreen(navController: NavController) {
    var text by rememberSaveable { mutableStateOf("") }

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.second_msg),
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(Modifier.height(8.dp))
        TextField(
            value = text,
            onValueChange = { text = it }
        )
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = {
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set("message", text)
                navController.popBackStack()
            },
        ) {
            Text(stringResource(R.string.back_msg))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyAppTheme {
        HomeScreen(rememberNavController())
    }
}

