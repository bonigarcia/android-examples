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
package es.uc3m.android.counterviewmodel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.asIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import es.uc3m.android.counterviewmodel.ui.theme.MyAppTheme

@Composable
fun CounterApp(modifier: Modifier = Modifier, viewModel: CounterViewModel = viewModel()) {
    // Observe the state from the ViewModel
    val count by viewModel.count.asIntState()

    // Stateless Counter composable
    Counter(
        count = count,
        onIncrement = { viewModel.increment() },
        onDecrement = { viewModel.decrement() },
        modifier = modifier.fillMaxSize()
    )
}

@Composable
fun Counter(
    count: Int, // State passed as a parameter
    onIncrement: () -> Unit, // Event callback for increment
    onDecrement: () -> Unit, // Event callback for decrement
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Count: $count",
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(onClick = onIncrement) {
                Text(stringResource(R.string.increment))
            }
            Button(onClick = onDecrement) {
                Text(stringResource(R.string.decrement))
            }
        }
    }
}

// ViewModel to manage the state
class CounterViewModel : ViewModel() {
    private val _count = mutableIntStateOf(0) // Mutable state
    val count: State<Int> get() = _count

    fun increment() {
        _count.intValue++
    }

    fun decrement() {
        _count.intValue--
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyAppTheme {
        CounterApp()
    }
}