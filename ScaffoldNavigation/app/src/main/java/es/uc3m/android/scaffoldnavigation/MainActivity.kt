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

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import es.uc3m.android.scaffoldnavigation.screens.HomeScreen
import es.uc3m.android.scaffoldnavigation.screens.ProfileScreen
import es.uc3m.android.scaffoldnavigation.screens.SettingsScreen
import es.uc3m.android.scaffoldnavigation.ui.theme.MyAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyAppTheme {
                MyScreen()
            }
        }
    }
}

data class NavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: String
)

@Composable
fun MyScreen() {
    // Items for the navigation drawer and bottom bar
    val items = listOf(
        NavigationItem(
            title = stringResource(R.string.home),
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            route = NavGraph.Home.route
        ),
        NavigationItem(
            title = stringResource(R.string.profile),
            selectedIcon = Icons.Filled.Person,
            unselectedIcon = Icons.Outlined.Person,
            route = NavGraph.Profile.route
        ),
        NavigationItem(
            title = stringResource(R.string.settings),
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings,
            route = NavGraph.Settings.createRoute("From navigation item")
        )
    )
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val snack = remember { SnackbarHostState() }
    val navController = rememberNavController()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            MyDrawerContent(items, scope, drawerState, navController)
        },
    ) {
        Scaffold(
            topBar = {
                MyTopAppBar(scope, drawerState, snack)
            },
            floatingActionButton = {
                MyFloatingActionButtons(scope, snack)
            },
            content = { innerPadding ->
                MyContent(Modifier.padding(innerPadding), navController)
            },
            bottomBar = {
                MyNavigationBar(items, navController)
            },
            snackbarHost = { SnackbarHost(snack) }
        )
    }
}

@Composable
fun MyDrawerContent(
    items: List<NavigationItem>,
    scope: CoroutineScope,
    drawerState: DrawerState,
    navController: NavHostController
) {
    var selectedItem by remember { mutableIntStateOf(0) }

    ModalDrawerSheet {
        Spacer(modifier = Modifier.height(24.dp))
        items.forEachIndexed { index, item ->
            NavigationDrawerItem(
                label = {
                    Text(text = item.title)
                },
                selected = index == selectedItem,
                onClick = {
                    selectedItem = index
                    scope.launch {
                        drawerState.close()
                    }
                    navController.navigate(item.route)
                },
                icon = {
                    Icon(
                        imageVector = if (index == selectedItem) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.title
                    )
                },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(scope: CoroutineScope, drawerState: DrawerState, snack: SnackbarHostState) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = {
                scope.launch {
                    drawerState.apply {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                }
            }) {
                Icon(
                    imageVector = Icons.Rounded.Menu,
                    stringResource(R.string.menu)
                )
            }
        },
        title = { Text(text = stringResource(R.string.app_name)) },
        actions = {
            IconButton(onClick = {
                scope.launch {
                    snack.showSnackbar("TODO: Search")
                }
            }) {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = stringResource(R.string.search)
                )
            }
            MyDropdownMenu(scope, snack)
        })
}

@Composable
fun MyDropdownMenu(scope: CoroutineScope, snack: SnackbarHostState) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(Icons.Default.MoreVert, contentDescription = stringResource(R.string.more))
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.dropdown_1)) },
                onClick = {
                    scope.launch {
                        snack.showSnackbar("TODO: Option 1")
                    }
                }
            )
            DropdownMenuItem(
                text = { Text(stringResource(R.string.dropdown_2)) },
                onClick = {
                    scope.launch {
                        snack.showSnackbar("TODO: Option 2")
                    }
                }
            )
        }
    }
}

@Composable
fun MyFloatingActionButtons(scope: CoroutineScope, snack: SnackbarHostState) {
    FloatingActionButton(onClick = {
        scope.launch {
            snack.showSnackbar("TODO: Add")
        }
    }) {
        Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add))
    }
}

@Composable
fun MyContent(modifier: Modifier = Modifier, navController: NavHostController) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = NavGraph.Home.route,
    ) {
        composable(NavGraph.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(NavGraph.Profile.route) {
            ProfileScreen(navController = navController)
        }
        composable(
            NavGraph.Settings.route,
            arguments = listOf(navArgument("source") { type = NavType.StringType })
        ) { backStackEntry ->
            val source = backStackEntry.arguments?.getString("source")
            SettingsScreen(navController = navController, source)
        }
    }
}

@Composable
fun MyNavigationBar(items: List<NavigationItem>, navController: NavHostController) {
    var selectedItem by remember { mutableIntStateOf(0) }

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = index == selectedItem,
                onClick = {
                    selectedItem = index
                    navController.navigate(item.route)
                },
                label = {
                    Text(text = item.title)
                },
                icon = {
                    Icon(
                        imageVector = if (index == selectedItem) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.title
                    )
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMyScreen() {
    MyAppTheme {
        MyScreen()
    }
}