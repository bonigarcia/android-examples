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
package es.uc3m.android.notifications

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri

class NotificationHelper(private val context: Context) {
    companion object {
        const val STANDARD_CHANNEL_ID = "standard_channel"
        const val STANDARD_CHANNEL_NAME = "Standard Notifications"
        const val STANDARD_CHANNEL_DESCRIPTION = "Channel for standard status bar notifications"

        const val HEADS_UP_CHANNEL_ID = "heads_up_channel"
        const val HEADS_UP_CHANNEL_NAME = "Heads-up Notifications"
        const val HEADS_UP_CHANNEL_DESCRIPTION = "Channel for heads-up notifications"

        const val BADGE_CHANNEL_ID = "badge_channel"
        const val BADGE_CHANNEL_NAME = "Badge Notifications"
        const val BADGE_CHANNEL_DESCRIPTION = "Channel for app icon badge notifications"

        const val NOTIFICATION_ID = 1
    }

    init {
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Channel for standard notifications
            val standardChannel = NotificationChannel(
                STANDARD_CHANNEL_ID,
                STANDARD_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = STANDARD_CHANNEL_DESCRIPTION
            }

            // Channel for heads-up notifications
            val headsUpChannel = NotificationChannel(
                HEADS_UP_CHANNEL_ID,
                HEADS_UP_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH // Required for heads-up
            ).apply {
                description = HEADS_UP_CHANNEL_DESCRIPTION
            }

            // Channel for badge notifications
            val badgeChannel = NotificationChannel(
                BADGE_CHANNEL_ID,
                BADGE_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = BADGE_CHANNEL_DESCRIPTION
            }

            // Register the channels with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(standardChannel)
            notificationManager.createNotificationChannel(headsUpChannel)
            notificationManager.createNotificationChannel(badgeChannel)
        }
    }

    @SuppressLint("MissingPermission")
    fun statusBarNotification(title: String, content: String) {
        val builder = NotificationCompat.Builder(context, STANDARD_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            notify(NOTIFICATION_ID, builder.build())
        }
    }

    @SuppressLint("MissingPermission")
    fun headsUpNotification(title: String, content: String) {
        val builder = NotificationCompat.Builder(context, HEADS_UP_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setFullScreenIntent(null, true)
            .addAction(R.drawable.ic_launcher_foreground, "Start action", getPendingIntent())
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(NOTIFICATION_ID + 1, builder.build())
        }
    }

    @SuppressLint("MissingPermission")
    fun badgeNotification(title: String, content: String) {
        val notification = NotificationCompat.Builder(context, BADGE_CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(getPendingIntent())
            .setAutoCancel(true)
            .setNumber(5) // This makes the badge appear
            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
            .build()

        with(NotificationManagerCompat.from(context)) {
            notify(NOTIFICATION_ID + 2, notification)
        }
    }

    fun getPendingIntent(): PendingIntent {
        val intent = Intent(Intent.ACTION_DIAL, "tel:666555444".toUri())
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    }

}
