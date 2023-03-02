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
package es.uc3m.android.foreign;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class EditActivity extends AppCompatActivity {

    private AppDatabase db;
    private EditText titleText;
    private EditText bodyText;

    private Spinner dropdown;
    private Long mRowId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        db = AppDatabase.getInstance(getApplicationContext());

        titleText = findViewById(R.id.title);
        bodyText = findViewById(R.id.body);
        dropdown = findViewById(R.id.spinner);

        List<String> items = new ArrayList<>();
        for (Category category : db.categoryDao().getAllCategories()) {
            items.add(category.getName());
        }
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mRowId = extras.getLong("id");
            Notes note = db.notesDao().findById(mRowId);
            titleText.setText(note.getTitle());
            bodyText.setText(note.getBody());
            dropdown.setSelection((int) note.getCategory() - 1);
        }
    }

    public void saveNote(View view) {
        String title = titleText.getText().toString();
        String body = bodyText.getText().toString();
        long category = dropdown.getSelectedItemPosition() + 1;

        if (mRowId == null) {
            Notes notes = new Notes(title, body, category);
            long id = db.notesDao().insert(notes);
            Toast.makeText(getApplicationContext(), "Created note with id " + id,
                    Toast.LENGTH_LONG).show();

        } else {
            Notes notes = new Notes(mRowId, title, body, category);
            db.notesDao().update(notes);

            Toast.makeText(getApplicationContext(), "Updated note with id " + mRowId,
                    Toast.LENGTH_LONG).show();
        }
        setResult(RESULT_OK);
        finish();
    }

}