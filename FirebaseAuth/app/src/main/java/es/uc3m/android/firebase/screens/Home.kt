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
package es.uc3m.android.firebase.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import es.uc3m.android.firebase.AuthViewModel
import es.uc3m.android.firebase.Note
import es.uc3m.android.firebase.NoteViewModel
import es.uc3m.android.firebase.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    noteViewModel: NoteViewModel = viewModel()
) {
    var showAddNoteDialog by remember { mutableStateOf(false) }
    var noteToEdit by remember { mutableStateOf<Note?>(null) }
    val notes by noteViewModel.notes.collectAsState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                actions = {
                    IconButton(onClick = {
                        scope.launch {
                            authViewModel.logout(context, navController)
                        }
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_logout_24),
                            contentDescription = stringResource(R.string.logout)
                        )
                    }
                })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddNoteDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_note))
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            LazyColumn {
                items(notes) { note ->
                    NoteItem(
                        note = note,
                        onNoteClick = { noteToEdit = it },
                        onDeleteClick = { noteViewModel.deleteNote(it.id!!) }
                    )
                }
            }
        }
    }

    if (showAddNoteDialog) {
        AddNoteDialog(
            onDismiss = { showAddNoteDialog = false },
            onAddNote = { title, body ->
                noteViewModel.addNote(title, body)
                showAddNoteDialog = false
            }
        )
    }

    noteToEdit?.let { note ->
        EditNoteDialog(
            note = note,
            onDismiss = { noteToEdit = null },
            onUpdateNote = { title, body ->
                noteViewModel.updateNote(note.id!!, title, body)
                noteToEdit = null
            }
        )
    }
}

@Composable
fun NoteItem(
    note: Note,
    onNoteClick: (Note) -> Unit,
    onDeleteClick: (Note) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onNoteClick(note) }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = note.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = note.body, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { onDeleteClick(note) }) {
                Text(stringResource(R.string.delete))
            }
        }
    }
}

@Composable
fun AddNoteDialog(
    onDismiss: () -> Unit,
    onAddNote: (String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                if (title.isNotBlank() && body.isNotBlank()) {
                    onAddNote(title, body)
                }
            }) {
                Text(stringResource(R.string.add))
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        },
        title = { Text(stringResource(R.string.add_note)) },
        text = {
            Column {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(stringResource(R.string.title)) }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = body,
                    onValueChange = { body = it },
                    label = { Text(stringResource(R.string.body)) }
                )
            }
        }
    )
}

@Composable
fun EditNoteDialog(
    note: Note,
    onDismiss: () -> Unit,
    onUpdateNote: (String, String) -> Unit
) {
    var title by remember { mutableStateOf(note.title) }
    var body by remember { mutableStateOf(note.body) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                if (title.isNotBlank() && body.isNotBlank()) {
                    onUpdateNote(title, body)
                }
            }) {
                Text(stringResource(R.string.update))
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        },
        title = { Text(stringResource(R.string.edit_note)) },
        text = {
            Column {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(stringResource(R.string.title)) }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = body,
                    onValueChange = { body = it },
                    label = { Text(stringResource(R.string.body)) }
                )
            }
        }
    )
}