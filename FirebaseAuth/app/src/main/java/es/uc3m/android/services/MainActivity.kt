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
package es.uc3m.android.services

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import es.uc3m.android.services.screens.HomeScreen
import es.uc3m.android.services.screens.LoginScreen
import es.uc3m.android.services.screens.SignUpScreen
import es.uc3m.android.services.ui.theme.MyAppTheme
import es.uc3m.android.services.viewmodel.MyViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyAppTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: MyViewModel = viewModel()) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val toastMessage by viewModel.toastMessage.collectAsState()
    val routeState by viewModel.route.collectAsState()

    NavHost(
        navController = navController,
        startDestination = NavGraph.Login.route,
    ) {
        composable(NavGraph.Login.route) {
            LoginScreen(viewModel = viewModel)
        }
        composable(NavGraph.Signup.route) {
            SignUpScreen(viewModel = viewModel)
        }
        composable(NavGraph.Home.route) {
            HomeScreen(viewModel = viewModel)
        }
    }

    LaunchedEffect(toastMessage) {
        toastMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            // Reset message to avoid showing it repeatedly (e.g., on configuration changes)
            viewModel.showToast(null)
        }
    }

    LaunchedEffect(routeState) {
        routeState?.let { route ->
            navController.navigate(route)
            viewModel.navigate(null)
        }
    }

}

