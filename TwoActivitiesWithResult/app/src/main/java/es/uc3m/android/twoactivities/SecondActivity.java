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
package es.uc3m.android.twoactivities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        String name = getIntent().getExtras().getString("name");
        String text = String.format(getResources().getString(R.string.hello), name);

        TextView textView = findViewById(R.id.textView);
        textView.setText(text);

        Button okButton = findViewById(R.id.ok_button);
        okButton.setOnClickListener(view -> {
            Intent result = new Intent();
            result.putExtra("message",
                    getResources().getString(R.string.ok_message));
            setResult(RESULT_OK, result);
            finish();
        });

        Button cancelButton = findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(view -> {
            Intent result = new Intent();
            result.putExtra("message",
                    getResources().getString(R.string.cancel_message));
            setResult(RESULT_CANCELED, result);
            finish();
        });
    }

}