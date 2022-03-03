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
package io.github.bonigarcia.android.databasedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {

    private NotesAdapter dbAdapter;

    private EditText titleText;
    private EditText bodyText;
    private Long mRowId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        dbAdapter = new NotesAdapter(this);
        dbAdapter.open();

        titleText = findViewById(R.id.title);
        bodyText = findViewById(R.id.body);

        mRowId = (savedInstanceState == null) ? null :
                (Long) savedInstanceState.getSerializable(NotesAdapter.KEY_ROWID);
        if (mRowId == null) {
            Bundle extras = getIntent().getExtras();
            mRowId = extras != null ? extras.getLong(NotesAdapter.KEY_ROWID) : null;
        }

        if (mRowId != null) {
            Cursor note = dbAdapter.fetchNote(mRowId);
            titleText.setText(note.getString(
                    note.getColumnIndexOrThrow(NotesAdapter.KEY_TITLE)));
            bodyText.setText(note.getString(
                    note.getColumnIndexOrThrow(NotesAdapter.KEY_BODY)));
        }
    }

    public void saveNote(View view) {
        String title = titleText.getText().toString();
        String body = bodyText.getText().toString();

        if (mRowId == null) {
            long id = dbAdapter.createNote(title, body);
            if (id > 0) {
                mRowId = id;
            }
        } else {
            dbAdapter.updateNote(mRowId, title, body);
        }
        setResult(RESULT_OK);
        dbAdapter.close();
        finish();
    }
}