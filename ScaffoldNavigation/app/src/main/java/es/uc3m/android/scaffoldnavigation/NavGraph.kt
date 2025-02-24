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
package es.uc3m.android.scaffoldnavigation

const val HOME_ROUTE = "home"
const val PROFILE_ROUTE = "profile"
const val SETTING_ROUTE = "settings"

sealed class NavGraph(val route: String) {
    data object Home : NavGraph(HOME_ROUTE)
    data object Profile : NavGraph(PROFILE_ROUTE)
    data object Settings : NavGraph("$SETTING_ROUTE/{source}") {
        // Helper function to create the route with arguments
        fun createRoute(source: String) = "$SETTING_ROUTE/$source"
    }
}