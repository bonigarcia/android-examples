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
package es.uc3m.android.dialogs;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(view -> {
            EditText nameText = findViewById(R.id.editText);
            String name = nameText.getText().toString();

            if (name.isEmpty()) {
                AlertDialog.Builder ad = new AlertDialog.Builder(this);
                ad.setTitle(R.string.title_dialog);
                ad.setMessage(R.string.message_dialog);
                ad.setPositiveButton(R.string.button_ok,
                        (dialog, position) -> dialog.cancel());
                ad.show();
            } else {
                Intent intent = new Intent(this, SecondActivity.class);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });
    }

}