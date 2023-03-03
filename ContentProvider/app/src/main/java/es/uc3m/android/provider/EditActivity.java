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
package es.uc3m.android.provider;

import static es.uc3m.android.provider.MyProvider.CONTENT_URI;
import static es.uc3m.android.provider.Notes.COLUMN_BODY;
import static es.uc3m.android.provider.Notes.COLUMN_ID;
import static es.uc3m.android.provider.Notes.COLUMN_TITLE;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditActivity extends AppCompatActivity {

    private EditText titleText;
    private EditText bodyText;
    private Long mRowId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        titleText = findViewById(R.id.title);
        bodyText = findViewById(R.id.body);

        Button confirm = findViewById(R.id.confirm);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mRowId = extras.getLong("id");

            try (Cursor cursor = getContentResolver().query(CONTENT_URI, null,
                    String.valueOf(mRowId), null, null)) {
                Notes notes = Notes.fromCursor(cursor).get(0);
                titleText.setText(notes.title);
                bodyText.setText(notes.body);
            }
            confirm.setText(R.string.update);

        } else {
            confirm.setText(R.string.add);
            findViewById(R.id.delete).setVisibility(View.GONE);
        }
    }

    public void saveNote(View view) {
        String title = titleText.getText().toString();
        String body = bodyText.getText().toString();

        if (mRowId == null) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_TITLE, title);
            values.put(COLUMN_BODY, body);

            Uri insert = getContentResolver().insert(CONTENT_URI, values);
            Toast.makeText(getApplicationContext(), "Created note " + insert,
                    Toast.LENGTH_LONG).show();

        } else {
            ContentValues values = new ContentValues();
            values.put(COLUMN_ID, mRowId);
            values.put(COLUMN_TITLE, title);
            values.put(COLUMN_BODY, body);

            getContentResolver().update(CONTENT_URI, values, null, null);
            Toast.makeText(getApplicationContext(), "Updated note with id " + mRowId,
                    Toast.LENGTH_LONG).show();
        }
        setResult(RESULT_OK);
        finish();
    }

    public void deleteNote(View view) {
        getContentResolver().delete(CONTENT_URI, String.valueOf(mRowId), null);
        Toast.makeText(getApplicationContext(), "Deleted note with id " + mRowId,
                Toast.LENGTH_LONG).show();

        setResult(RESULT_OK);
        finish();
    }

}