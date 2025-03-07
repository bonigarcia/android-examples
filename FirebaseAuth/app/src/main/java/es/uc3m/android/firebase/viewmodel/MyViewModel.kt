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
package es.uc3m.android.firebase.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

private const val NOTES_COLLECTION = "notes"

class MyViewModel : ViewModel() {
    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> get() = _notes

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = _toastMessage

    private val firestore = FirebaseFirestore.getInstance()

    init {
        fetchNotes()
    }

    private fun fetchNotes() {
        viewModelScope.launch {
            firestore.collection(NOTES_COLLECTION).get()
                .addOnSuccessListener { result ->
                    val noteList = result.map { document ->
                        document.toObject<Note>().copy(id = document.id)
                    }
                    _notes.value = noteList
                }
                .addOnFailureListener { exception ->
                    _toastMessage.value = exception.message
                }
        }
    }

    fun addNote(title: String, body: String) {
        viewModelScope.launch {
            val note = Note(title = title, body = body)
            firestore.collection(NOTES_COLLECTION)
                .add(note)
                .addOnSuccessListener {
                    fetchNotes() // Refresh the list after adding
                }
                .addOnFailureListener { exception ->
                    _toastMessage.value = exception.message
                }
        }
    }

    fun updateNote(id: String, title: String, body: String) {
        viewModelScope.launch {
            val updatedNote = Note(title = title, body = body)
            firestore.collection(NOTES_COLLECTION).document(id)
                .set(updatedNote)
                .addOnSuccessListener {
                    fetchNotes() // Refresh the list after updating
                }
                .addOnFailureListener { exception ->
                    _toastMessage.value = exception.message
                }
        }
    }

    fun deleteNote(id: String) {
        viewModelScope.launch {
            firestore.collection(NOTES_COLLECTION).document(id)
                .delete()
                .addOnSuccessListener {
                    fetchNotes() // Refresh the list after deleting
                }
                .addOnFailureListener { exception ->
                    _toastMessage.value = exception.message
                }
        }
    }
}