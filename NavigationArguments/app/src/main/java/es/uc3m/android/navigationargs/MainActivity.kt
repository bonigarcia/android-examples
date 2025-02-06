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
package es.uc3m.android.navigationargs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import es.uc3m.android.navigationargs.ui.theme.MyAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    val modifier = Modifier.padding(innerPadding)
                    NavHost(
                        navController = navController,
                        startDestination = "first"
                    ) {
                        composable("first") {
                            FirstScreen(navController, modifier)
                        }
                        composable(
                            "second/{name}",
                            arguments = listOf(navArgument("name") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val name = backStackEntry.arguments?.getString("name")
                            name?.let { SecondScreen(modifier, it) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FirstScreen(navController: NavController, modifier: Modifier = Modifier) {
    var text by rememberSaveable { mutableStateOf("") }

    Column(modifier = modifier) {
        Text(text = stringResource(R.string.text_msg))
        TextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = { navController.navigate("second/$text") },
        ) {
            Text(stringResource(R.string.button_msg))
        }
    }
}

@Composable
fun SecondScreen(modifier: Modifier = Modifier, name: String = "") {
    val hello = String.format(stringResource(R.string.hello_msg), name)
    Text(text = hello, modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyAppTheme {
        FirstScreen(rememberNavController())
    }
}

