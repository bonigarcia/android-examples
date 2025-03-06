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
package es.uc3m.android.firebase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import es.uc3m.android.firebase.screens.HomeScreen
import es.uc3m.android.firebase.screens.LoginScreen
import es.uc3m.android.firebase.screens.SignUpScreen
import es.uc3m.android.firebase.ui.theme.MyAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        setContent {
            MyAppTheme {
                NavContainer(authViewModel)
            }
        }
    }
}

@Composable
fun NavContainer(authViewModel: AuthViewModel) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = NavGraph.Login.route,
    ) {
        composable(NavGraph.Login.route) {
            LoginScreen(navController = navController, auhViewModel = authViewModel)
        }
        composable(NavGraph.Signup.route) {
            SignUpScreen(navController = navController, authViewModel = authViewModel)
        }
        composable(NavGraph.Home.route) {
            HomeScreen(navController = navController, authViewModel = authViewModel)
        }
    }
}

