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
package io.github.bonigarcia.android.settingsdemo;

import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends ParentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // Load preferences to display textView elements
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        // Name
        String name = prefs.getString("name", getResources().getString(R.string.not_defined));
        TextView textViewName = findViewById(R.id.textViewName);
        textViewName.setText(textViewName.getText() + ": " + name);

        // Role
        String role = prefs.getString("role", getResources().getString(R.string.not_defined));
        TextView textViewRole = findViewById(R.id.textViewRole);
        textViewRole.setText(textViewRole.getText() + ": " + role);

        // Preferred programming language
        TextView textViewLang = findViewById(R.id.textViewLang);
        String lang = textViewLang.getText() + ":";
        if (prefs.getBoolean("prog_java", false)) {
            lang += " " + getResources().getString(R.string.pref_java);
        }
        if (prefs.getBoolean("prog_python", false)) {
            lang += " " + getResources().getString(R.string.pref_python);
        }
        if (prefs.getBoolean("prog_c", false)) {
            lang += " " + getResources().getString(R.string.pref_c);
        }
        if (lang.endsWith(":")) {
            lang += " " + getResources().getString(R.string.not_defined);
        }
        textViewLang.setText(lang);
    }

}