/*
 * (C) Copyright 2024 Boni Garcia (https://bonigarcia.github.io/)
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
package es.uc3m.android.viewmodel;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class MainActivity extends AppCompatActivity {

    private CounterModel counterModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        counterModel =
                new ViewModelProvider(this).get(CounterModel.class);

        findViewById(R.id.btn_increment).setOnClickListener(view -> {
            counterModel.increment();
            displayCounter();
        });
        findViewById(R.id.btn_decrement).setOnClickListener(view -> {
            counterModel.decrement();
            displayCounter();
        });

        displayCounter();
    }

    private void displayCounter() {
        TextView counter = findViewById(R.id.tv_count);
        counterModel.getUiState().observe(this, counter::setText);
    }

}