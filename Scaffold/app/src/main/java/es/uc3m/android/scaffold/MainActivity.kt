package es.uc3m.android.scaffold

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import es.uc3m.android.scaffold.ui.theme.MyAppTheme
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

@Composable
fun MyScreen() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            MyDrawerContent(scope, drawerState)
        },
    ) {
        Scaffold(
            topBar = {
                MyTopAppBar(scope, drawerState)
            },
            floatingActionButton = {
                MyFloatingActionButtons()
            },
            content = { innerPadding ->
                MyContent(Modifier.padding(innerPadding))
            },
            bottomBar = {
                MyNavigationBar()
            }
        )
    }
}

data class NavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

@Composable
fun MyDrawerContent(scope: CoroutineScope, drawerState: DrawerState) {
    var selectedItem by remember { mutableIntStateOf(0) }

    val items = listOf(
        NavigationItem(
            title = stringResource(R.string.home),
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home
        ),
        NavigationItem(
            title = stringResource(R.string.profile),
            selectedIcon = Icons.Filled.Person,
            unselectedIcon = Icons.Outlined.Person
        ),
        NavigationItem(
            title = stringResource(R.string.settings),
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings
        )
    )
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
fun MyTopAppBar(scope: CoroutineScope, drawerState: DrawerState) {
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
            IconButton(onClick = { /* TODO */ }) {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = stringResource(R.string.search)
                )
            }
            IconButton(onClick = { /* TODO */ }) {
                Icon(
                    imageVector = Icons.Outlined.AccountCircle,
                    contentDescription = stringResource(R.string.account)
                )
            }
        })
}

@Composable
fun MyFloatingActionButtons() {
    FloatingActionButton(onClick = { /* TODO */ }) {
        Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add))
    }
}

@Composable
fun MyContent(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.content_text),
        modifier = modifier.padding(16.dp)
    )
}


@Composable
fun MyNavigationBar() {
    NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
        NavigationBarItem(
            selected = true,
            onClick = { /* TODO */ },
            icon = { Icon(Icons.Filled.Home, stringResource(R.string.home)) },
            label = { Text(text = stringResource(R.string.home)) }
        )
        NavigationBarItem(
            selected = false,
            onClick = { /* TODO */ },
            icon = { Icon(Icons.Filled.Favorite, stringResource(R.string.favorite)) },
            label = { Text(text = stringResource(R.string.favorite)) }
        )
        NavigationBarItem(
            selected = false,
            onClick = { /* TODO */ },
            icon = { Icon(Icons.Filled.Info, stringResource(R.string.info)) },
            label = { Text(text = stringResource(R.string.info)) }
        )
        NavigationBarItem(
            selected = false,
            onClick = { /* TODO */ },
            icon = { Icon(Icons.Filled.Place, stringResource(R.string.places)) },
            label = { Text(text = stringResource(R.string.places)) }
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewMyScreen() {
    MyAppTheme {
        MyScreen()
    }
}