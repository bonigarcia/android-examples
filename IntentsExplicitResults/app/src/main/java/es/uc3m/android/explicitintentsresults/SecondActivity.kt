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
package es.uc3m.android.explicitintentsresults

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import es.uc3m.android.explicitintentsresults.ui.theme.MyAppTheme

class SecondActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val name = intent.getStringExtra("name")
                    val hello = String.format(stringResource(R.string.hello_msg), name)
                    MyLayout(modifier = Modifier.padding(innerPadding), hello)
                }
            }
        }
    }

}

@Composable
fun MyLayout(modifier: Modifier = Modifier, text: String) {
    val okMessage = stringResource(R.string.ok_message)
    val cancelMessage = stringResource(R.string.cancel_message)
    val context = LocalContext.current

    Column(modifier = modifier) {
        Text(text = text)
        Row() {
            Button(
                onClick = {
                    if (context is Activity) {
                        finishActivity(context, okMessage, Activity.RESULT_OK)
                    }
                },
            ) {
                Text(stringResource(R.string.button2))
            }
            Button(
                modifier = Modifier.padding(start = 16.dp),
                onClick = {
                    if (context is Activity) {
                        finishActivity(context, cancelMessage, Activity.RESULT_CANCELED)
                    }
                },
            ) {
                Text(stringResource(R.string.button3))
            }
        }
    }
}

private fun finishActivity(context: Activity, resultData: String, resultCode: Int) {
    val resultIntent = Intent().apply {
        putExtra("message", resultData)
    }
    context.setResult(resultCode, resultIntent)
    context.finish()
}

