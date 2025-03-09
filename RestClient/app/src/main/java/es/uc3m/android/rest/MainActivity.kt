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
package es.uc3m.android.rest

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import es.uc3m.android.rest.dummyjson.Login
import es.uc3m.android.rest.dummyjson.Todo
import es.uc3m.android.rest.ui.theme.MyAppTheme
import es.uc3m.android.rest.viewmodel.RestViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyAppTheme {
                UserListScreen()
            }
        }
    }
}

@Composable
fun UserListScreen(viewModel: RestViewModel = viewModel()) {
    val context = LocalContext.current
    val todos by viewModel.todos.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    val toastMessage by viewModel.toastMessage.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.login))
            }
        }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                LazyColumn {
                    items(todos) { todo ->
                        TodoItem(todo = todo)
                    }
                }
            }
        }

        if (showDialog) {
            LoginDialog(onDismiss = { showDialog = false }, onConfirm = { login ->
                viewModel.login(login)
            })
        }

        LaunchedEffect(toastMessage) {
            toastMessage?.let { message ->
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            }
        }
    }
}

@Composable
fun TodoItem(todo: Todo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        val color = if (todo.completed) {
            MaterialTheme.colorScheme.outline
        } else {
            MaterialTheme.colorScheme.error
        }
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = todo.id.toString() + ". " + todo.todo,
                style = MaterialTheme.typography.bodyMedium,
                color = color
            )
        }
    }
}

@Composable
fun LoginDialog(
    onDismiss: () -> Unit, onConfirm: (Login) -> Unit
) {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    AlertDialog(onDismissRequest = onDismiss, confirmButton = {
        Button(onClick = {
            onConfirm(Login(login, password))
            onDismiss()
        }) {
            Text(stringResource(R.string.login))
        }
    }, dismissButton = {
        Button(onClick = onDismiss) {
            Text(stringResource(R.string.cancel))
        }
    }, title = { Text(stringResource(R.string.login)) }, text = {
        Column {
            TextField(
                value = login,
                onValueChange = { login = it },
                label = { Text(stringResource(R.string.login)) })
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(stringResource(R.string.password)) })
        }
    })
}