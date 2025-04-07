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
package es.uc3m.android.alarms

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import es.uc3m.android.alarms.ui.theme.MyAppTheme

const val MSG_KEY = "MESSAGE"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AlarmApp(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun AlarmApp(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val alarmState = remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                setOneTimeAlarm(context, alarmManager)
                alarmState.value = context.getString(R.string.one_time_alarm_note)
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text(stringResource(R.string.one_time_alarm_set))
        }

        Button(
            onClick = {
                ->
                setRepeatingAlarm(context, alarmManager)
                alarmState.value = context.getString(R.string.repeating_alarm_note)
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text(stringResource(R.string.repeating_alarm_set))
        }

        Button(
            onClick = {
                cancelRepeatingAlarm(context, alarmManager)
                alarmState.value = context.getString(R.string.repeating_alarm_canceled_note)
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text(stringResource(R.string.repeating_alarm_canceled))
        }

        Text(
            text = alarmState.value,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 24.dp)
        )
    }
}

private fun setOneTimeAlarm(context: Context, alarmManager: AlarmManager) {
    val intent = Intent(context, AlarmReceiver::class.java).apply {
        putExtra(MSG_KEY, context.getString(R.string.one_time_alarm_msg))
    }

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        0, // Different request code for one-time alarm
        intent,
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )

    // Set the alarm to trigger 10 seconds from now
    val triggerTime = System.currentTimeMillis() + 10_000

    alarmManager.setExactAndAllowWhileIdle(
        AlarmManager.RTC_WAKEUP,
        triggerTime,
        pendingIntent
    )
}

private fun setRepeatingAlarm(context: Context, alarmManager: AlarmManager) {
    val intent = Intent(context, AlarmReceiver::class.java).apply {
        putExtra(MSG_KEY, context.getString(R.string.repeating_alarm_msg))
    }

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        AlarmReceiver.REPEATING_ALARM_REQUEST_CODE, // Same request code for the repeating alarm
        intent,
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )

    // Set the alarm to start approximately 10 seconds from now and repeat every minute
    val triggerTime = System.currentTimeMillis() + 10_000
    val repeatInterval = 60_000L // 1 minute in milliseconds

    alarmManager.setRepeating(
        AlarmManager.RTC_WAKEUP,
        triggerTime,
        repeatInterval,
        pendingIntent
    )
}

private fun cancelRepeatingAlarm(context: Context, alarmManager: AlarmManager) {
    val intent = Intent(context, AlarmReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        AlarmReceiver.REPEATING_ALARM_REQUEST_CODE,
        intent,
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )

    alarmManager.cancel(pendingIntent)
    pendingIntent.cancel()
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    MyAppTheme {
        AlarmApp()
    }
}
