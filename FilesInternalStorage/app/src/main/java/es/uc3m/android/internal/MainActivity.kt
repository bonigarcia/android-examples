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
package es.uc3m.android.internal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import es.uc3m.android.internal.ui.theme.MyAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    FileScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun FileScreen(
    modifier: Modifier = Modifier,
    viewModel: FileViewModel = viewModel(
        factory = FileViewModelFactory(
            FileHelper(LocalContext.current)
        )
    )
) {
    val fileContent by viewModel.fileContent.collectAsState()
    val fileList by viewModel.fileList.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Input field for file name
        var fileName by remember { mutableStateOf("") }
        OutlinedTextField(
            value = fileName,
            onValueChange = { fileName = it },
            label = { Text(stringResource(R.string.file_name)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Input field for file content
        var content by remember { mutableStateOf("") }
        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text(stringResource(R.string.file_content)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Buttons for file operations
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = { viewModel.writeToFile(fileName, content) }) {
                Text(stringResource(R.string.save_file))
            }
            Button(onClick = { viewModel.readFromFile(fileName) }) {
                Text(stringResource(R.string.read_file))
            }
            Button(onClick = { viewModel.deleteFile(fileName) }) {
                Text(stringResource(R.string.delete_file))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display file content
        Text(text = stringResource(R.string.content, fileContent))

        Spacer(modifier = Modifier.height(16.dp))

        // Display list of files
        Text(text = stringResource(R.string.files_in_internal_storage))
        fileList.forEach { file ->
            Text(text = file)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    MyAppTheme {
        FileScreen()
    }
}
