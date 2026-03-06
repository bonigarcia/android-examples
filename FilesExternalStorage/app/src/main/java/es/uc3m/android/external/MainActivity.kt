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

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.createBitmap
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import es.uc3m.android.external.storage.ExternalStorageHelper
import es.uc3m.android.external.ui.theme.MyAppTheme
import es.uc3m.android.external.viewmodel.ExternalStorageViewModel
import android.graphics.Color as AndroidColor
import androidx.compose.ui.graphics.Color as ComposeColor

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
    val viewModel: ExternalStorageViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                val application =
                    this[androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application
                val helper = ExternalStorageHelper(application)
                ExternalStorageViewModel(application, helper)
            }
        })

    val fileContent by viewModel.fileContent.collectAsState()
    val loadedImage by viewModel.loadedImage.collectAsState()

    val defaultNameValue = stringResource(R.string.default_file_name)
    val defaultFileContent = stringResource(R.string.default_file_content)
    var fileName by rememberSaveable { mutableStateOf(defaultNameValue) }
    var content by rememberSaveable { mutableStateOf(defaultFileContent) }
    var selectedColor by remember { mutableStateOf(ComposeColor.Red) }

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
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
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

        Text(stringResource(R.string.select_color))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            val colors = listOf(
                ComposeColor.Red,
                ComposeColor.Green,
                ComposeColor.Blue,
                ComposeColor.Yellow,
                ComposeColor.Magenta
            )
            colors.forEach { color ->
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(color)
                        .border(
                            2.dp,
                            if (selectedColor == color) ComposeColor.Black else ComposeColor.Transparent
                        )
                        .clickable { selectedColor = color })
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                val androidColor = AndroidColor.argb(
                    (selectedColor.alpha * 255).toInt(),
                    (selectedColor.red * 255).toInt(),
                    (selectedColor.green * 255).toInt(),
                    (selectedColor.blue * 255).toInt()
                )
                val bitmap = createBitmap(200, 200).apply {
                    eraseColor(androidColor)
                }
                viewModel.saveImageToMediaStore(bitmap, fileName.replace(".txt", ""))
            }) {
                Text(stringResource(R.string.save))
            }
            Button(onClick = { viewModel.readImageFromMediaStore() }) {
                Text(stringResource(R.string.read))
            }
        }

        loadedImage?.let { bitmap ->
            Text(stringResource(R.string.loaded_image))
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = stringResource(R.string.from_media_store),
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.CenterHorizontally)
                    .border(1.dp, ComposeColor.Gray)
            )
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

        Text(text = stringResource(R.string.result), style = MaterialTheme.typography.titleSmall)
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
