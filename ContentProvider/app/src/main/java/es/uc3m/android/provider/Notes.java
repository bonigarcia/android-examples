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
package es.uc3m.android.provider;

import android.content.ContentValues;
import android.database.Cursor;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Notes {

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_BODY = "body";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_ID)
    public long id;

    @ColumnInfo(name = COLUMN_TITLE)
    public String title;

    @ColumnInfo(name = COLUMN_BODY)
    public String body;

    public Notes(long id, String title, String body) {
        this.id = id;
        this.title = title;
        this.body = body;
    }

    @Ignore
    public Notes(String title, String body) {
        this.title = title;
        this.body = body;
    }

    @Ignore
    public Notes() {
    }

    public static Notes fromContentValues(ContentValues values) {
        Notes notes = new Notes();
        if (values.containsKey(COLUMN_ID)) {
            notes.id = values.getAsLong(COLUMN_ID);
        }
        if (values.containsKey(COLUMN_TITLE)) {
            notes.title = values.getAsString(COLUMN_TITLE);
        }
        if (values.containsKey(COLUMN_BODY)) {
            notes.body = values.getAsString(COLUMN_BODY);
        }
        return notes;
    }

    public static List<Notes> fromCursor(Cursor cursor) {
        List<Notes> list = new ArrayList<>();

        while (cursor.moveToNext()) {
            Notes notes = new Notes();
            int id = cursor.getColumnIndex(COLUMN_ID);
            if (id != -1) {
                notes.id = cursor.getLong(id);
            }
            int title = cursor.getColumnIndex(COLUMN_TITLE);
            if (id != -1) {
                notes.title = cursor.getString(title);
            }
            int body = cursor.getColumnIndex(COLUMN_BODY);
            if (id != -1) {
                notes.body = cursor.getString(body);
            }
            list.add(notes);
        }

        return list;
    }

    @Override
    public String toString() {
        return title;
    }
}
