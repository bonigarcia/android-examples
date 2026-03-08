/*
 * (C) Copyright 2025 Boni Garcia (https://bonigarcia.github.io/)
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
package es.uc3m.android.room.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import es.uc3m.android.room.model.Note
import es.uc3m.android.room.model.NoteDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MyViewModel(application: Application) : AndroidViewModel(application) {
    private val _notesDao = NoteDatabase.getDatabase(application).noteDao()
    val notes: Flow<List<Note>> = _notesDao.getAllNotes()

    fun addNote(title: String, body: String) {
        viewModelScope.launch {
            val note = Note(title = title, body = body)
            _notesDao.insert(note)
        }
    }

    fun updateNote(id: Int, title: String, body: String) {
        viewModelScope.launch {
            val note = Note(id = id, title = title, body = body)
            _notesDao.update(note)
        }
    }

    fun deleteNote(id: Int) {
        viewModelScope.launch {
            _notesDao.delete(id)
        }
    }
}