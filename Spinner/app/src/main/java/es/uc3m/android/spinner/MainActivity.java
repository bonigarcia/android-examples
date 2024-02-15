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
package es.uc3m.android.spinner;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Static spinner (content from resources)
        Spinner staticDropdown = findViewById(R.id.static_spinner);
        ArrayAdapter<CharSequence> staticAdapter =
                ArrayAdapter.createFromResource(this, R.array.planets_array,
                        android.R.layout.simple_spinner_dropdown_item
                );
        staticDropdown.setAdapter(staticAdapter);
        staticDropdown.setSelection(0,
                false); // this line avoids calling the listener for the default value
        staticDropdown.setOnItemSelectedListener(new SpinnerListener());

        // Dynamic spinner (content generated with Java logic)
        Spinner dynamicDropdown = findViewById(R.id.dynamic_spinner);
        List<String> randomContent = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            randomContent.add(UUID.randomUUID().toString());
        }
        ArrayAdapter<String> dynamicAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                        randomContent);
        dynamicDropdown.setAdapter(dynamicAdapter);
        dynamicDropdown.setSelection(0, false);
        dynamicDropdown.setOnItemSelectedListener(new SpinnerListener());
    }

}