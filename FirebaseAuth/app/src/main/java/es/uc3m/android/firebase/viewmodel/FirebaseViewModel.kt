/*
 * (C) Copyright 2026 Boni Garcia (https://bonigarcia.github.io/)
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

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import es.uc3m.android.firebase.screens.NavGraph
import es.uc3m.android.firebase.R
import es.uc3m.android.firebase.model.NOTES_COLLECTION
import es.uc3m.android.firebase.model.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlin.collections.mapNotNull

class FirebaseViewModel(application: Application) : AndroidViewModel(application) {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = Firebase.auth

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes.asStateFlow()

    private val _snackMessage = MutableStateFlow<String?>(null)
    val snackMessage: StateFlow<String?> = _snackMessage.asStateFlow()

    private val _route = MutableStateFlow<String?>(null)
    val route: StateFlow<String?> = _route.asStateFlow()

    private fun getString(resId: Int): String = getApplication<Application>().getString(resId)

    init {
        fetchNotes()
    }

    fun fetchNotes() {
        viewModelScope.launch {
            try {
                val snapshot = firestore.collection(NOTES_COLLECTION).get().await()
                val noteList = snapshot.documents.mapNotNull { doc ->
                    doc.toObject<Note>()?.copy(id = doc.id)
                }
                _notes.value = noteList
            } catch (e: Exception) {
                setSnackMessage(e.message ?: getString(R.string.read_error))
            }
        }
    }

    fun addNote(title: String, body: String) {
        viewModelScope.launch {
            try {
                val note = Note(title = title, body = body)
                firestore.collection(NOTES_COLLECTION).add(note).await()
                fetchNotes() // Update notes list
                setSnackMessage(getString(R.string.note_added)) // Update snack message
            } catch (e: Exception) {
                setSnackMessage(e.message ?: getString(R.string.write_error))
            }
        }
    }

    fun updateNote(id: String, title: String, body: String) {
        viewModelScope.launch {
            try {
                val updatedNote = Note(title = title, body = body)
                firestore.collection(NOTES_COLLECTION).document(id).set(updatedNote).await()
                fetchNotes() // Update notes list
                setSnackMessage(getString(R.string.note_updated)) // Update snack message
            } catch (e: Exception) {
                setSnackMessage(e.message ?: getString(R.string.update_error))
            }
        }
    }

    fun deleteNote(id: String) {
        viewModelScope.launch {
            try {
                firestore.collection(NOTES_COLLECTION).document(id).delete().await()
                fetchNotes() // Update notes list
                setSnackMessage(getString(R.string.note_deleted)) // Update snack message
            } catch (e: Exception) {
                setSnackMessage(e.message ?: getString(R.string.delete_error))
            }
        }
    }

    fun setSnackMessage(message: String?) {
        _snackMessage.value = message
    }

    fun navigateTo(route: String?) {
        _route.value = route
    }

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            try {
                auth.createUserWithEmailAndPassword(email, password).await()
                navigateTo(NavGraph.Home.route) // Go to home screen
                setSnackMessage(getString(R.string.sign_up_ok)) // Update snack message
            } catch (e: Exception) {
                setSnackMessage(e.message ?: getString(R.string.signup_error))
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password).await()
                fetchNotes()
                navigateTo(NavGraph.Home.route) // Go to home screen
                setSnackMessage(getString(R.string.login_ok)) // Update snack message
            } catch (e: Exception) {
                setSnackMessage(e.message ?: getString(R.string.login_error))
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                auth.signOut()
                navigateTo(NavGraph.Login.route) // Go to login screen
                setSnackMessage(getString(R.string.logout_ok)) // Update snack message
            } catch (e: Exception) {
                setSnackMessage(e.message ?: getString(R.string.logout_error))
            }
        }
    }

}