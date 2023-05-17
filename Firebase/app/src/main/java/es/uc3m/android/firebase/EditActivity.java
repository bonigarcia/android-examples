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
package es.uc3m.android.firebase;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private EditText titleText;
    private EditText bodyText;
    private String mRowId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        db = FirebaseFirestore.getInstance();

        titleText = findViewById(R.id.title);
        bodyText = findViewById(R.id.body);

        Button confirm = findViewById(R.id.confirm);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mRowId = extras.getString("id");
            db.collection("notes").document(mRowId).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            titleText.setText(task.getResult().getData().get("title").toString());
                            bodyText.setText(task.getResult().getData().get("body").toString());
                            confirm.setText(R.string.update);
                        } else {
                            Log.e(this.getLocalClassName(), "Error getting notes",
                                    task.getException());
                        }
                    });

        } else {
            confirm.setText(R.string.add);
            findViewById(R.id.delete).setVisibility(View.GONE);
        }
    }

    public void saveNote(View view) {
        String title = titleText.getText().toString();
        String body = bodyText.getText().toString();

        if (mRowId == null) {
            Map<String, Object> note = new HashMap<>();
            note.put("title", title);
            note.put("body", body);
            db.collection("notes").document().set(note);

            Toast.makeText(getApplicationContext(), "Stored note in Firebase",
                    Toast.LENGTH_LONG).show();

        } else {
            Map<String, Object> note = new HashMap<>();
            note.put("title", title);
            note.put("body", body);
            db.collection("notes").document(mRowId).update(note);

            Toast.makeText(getApplicationContext(), "Updated note in Firebase",
                    Toast.LENGTH_LONG).show();
        }
        setResult(RESULT_OK);
        finish();
    }

    public void deleteNote(View view) {
        db.collection("notes").document(mRowId).delete();
        Toast.makeText(getApplicationContext(), "Deleted note from Firebase",
                Toast.LENGTH_LONG).show();

        setResult(RESULT_OK);
        finish();
    }

}