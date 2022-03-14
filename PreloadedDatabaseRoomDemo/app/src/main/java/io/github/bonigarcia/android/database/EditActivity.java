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
package io.github.bonigarcia.android.database;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

public class EditActivity extends AppCompatActivity {

    private static final String DATABASE_NAME = "my-notes";

    private AppDatabase db;

    private EditText titleText;
    private EditText bodyText;
    private Long mRowId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                .allowMainThreadQueries()
                .build();

        titleText = findViewById(R.id.title);
        bodyText = findViewById(R.id.body);

        Bundle extras = getIntent().getExtras();
        mRowId = extras != null ? extras.getLong("id") : null;
        if (mRowId != null) {
            Note note = db.notesDao().findById(mRowId);
            titleText.setText(note.getTitle());
            bodyText.setText(note.getBody());
        }

    }

    public void saveNote(View view) {
        String title = titleText.getText().toString();
        String body = bodyText.getText().toString();

        if (mRowId == null) {
            Note note = new Note(title, body);
            long id = db.notesDao().insert(note);
            if (id > 0) {
                mRowId = id;
            }
        } else {
            Note note = new Note(mRowId, title, body);
            db.notesDao().update(note);
        }
        setResult(RESULT_OK);
        finish();
    }
}