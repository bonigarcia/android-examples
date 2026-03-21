/*
 * (C) Copyright 2026 Boni Garcia (https://bonigarcia.github.io/)
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
package es.uc3m.android.workmanager

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import java.util.UUID
import java.util.concurrent.TimeUnit

private const val PERIODIC_WORK_NAME = "demo_periodic_work"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MaterialTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    WorkManagerDemoScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun WorkManagerDemoScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val workManager = remember { WorkManager.getInstance(context) }

    var oneTimeWorkId by remember { mutableStateOf<UUID?>(null) }
    val oneTimeFlow: Flow<WorkInfo?> = remember(oneTimeWorkId) {
        oneTimeWorkId?.let { id ->
            workManager.getWorkInfoByIdFlow(id)
        } ?: emptyFlow()
    }
    val oneTimeInfo by oneTimeFlow.collectAsState(initial = null)
    val oneTimeStatus = oneTimeInfo?.state?.name ?: stringResource(R.string.not_scheduled)

    val periodicFlow = remember {
        workManager.getWorkInfosForUniqueWorkFlow(PERIODIC_WORK_NAME)
    }
    val periodicInfos by periodicFlow.collectAsState(initial = emptyList())
    val periodicStatus =
        periodicInfos.firstOrNull()?.state?.name ?: stringResource(R.string.not_scheduled)

    val oneTimeMsg = stringResource(R.string.one_time)
    val periodicMsg = stringResource(R.string.periodic_msg)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp), verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(R.string.one_time_work, oneTimeStatus),
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = {
            val request = OneTimeWorkRequestBuilder<MyWorker>().setInputData(
                Data.Builder().putString(MyWorker.KEY_MESSAGE, oneTimeMsg).build()
            ).build()

            oneTimeWorkId = request.id
            workManager.enqueue(request)
        }) {
            Text(stringResource(R.string.start_one_time_work))
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(R.string.periodic_work, periodicStatus),
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = {
            val request = PeriodicWorkRequestBuilder<MyWorker>(
                15, TimeUnit.MINUTES
            ).setInputData(
                Data.Builder().putString(
                    MyWorker.KEY_MESSAGE, periodicMsg
                ).build()
            ).build()
            workManager.enqueueUniquePeriodicWork(
                PERIODIC_WORK_NAME, ExistingPeriodicWorkPolicy.UPDATE, request
            )
        }) {
            Text(stringResource(R.string.start_periodic_work))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            workManager.cancelUniqueWork(PERIODIC_WORK_NAME)
        }) {
            Text(stringResource(R.string.stop_periodic_work))
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(R.string.check_logcat, MyWorker.TAG),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}