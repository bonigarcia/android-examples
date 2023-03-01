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
package es.uc3m.android.preloaded;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NotesDao {

    @Query("SELECT * FROM Notes")
    List<Notes> getAllNotes();

    @Query("SELECT * FROM Notes WHERE id IN (:notesIds)")
    List<Notes> loadAllByIds(long[] notesIds);

    @Query("SELECT * FROM Notes WHERE id=:id")
    Notes findById(long id);

    @Query("SELECT * FROM Notes WHERE title LIKE :title LIMIT 1")
    Notes findByTitle(String title);

    @Insert
    long insert(Notes notes);

    @Insert
    long[] insertAll(Notes... notes);

    @Update
    public void update(Notes... notes);

    @Delete
    void delete(Notes notes);

}