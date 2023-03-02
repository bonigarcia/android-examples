/*
 * (C) Copyright 2023 Boni Garcia (https://bonigarcia.github.io/)
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
package es.uc3m.android.savedstate;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String COUNT_KEY = "COUNTER";
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_increment).setOnClickListener(view -> {
            count++;
            displayCounter();
        });
        findViewById(R.id.btn_decrement).setOnClickListener(view -> {
            count--;
            displayCounter();
        });

        if (savedInstanceState != null) {
            count = savedInstanceState.getInt(COUNT_KEY, 0);
        }

        displayCounter();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(COUNT_KEY, count);
    }

    private void displayCounter() {
        TextView counter = findViewById(R.id.tv_count);
        counter.setText(String.valueOf(count));
    }

    // Alternative method:
    // @Override
    //protected void onRestoreInstanceState(Bundle savedInstanceState) {
    //    super.onRestoreInstanceState(savedInstanceState);
    //    count = savedInstanceState.getInt(COUNT_KEY, 0);
    //    displayCounter();
    //}
}