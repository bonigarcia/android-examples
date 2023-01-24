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
package io.github.bonigarcia.android.settings;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AboutFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_about, container, false);

        // Load preferences to display textView elements
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(view.getContext());

        // Name
        String name = prefs.getString("name", getResources().getString(R.string.not_defined));
        TextView textViewName = view.findViewById(R.id.textViewName);
        textViewName.setText(textViewName.getText() + ": " + name);

        // Role
        String role = prefs.getString("role", getResources().getString(R.string.not_defined));
        TextView textViewRole = view.findViewById(R.id.textViewRole);
        textViewRole.setText(textViewRole.getText() + ": " + role);

        // Preferred programming language
        TextView textViewLang = view.findViewById(R.id.textViewLang);
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

        // Pro user
        boolean pro = prefs.getBoolean("pro_user", false);
        TextView textViewPro = view.findViewById(R.id.textViewPro);
        textViewPro.setText(textViewPro.getText() + ": " + pro);

        return view;
    }


}