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
package es.uc3m.android.test

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import es.uc3m.android.test.dummyjson.Recipe
import es.uc3m.android.test.dummyjson.Todo
import es.uc3m.android.test.ui.theme.MyAppTheme
import es.uc3m.android.test.viewmodel.RestViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyAppTheme {
                TodosScreen()
            }
        }
    }
}

@Composable
fun TodosScreen(viewModel: RestViewModel = viewModel()) {
    val context = LocalContext.current
    val todos by viewModel.todos.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    val toastMessage by viewModel.toastMessage.collectAsState()
    var isHome by remember { mutableStateOf(true) }
    var fetching by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add))
            }
        }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (isHome) {
                Button(onClick = {
                    isHome = false
                }) {
                    Text(stringResource(R.string.get_todos))
                }
            } else if (!fetching) {
                viewModel.fetchTodos()
                fetching = true
            } else if (isLoading) {
                CircularProgressIndicator()
            } else {
                Text(
                    style = MaterialTheme.typography.headlineSmall,
                    text = stringResource(R.string.my_todos)
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn(
                    modifier = Modifier.testTag("todos")
                ) {
                    items(todos) { todo ->
                        TodoItem(todo = todo)
                    }
                }
            }
        }
    }

    if (showDialog) {
        AddRecipeDialog(onDismiss = { showDialog = false }, onConfirm = { recipe ->
            viewModel.addRecipe(recipe)
        })
    }

    LaunchedEffect(toastMessage) {
        toastMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            // Reset message to avoid blocking further messages
            viewModel.showToast(null)
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
fun AddRecipeDialog(
    onDismiss: () -> Unit, onConfirm: (Recipe) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var ingredients by remember { mutableStateOf("") }

    AlertDialog(onDismissRequest = onDismiss, confirmButton = {
        Button(onClick = {
            onConfirm(Recipe(name = name, ingredients = ingredients))
            onDismiss()
        }) {
            Text(stringResource(R.string.accept))
        }
    }, dismissButton = {
        Button(onClick = onDismiss) {
            Text(stringResource(R.string.cancel))
        }
    }, title = { Text(stringResource(R.string.add)) }, text = {
        Column {
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(stringResource(R.string.name)) })
            TextField(
                value = ingredients,
                onValueChange = { ingredients = it },
                label = { Text(stringResource(R.string.ingredients)) })
        }
    })
}