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

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        String name = getIntent().getExtras().getString("name");
        String text = String.format(getResources().getString(R.string.hello), name);
        TextView textView = findViewById(R.id.textView);
        textView.setText(text);

        // Click listener for buttons notifiers
        findViewById(R.id.btn_long_toast).setOnClickListener(this::showLongToast);
        findViewById(R.id.btn_short_toast).setOnClickListener(this::showShortToast);
        findViewById(R.id.btn_long_snack).setOnClickListener(this::showLongSnackbar);
        findViewById(R.id.btn_short_snack).setOnClickListener(this::showShortSnackbar);
        findViewById(R.id.btn_action_snack).setOnClickListener(this::showActionSnackbar);
    }

    public void datePicker(View view) {
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view1, year, monthOfYear, dayOfMonth) -> {
                    EditText txtDate = findViewById(R.id.in_date);
                    txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) +
                            "-" + year);
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public void timePicker(View view) {
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view1, hourOfDay, minute) -> {
                    EditText txtTime = findViewById(R.id.in_time);
                    txtTime.setText(hourOfDay + ":" + minute);
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }


    public void showLongToast(View view) {
        String message = getResources().getString(R.string.long_toast);
        Toast toast = Toast.makeText(getBaseContext(), message,
                Toast.LENGTH_LONG);
        toast.show();
    }

    public void showShortToast(View view) {
        String message = getResources().getString(R.string.short_toast);
        Toast toast = Toast.makeText(getBaseContext(), message,
                Toast.LENGTH_SHORT);
        toast.show();
    }


    public void showLongSnackbar(View view) {
        String message = getResources().getString(R.string.long_snackbar);
        Snackbar snackbar = Snackbar.make(view,
                message, Snackbar.LENGTH_LONG);

        snackbar.show();
    }

    public void showActionSnackbar(View view) {
        String message = getResources().getString(R.string.message_snackbar);
        Snackbar snackbar = Snackbar.make(view,
                message, Snackbar.LENGTH_INDEFINITE);
        String action = getResources().getString(R.string.action_snackbar);
        snackbar.setAction(action, view1 -> {
            Log.d(this.getLocalClassName(), "TODO");
        });

        snackbar.show();
    }

    public void showShortSnackbar(View view) {
        String message = getResources().getString(R.string.short_snackbar);
        Snackbar snackbar = Snackbar.make(view,
                message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }


}