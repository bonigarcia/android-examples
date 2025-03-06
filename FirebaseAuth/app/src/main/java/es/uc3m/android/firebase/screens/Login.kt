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
package es.uc3m.android.firebase.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import es.uc3m.android.firebase.AuthViewModel
import es.uc3m.android.firebase.NavGraph
import es.uc3m.android.firebase.R

@Composable
fun LoginScreen(navController: NavController, auhViewModel: AuthViewModel) {
    var login by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current

    Row(modifier = Modifier.fillMaxSize()) {
        Spacer(
            modifier = Modifier
                .weight(0.15f)
                .fillMaxHeight()
        )
        Box(
            modifier = Modifier
                .weight(0.7f)
                .fillMaxHeight()
        ) {
            Column {
                Spacer(modifier = Modifier.fillMaxHeight(0.2f))
                Text(
                    text = stringResource(R.string.login_label),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = stringResource(R.string.sign_in_to_continue),
                    modifier = Modifier.padding(bottom = 16.dp),
                    style = MaterialTheme.typography.bodySmall
                )
                TextField(
                    value = login,
                    onValueChange = { login = it },
                    placeholder = {
                        Text(stringResource(R.string.email_edit_text))
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = {
                        Text(stringResource(R.string.password_edit_text))
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )
                Button(
                    onClick = {
                        auhViewModel.login(login, password, context, navController)
                    },
                    modifier = Modifier
                        .align(alignment = Alignment.End)
                        .padding(top = 16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(stringResource(R.string.login_label))
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_arrow_forward_24),
                            contentDescription = stringResource(R.string.login_label),
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .align(alignment = Alignment.BottomCenter)
                    .fillMaxHeight(0.2f)
            ) {
                TextButton(
                    onClick = { navController.navigate(NavGraph.Signup.route) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.don_t_have_an_account_sign_up))
                }
            }
        }
        Spacer(
            modifier = Modifier
                .weight(0.15f)
                .fillMaxHeight()
        )
    }
}
