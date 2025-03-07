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
package es.uc3m.android.firebase.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import es.uc3m.android.firebase.NavGraph
import es.uc3m.android.firebase.R
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

open class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth

    fun signUp(email: String, password: String, context: Context, navController: NavController) {
        viewModelScope.launch {
            try {
                auth.createUserWithEmailAndPassword(email, password).await()
                Toast.makeText(
                    context, context.getString(R.string.sign_up_ok), Toast.LENGTH_SHORT
                ).show()
                navController.navigate(NavGraph.Home.route)

            } catch (e: Exception) {
                Toast.makeText(
                    context, e.message, Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun login(email: String, password: String, context: Context, navController: NavController) {
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password).await()
                Toast.makeText(
                    context, context.getString(R.string.login_ok), Toast.LENGTH_SHORT
                ).show()
                navController.navigate(NavGraph.Home.route)

            } catch (e: Exception) {
                Toast.makeText(
                    context, e.message, Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun logout(context: Context, navController: NavController) {
        viewModelScope.launch {
            try {
                auth.signOut()
                Toast.makeText(
                    context, context.getString(R.string.logout_ok), Toast.LENGTH_SHORT
                ).show()
                navController.navigate(NavGraph.Login.route)

            } catch (e: Exception) {
                Toast.makeText(
                    context, e.message, Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}