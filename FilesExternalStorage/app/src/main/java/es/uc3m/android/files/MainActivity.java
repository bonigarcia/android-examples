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
package es.uc3m.android.files;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileWriter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View button = findViewById(R.id.button);
        button.setOnClickListener(view -> {
            writeFile();
            startNextActivity();
        });
    }

    private void writeFile() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            EditText nameText = findViewById(R.id.editText);
            String name = nameText.getText().toString();

            File file = new File(getExternalFilesDir(null), "myfile.txt");
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(name);
            } catch (Exception e) {
                Log.e(this.getLocalClassName(), "Exception writing file", e);
            }

        } else {
            Log.d(this.getLocalClassName(), "External storage not mounted");
        }
    }

    private void startNextActivity() {
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }
}