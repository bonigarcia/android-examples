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
package es.uc3m.android.scaffoldnavigation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import es.uc3m.android.scaffoldnavigation.NavGraph
import es.uc3m.android.scaffoldnavigation.R

@Composable
fun SettingsScreen(navController: NavHostController) {
    Column(modifier = Modifier.padding(start = 16.dp)) {
        Text(text = stringResource(R.string.settings_screen))
        Row() {
            Button(onClick = {
                navController.navigate(NavGraph.Home.route)
            }) {
                Text(text = stringResource(R.string.home))
            }
            Spacer(modifier = Modifier.size(8.dp))
            Button(onClick = {
                navController.navigate(NavGraph.Profile.route)
            }) {
                Text(text = stringResource(R.string.profile))
            }
        }
    }
}