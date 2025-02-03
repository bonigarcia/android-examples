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
package es.uc3m.android.layoutbasics

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import es.uc3m.android.layoutbasics.ui.theme.MyAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DevLayout(
                        developer = Developer("John Doe", "Developer"),
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun DevCard() {
    Text("John Doe")
    Text("Developer")
}

@Composable
fun DevCardColumn() {
    Column {
        Text("John Doe")
        Text("Developer")
    }
}

@Composable
fun DevCardRow() {
    Row {
        Text("John Doe")
        Text(" - ")
        Text("Developer")
    }
}

@Composable
fun DevCardBox() {
    Box {
        Image(
            bitmap = ImageBitmap.imageResource(R.drawable.user_icon),
            contentDescription = "Developer image"
        )
        Icon(
            imageVector = Icons.Outlined.Edit,
            contentDescription = "Edit user"
        )
    }
}

data class Developer(var name: String, var role: String)

@Composable
fun DevLayout(developer: Developer, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxSize()
    ) {
        Image(
            bitmap = ImageBitmap.imageResource(R.drawable.user_icon),
            contentDescription = "Developer image"
        )
        Column {
            Text(developer.name)
            Text(developer.role)
            Button(
                onClick = {
                    Log.d("MainActivity", "Button clicked")
                },
                content = {
                    Text("Click Me")
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NoLayoutPreview() {
    MyAppTheme {
        DevCard()
    }
}

@Preview(showBackground = true)
@Composable
fun ColumnPreview() {
    MyAppTheme {
        DevCardColumn()
    }
}

@Preview(showBackground = true)
@Composable
fun RowPreview() {
    MyAppTheme {
        DevCardRow()
    }
}

@Preview(showBackground = true)
@Composable
fun BoxPreview() {
    MyAppTheme {
        DevCardBox()
    }
}


@Preview(showBackground = true)
@Composable
fun Preview() {
    MyAppTheme {
        DevLayout(Developer("John Doe", "Developer"))
    }
}
