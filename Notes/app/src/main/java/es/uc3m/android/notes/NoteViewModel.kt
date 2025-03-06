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
package es.uc3m.android.notes

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class NoteViewModel : ViewModel() {
    private val _notes = mutableStateListOf<Note>()
    val notes: List<Note> get() = _notes

    private var nextId = 1 // Auto-increment ID

    fun addNote(title: String, body: String) {
        _notes.add(Note(id = nextId++, title = title, body = body))
    }

    fun updateNote(id: Int, title: String, body: String) {
        val noteIndex = _notes.indexOfFirst { it.id == id }
        if (noteIndex != -1) {
            _notes[noteIndex] = _notes[noteIndex].copy(title = title, body = body)
        }
    }

    fun deleteNote(id: Int) {
        _notes.removeAll { it.id == id }
    }
}