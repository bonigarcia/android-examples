/*
 * (C) Copyright 2022 Boni Garcia (https://bonigarcia.github.io/)
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
package es.uc3m.android.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID_1 = "es.uc3m.android.notifications.notify_001";
    private static final String CHANNEL_NAME_1 = "My notification channel 1";
    private static final String CHANNEL_ID_2 = "es.uc3m.android.notifications.notify_002";
    private static final String CHANNEL_NAME_2 = "My notification channel 2";
    private static final String CHANNEL_ID_3 = "es.uc3m.android.notifications.notify_003";
    private static final String CHANNEL_NAME_3 = "My notification channel 3";

    private NotificationManager notificationManager;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.start_notification_1).setOnClickListener(this::simpleNotification);
        findViewById(R.id.stop_notification_1).setOnClickListener(view -> stopNotification(0));

        findViewById(R.id.start_notification_2).setOnClickListener(this::expandedNotification);
        findViewById(R.id.stop_notification_2).setOnClickListener(view -> stopNotification(1));

        findViewById(R.id.start_notification_3).setOnClickListener(this::actionNotification);
        findViewById(R.id.stop_notification_3).setOnClickListener(view -> stopNotification(2));
    }

    private void simpleNotification(View view) {
        Context context = view.getContext();
        notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID_1);
        builder.setContentTitle("Notification #1");
        builder.setContentText("This is the first notification");
        builder.setSmallIcon(R.drawable.ic_android_black_24dp);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID_1, CHANNEL_NAME_1,
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(CHANNEL_ID_1);
        }

        int notificationId = 0;
        notificationManager.notify(notificationId, builder.build());
    }


    private void expandedNotification(View view) {
        Context context = view.getContext();
        notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID_2);
        builder.setContentTitle("Notification #2");
        builder.setSmallIcon(R.drawable.baseline_account_box_24);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(
                "You have received an email from John Doe"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID_2, CHANNEL_NAME_2,
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(CHANNEL_ID_2);
        }

        int notificationId = 1;
        notificationManager.notify(notificationId, builder.build());
    }

    private void actionNotification(View view) {
        Context context = view.getContext();
        notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:666555444"));
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID_3);
        builder.setContentTitle("Notification #3");
        builder.setContentText("This is the third notification");
        builder.setSmallIcon(R.drawable.baseline_dangerous_24);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.addAction(R.drawable.baseline_access_time_24, "Start action", pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID_3, CHANNEL_NAME_3,
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(CHANNEL_ID_3);
        }

        int notificationId = 2;
        notificationManager.notify(notificationId, builder.build());
    }

    private void stopNotification(int notificationId) {
        notificationManager.cancel(notificationId);
    }

}