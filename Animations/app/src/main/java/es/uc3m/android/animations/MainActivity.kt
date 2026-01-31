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
package es.uc3m.android.animations

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import es.uc3m.android.animations.ui.theme.MyAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        ChangeSize()
                        ChangeSizeAndColor()
                        ToggleVisibility()
                        FadeScreens()
                        RotatingIcon()
                    }
                }
            }
        }
    }
}

@Composable
fun ChangeSize() {
    var expanded by remember { mutableStateOf(false) }
    val size by animateDpAsState(targetValue = if (expanded) 200.dp else 100.dp)

    Box(
        modifier = Modifier
            .size(size)
            .background(Color.Blue)
            .clickable { expanded = !expanded })
}

@Composable
fun ChangeSizeAndColor() {
    var toggled by remember { mutableStateOf(false) }
    val transition = updateTransition(targetState = toggled)

    val color by transition.animateColor(label = "color") { state ->
        if (state) Color.Green else Color.Red
    }
    val size by transition.animateDp(label = "size") { state ->
        if (state) 150.dp else 100.dp
    }

    Box(
        modifier = Modifier
            .size(size)
            .background(color)
            .clickable { toggled = !toggled })
}

@Composable
fun ToggleVisibility() {
    var visible by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)
    ) {
        Button(onClick = { visible = !visible }) {
            Text(if (visible) "Hide" else "Show")
        }

        AnimatedVisibility(visible) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.Blue)
            )
        }
    }
}

@Composable
fun FadeScreens() {
    var screen by remember { mutableStateOf("Screen1") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)
    ) {
        Button(onClick = { screen = if (screen == "Screen1") "Screen2" else "Screen1" }) {
            Text("Switch Screen")
        }

        Crossfade(targetState = screen) { currentScreen ->
            when (currentScreen) {
                "Screen1" -> Text("Screen 1", style = MaterialTheme.typography.headlineSmall)
                "Screen2" -> Text("Screen 2", style = MaterialTheme.typography.headlineSmall)
            }
        }
    }
}

@Composable
fun RotatingIcon() {
    val infiniteTransition = rememberInfiniteTransition()
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 360f, animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing), repeatMode = RepeatMode.Restart
        )
    )

    Icon(
        imageVector = ImageVector.vectorResource(R.drawable.outline_3d_rotation_24),
        contentDescription = "Rotating Icon",
        modifier = Modifier
            .rotate(angle)
            .size(48.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    MyAppTheme {
        ChangeSizeAndColor()
    }
}
