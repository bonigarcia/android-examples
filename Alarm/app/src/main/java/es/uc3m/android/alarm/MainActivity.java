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
package es.uc3m.android.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.start_alarm_1).setOnClickListener(this::oneTimeAlarm);
        findViewById(R.id.start_alarm_2).setOnClickListener(this::repeatingAlarm);
        findViewById(R.id.stop_alarm_2).setOnClickListener(this::cancelRepeatingAlarm);
    }

    private void oneTimeAlarm(View view) {
        // Get alarm manager
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // Create pending intent
        Context context = view.getContext();
        Intent intent = new Intent(context, SecondActivity.class);
        intent.putExtra("name", "John Doe");
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        // Set alarm
        int alarmType = AlarmManager.ELAPSED_REALTIME_WAKEUP;
        long triggerAtMillis = SystemClock.elapsedRealtime() + TimeUnit.SECONDS.toMillis(5);
        alarmManager.set(alarmType, triggerAtMillis, pendingIntent);
    }

    private void repeatingAlarm(View view) {
        // Get alarm manager
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // Create pending intent
        Context context = view.getContext();
        Intent intent = new Intent(context, PeriodicService.class);
        pendingIntent =
                PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Set alarm
        int alarmType = AlarmManager.RTC_WAKEUP;
        long triggerAtMillis = SystemClock.elapsedRealtime();
        long intervalMillis = TimeUnit.SECONDS.toMillis(10);
        System.out.println("************** " + alarmManager.getClass());
        alarmManager.setRepeating(alarmType, triggerAtMillis, intervalMillis, pendingIntent);
    }

    private void cancelRepeatingAlarm(View view) {
        if (pendingIntent != null) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
        }
    }

}