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

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppDatabase db;

    private ListView listView;

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> displayList());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = AppDatabase.getInstance(getApplicationContext());

        listView = findViewById(R.id.list_view);
        listView.setOnItemClickListener((adapter, view, pos, id) -> {
            Notes notes = (Notes) adapter.getAdapter().getItem(pos);

            Intent intent = new Intent(view.getContext(), EditActivity.class);
            intent.putExtra("id", notes.id);
            activityResultLauncher.launch(intent);
        });

        displayList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_insert) {
            insertNote();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void insertNote() {
        Intent intent = new Intent(this, EditActivity.class);
        activityResultLauncher.launch(intent);
    }

    private void displayList() {
        List<Notes> notesList = db.notesDao().selectAll();
        ArrayAdapter<Notes> listAdapter = new ArrayAdapter<>(listView.getContext(),
                android.R.layout.simple_list_item_1, notesList);
        listView.setAdapter(listAdapter);
    }

}