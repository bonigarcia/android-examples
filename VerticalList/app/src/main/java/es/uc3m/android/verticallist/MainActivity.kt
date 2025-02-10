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
package es.uc3m.android.verticallist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import es.uc3m.android.verticallist.ui.theme.MyAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MyLazyColumn(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

data class Item(val title: String, val description: String)

@Composable
fun MyLazyColumn(modifier: Modifier = Modifier) {
    val title = stringResource(R.string.item_title)
    val description = stringResource(R.string.item_description)
    val myList = (0..20).map {
        Item(
            title = String.format(title, it + 1),
            description = String.format(description, it + 1),
        )
    }

    LazyColumn(
        state = rememberLazyListState(),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
        content = {
            item {
                Text(
                    text = stringResource(R.string.my_list),
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            items(myList) {
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = it.title,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = it.description,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    HorizontalDivider()
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    MyAppTheme {
        MyLazyColumn()
    }
}
