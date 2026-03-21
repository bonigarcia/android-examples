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
import android.os.Build

class AlarmsHelper(private val context: Context, private val alarmManager: AlarmManager) {
    fun setOneTimeAlarm() {
        // Set the alarm to trigger 10 seconds from now
        val triggerTime = System.currentTimeMillis() + 10_000
        val pendingIntent = getPendingIntent(
            context.getString(R.string.one_time_alarm_msg), ONE_TIME_ALARM_REQUEST_CODE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            // Fallback to inexact alarm if exact permission is not granted
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent
            )
        } else {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent
            )
        }
    }

    fun setRepeatingAlarm() {
        // Set the alarm to start approximately 10 seconds from now and repeat every minute
        val triggerTime = System.currentTimeMillis() + 10_000
        val repeatInterval = 60_000L // 1 minute in milliseconds
        val pendingIntent = getPendingIntent(
            context.getString(R.string.repeating_alarm_msg), REPEATING_ALARM_REQUEST_CODE
        )

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP, triggerTime, repeatInterval, pendingIntent
        )
    }

    fun cancelRepeatingAlarm() {
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REPEATING_ALARM_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
    }

    private fun getPendingIntent(message: String, requestCode: Int): PendingIntent {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(MSG_KEY, message)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        return pendingIntent
    }
}