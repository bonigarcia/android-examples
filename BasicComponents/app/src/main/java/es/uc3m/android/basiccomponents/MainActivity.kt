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
package es.uc3m.android.basiccomponents

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.uc3m.android.basiccomponents.ui.theme.MyAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TextSample() {
    Text(
        text = "Hello, Compose!",
        fontSize = 20.sp,
        color = Color.Blue,
        fontWeight = FontWeight.Bold
    )
}

@Preview(showBackground = true)
@Composable
fun ButtonSample() {
    Button(onClick = { /* Handle click */ }) {
        Text("Click Me")
    }
}


@Preview(showBackground = true)
@Composable
fun ImageSample() {
    Image(
        painter = painterResource(R.drawable.ic_launcher_foreground),
        contentDescription = "App Icon"
    )
}

@Preview(showBackground = true)
@Composable
fun IconSample() {
    Icon(
        painter = painterResource(R.drawable.baseline_directions_bus_24),
        contentDescription = "App Icon"
    )
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello, $name!",
        modifier = modifier
            .fillMaxWidth()
            .background(Color.LightGray)
    )
    Button(
        onClick = {
            println("TODO handle click")
        },
        modifier = modifier.padding(32.dp)
    ) {
        Text(
            text = "Click Me",
            fontSize = 24.sp,
            color = Color.Green
        )
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    MyAppTheme {
        Greeting("Android")
    }
}
