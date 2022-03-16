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
package io.github.bonigarcia.android.alarm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.EditText;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                Context context = view.getContext();

                AlarmManager alarms = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                int alarmType = AlarmManager.ELAPSED_REALTIME_WAKEUP;
                long timeMillis = TimeUnit.SECONDS.toMillis(5);

                Intent intent = new Intent(context, SecondActivity.class);
                intent.putExtra("name", "John Doe");
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                        PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                alarms.set(alarmType, SystemClock.elapsedRealtime() + timeMillis, pendingIntent);
            }
        });
    }

}