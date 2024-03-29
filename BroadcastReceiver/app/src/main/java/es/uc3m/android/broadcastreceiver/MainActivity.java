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
package es.uc3m.android.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String MY_BROADCAST_ACTION = "es.uc3m.android.sendbroadcast";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Receiver configuration
        IntentFilter filter = new IntentFilter();
        filter.addAction(MY_BROADCAST_ACTION);
        filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);  // "android.intent.action.AIRPLANE_MODE"
        filter.addAction(Intent.ACTION_BATTERY_LOW);  // "android.intent.action.BATTERY_LOW"
        BroadcastReceiver receiver = new MyReceiver();
        registerReceiver(receiver, filter);

        // Button listener
        findViewById(R.id.button).setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction(MY_BROADCAST_ACTION);
            sendBroadcast(intent);
        });

    }

}