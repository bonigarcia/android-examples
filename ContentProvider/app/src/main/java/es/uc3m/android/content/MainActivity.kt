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
package es.uc3m.android.content

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import es.uc3m.android.content.MyDatabaseHelper.Companion.TABLE_NAME
import es.uc3m.android.content.ui.theme.MyAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyAppTheme {
                ContentProviderDemoApp()
            }
        }
    }
}

@SuppressLint("Range")
@Composable
fun ContentProviderDemoApp() {
    val context = LocalContext.current
    var text by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text(stringResource(R.string.enter_name)) })

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val values = ContentValues().apply {
                put(MyDatabaseHelper.COLUMN_NAME, text)
            }
            context.contentResolver.insert(
                "content://${context.packageName}.provider/${TABLE_NAME}".toUri(), values
            )
            result = context.getString(R.string.inserted, text)
        }) {
            Text(stringResource(R.string.insert))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val cursor = context.contentResolver.query(
                "content://${context.packageName}.provider/${TABLE_NAME}".toUri(),
                null,
                null,
                null,
                null
            )
            val names = mutableListOf<String>()
            cursor?.use {
                while (it.moveToNext()) {
                    names.add(it.getString(it.getColumnIndex(MyDatabaseHelper.COLUMN_NAME)))
                }
            }
            result = context.getString(R.string.names, names.joinToString(", "))
        }) {
            Text(stringResource(R.string.query))
        }

        Spacer(modifier = Modifier.run { height(16.dp) })

        Text(result)
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    MyAppTheme {
        ContentProviderDemoApp()
    }
}