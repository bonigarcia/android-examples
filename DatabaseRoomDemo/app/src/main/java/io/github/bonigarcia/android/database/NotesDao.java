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

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NotesDao {

    @Query("SELECT * FROM note")
    List<Note> getAllNotes();

    @Query("SELECT * FROM note WHERE id IN (:notesIds)")
    List<Note> loadAllByIds(long[] notesIds);

    @Query("SELECT * FROM note WHERE id=:id")
    Note findById(long id);

    @Query("SELECT * FROM note WHERE title LIKE :title LIMIT 1")
    Note findByTitle(String title);

    @Insert
    long insert(Note note);

    @Insert
    long[] insertAll(Note... notes);

    @Update
    public void update(Note... notes);

    @Delete
    void delete(Note note);
}
