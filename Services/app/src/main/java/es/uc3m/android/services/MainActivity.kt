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
package es.uc3m.android.services

import android.annotation.SuppressLint
import android.content.Context.BIND_AUTO_CREATE
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import es.uc3m.android.services.ui.theme.MyAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ServiceDemoApp(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun ServiceDemoApp(modifier: Modifier = Modifier) {
    var serviceBound by remember { mutableStateOf(false) }
    var boundService: BoundService? by remember { mutableStateOf(null) }
    val context = LocalContext.current

    val connection = remember {
        object : android.content.ServiceConnection {
            override fun onServiceConnected(
                name: android.content.ComponentName?, service: IBinder?
            ) {
                val binder = service as BoundService.LocalBinder
                boundService = binder.getService()
                serviceBound = true
            }

            override fun onServiceDisconnected(name: android.content.ComponentName?) {
                serviceBound = false
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Started Service
        Text(
            stringResource(R.string.started_service), style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            val intent = Intent(context, StartedService::class.java).apply {
                putExtra(StartedService.EXTRA_INPUT, context.getString(R.string.hello_from_ui))
            }
            context.startService(intent)
        }) {
            Text(stringResource(R.string.start_service))
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Bound Service
        Text(stringResource(R.string.bound_service), style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        if (serviceBound) {
            val progress by boundService?.progress?.collectAsState() ?: mutableIntStateOf(0)

            Text(stringResource(R.string.progress, progress))
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { boundService?.startTask() }) {
                Text("Start Task")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                context.unbindService(connection)
                serviceBound = false
                boundService = null
            }) {
                Text(stringResource(R.string.unbind_service))
            }
        } else {
            Button(onClick = {
                val intent = Intent(context, BoundService::class.java)
                context.bindService(intent, connection, BIND_AUTO_CREATE)
            }) {
                Text(stringResource(R.string.bind_to_service))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    MyAppTheme {
        ServiceDemoApp()
    }
}
