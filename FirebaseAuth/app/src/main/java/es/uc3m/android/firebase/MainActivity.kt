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
import android.widget.Toast
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
import es.uc3m.android.firebase.viewmodel.AuthViewModel
import es.uc3m.android.firebase.viewmodel.MyViewModel

private lateinit var viewModel: MyViewModel
private lateinit var authViewModel: AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyAppTheme {
                NoteListScreen()
            }
        }
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        viewModel = ViewModelProvider(this)[MyViewModel::class.java]

        // Observe potential toast messages in the view model
        viewModel.toastMessage.observe(this) { message ->
            if (!message.isNullOrEmpty()) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@Composable
fun NoteListScreen() {
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
            HomeScreen(
                navController = navController,
                authViewModel = authViewModel,
                noteViewModel = viewModel
            )
        }
    }
}

