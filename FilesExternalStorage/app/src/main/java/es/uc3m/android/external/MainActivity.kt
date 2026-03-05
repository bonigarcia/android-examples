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
package es.uc3m.android.external

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import es.uc3m.android.external.ui.theme.MyAppTheme
import androidx.core.graphics.createBitmap

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ExternalStorageScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun ExternalStorageScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val helper = remember(context.applicationContext) {
        ExternalStorageHelper(context.applicationContext)
    }
    val factory = remember(helper) { ExternalStorageViewModelFactory(helper) }
    val viewModel: ExternalStorageViewModel = viewModel(factory = factory)

    val fileContent by viewModel.fileContent.collectAsState()

    var fileName by rememberSaveable { mutableStateOf("demo.txt") }
    var content by rememberSaveable { mutableStateOf("Hello Android!") }

    // SAF Launchers
    val createDocumentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("text/plain")
    ) { uri ->
        uri?.let { viewModel.writeToUri(it, content) }
    }

    val openDocumentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let { viewModel.readFromUri(it) }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = fileName,
            onValueChange = { fileName = it },
            label = { Text(stringResource(R.string.file_name)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text(stringResource(R.string.file_content)) },
            modifier = Modifier.fillMaxWidth()
        )

        HorizontalDivider()

        // 1. App-specific storage
        Text(stringResource(R.string.app_specific), style = MaterialTheme.typography.titleMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { viewModel.writeToAppSpecific(fileName, content) }) {
                Text(stringResource(R.string.save))
            }
            Button(onClick = { viewModel.readFromAppSpecific(fileName) }) {
                Text(stringResource(R.string.read))
            }
        }

        // 2. MediaStore
        Text(stringResource(R.string.media_store), style = MaterialTheme.typography.titleMedium)
        Button(onClick = {
            val bitmap = createBitmap(100, 100).apply {
                eraseColor(Color.RED)
            }
            viewModel.saveImageToMediaStore(bitmap, fileName.replace(".txt", ""))
        }) {
            Text(stringResource(R.string.save_file))
        }

        // 3. SAF
        Text(stringResource(R.string.saf), style = MaterialTheme.typography.titleMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { createDocumentLauncher.launch(fileName) }) {
                Text(stringResource(R.string.save))
            }
            Button(onClick = { openDocumentLauncher.launch(arrayOf("text/plain")) }) {
                Text(stringResource(R.string.read))
            }
        }

        HorizontalDivider()

        Text(text = "Result:", style = MaterialTheme.typography.titleSmall)
        Text(text = fileContent)
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    MyAppTheme {
        ExternalStorageScreen()
    }
}
