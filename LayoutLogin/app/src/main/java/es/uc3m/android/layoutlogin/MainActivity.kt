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
package es.uc3m.android.layoutlogin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import es.uc3m.android.layoutlogin.ui.theme.MyAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MyConstraintLayout(modifier = Modifier.padding(innerPadding))
                    // MyLayout(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MyConstraintLayout(modifier: Modifier = Modifier) {
    ConstraintLayout(
        modifier = modifier.fillMaxSize()
    ) {
        // State variables for text fields
        var login by rememberSaveable { mutableStateOf("") }
        var password by rememberSaveable { mutableStateOf("") }

        // Guideline from the start of the parent at 15% the width
        val startGuideline = createGuidelineFromStart(0.15f)
        // Guideline from the end of the parent at 15% the width
        val endGuideline = createGuidelineFromEnd(0.15f)
        // Guideline from the top of the parent at 20% the height
        val topGuideline = createGuidelineFromTop(0.2f)
        // Guideline from the bottom of the parent at 20% the height
        val bottomGuideline = createGuidelineFromBottom(0.2f)

        // References for the components
        val (text1, text2, loginField, passwordField, button, text3) = createRefs()

        Text(
            text = stringResource(R.string.login_label),
            modifier = Modifier
                .constrainAs(text1) {
                    start.linkTo(startGuideline)
                    top.linkTo(topGuideline)
                },
            style = MaterialTheme.typography.headlineLarge
        )
        Text(
            text = stringResource(R.string.sign_in_to_continue),
            modifier = Modifier
                .constrainAs(text2) {
                    top.linkTo(text1.bottom)
                    start.linkTo(startGuideline)
                },
            style = MaterialTheme.typography.bodySmall
        )
        TextField(
            value = login,
            onValueChange = { login = it },
            modifier = Modifier
                .constrainAs(loginField) {
                    top.linkTo(text2.bottom, margin = 16.dp)
                    start.linkTo(startGuideline)
                },
            placeholder = {
                Text(stringResource(R.string.email_edit_text))
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        TextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier
                .constrainAs(passwordField) {
                    top.linkTo(loginField.bottom)
                    start.linkTo(startGuideline)
                },
            placeholder = {
                Text(stringResource(R.string.password_edit_text))
            },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        Button(
            onClick = {
                println("TODO login")
            },
            modifier = Modifier
                .constrainAs(button) {
                    top.linkTo(passwordField.bottom, margin = 16.dp)
                    end.linkTo(endGuideline)
                }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(stringResource(R.string.login_button_text))
                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_forward_24),
                    contentDescription = stringResource(R.string.login_button_text),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
        Text(
            text = stringResource(R.string.don_t_have_an_account_sign_up),
            modifier = Modifier
                .constrainAs(text3) {
                    top.linkTo(bottomGuideline)
                    linkTo(
                        start = startGuideline,
                        end = endGuideline,
                        bias = 0.5f,
                    )
                }
                .clickable {
                    println("TODO sign up")
                },
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun MyLayout(modifier: Modifier = Modifier) {
    // State variables for text fields
    var login by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    Row(modifier = modifier.fillMaxSize()) {
        // Left spacer (15% width)
        Spacer(
            modifier = Modifier
                .weight(0.15f)
                .fillMaxHeight()
        )
        // Middle content (70% width)
        Box(
            modifier = Modifier
                .weight(0.7f)
                .fillMaxHeight()
        ) {
            Column {
                Spacer(modifier = modifier.fillMaxHeight(0.2f))
                Text(
                    text = stringResource(R.string.login_label),
                    style = MaterialTheme.typography.headlineLarge
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
                        println("TODO login")
                    },
                    modifier = Modifier
                        .align(alignment = Alignment.End)
                        .padding(top = 16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(stringResource(R.string.login_button_text))
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_arrow_forward_24),
                            contentDescription = stringResource(R.string.login_button_text),
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
                Text(
                    text = stringResource(R.string.don_t_have_an_account_sign_up),
                    modifier = Modifier
                        .align(alignment = Alignment.CenterHorizontally)
                        .clickable {
                            println("TODO sign up")
                        },
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        // Right spacer (15% width)
        Spacer(
            modifier = Modifier
                .weight(0.15f)
                .fillMaxHeight()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MyConstraintLayoutPreview() {
    MyAppTheme {
        MyConstraintLayout()
    }
}

@Preview(showBackground = true)
@Composable
fun MyLayoutPreview() {
    MyAppTheme {
        MyLayout()
    }
}

