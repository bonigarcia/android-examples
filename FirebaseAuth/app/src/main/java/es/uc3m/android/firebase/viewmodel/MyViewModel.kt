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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import es.uc3m.android.firebase.NavGraph
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

private const val NOTES_COLLECTION = "notes"

class MyViewModel : ViewModel() {
    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> get() = _notes

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> get() = _toastMessage

    private val firestore = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = Firebase.auth

    private val _route = MutableStateFlow<String?>(null)
    val route: StateFlow<String?> get() = _route

    fun fetchNotes() {
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

    fun showToast(message: String?) {
        _toastMessage.value = message
    }

    fun navigate(route: String?) {
        _route.value = route
    }

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            try {
                auth.createUserWithEmailAndPassword(email, password).await()
                _route.value = NavGraph.Home.route

            } catch (e: Exception) {
                _toastMessage.value = e.message
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password).await()
                fetchNotes()
                _route.value = NavGraph.Home.route

            } catch (e: Exception) {
                _toastMessage.value = e.message
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                auth.signOut()
                _route.value = NavGraph.Login.route

            } catch (e: Exception) {
                _toastMessage.value = e.message
            }
        }
    }

}