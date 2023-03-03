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
package es.uc3m.android.database;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditActivity extends AppCompatActivity {

    private AppDatabase db;
    private EditText titleText;
    private EditText bodyText;
    private Long mRowId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        db = AppDatabase.getInstance(getApplicationContext());

        titleText = findViewById(R.id.title);
        bodyText = findViewById(R.id.body);

        Button confirm = findViewById(R.id.confirm);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mRowId = extras.getLong("id");
            Notes note = db.notesDao().selectById(mRowId);
            titleText.setText(note.title);
            bodyText.setText(note.body);

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
            Notes note = new Notes(title, body);
            long id = db.notesDao().insert(note);
            Toast.makeText(getApplicationContext(), "Created note with id " + id,
                    Toast.LENGTH_LONG).show();

        } else {
            Notes note = new Notes(mRowId, title, body);
            db.notesDao().update(note);
            Toast.makeText(getApplicationContext(), "Updated note with id " + mRowId,
                    Toast.LENGTH_LONG).show();
        }
        setResult(RESULT_OK);
        finish();
    }

    public void deleteNote(View view) {
        String title = titleText.getText().toString();
        String body = bodyText.getText().toString();
        Notes note = new Notes(mRowId, title, body);

        int delete = db.notesDao().delete(note);
        Toast.makeText(getApplicationContext(), "Deleted note with id " + delete,
                Toast.LENGTH_LONG).show();

        setResult(RESULT_OK);
        finish();
    }

}