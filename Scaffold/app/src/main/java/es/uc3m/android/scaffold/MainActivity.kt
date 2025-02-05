package es.uc3m.android.scaffold

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import es.uc3m.android.scaffold.ui.theme.MyAppTheme


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
    Scaffold(
        topBar = {
            MyTopAppBar()
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar() {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = { /* TODO */ }) {
                Icon(imageVector = Icons.Rounded.Menu, contentDescription = null)
            }
        },
        title = { Text(text = stringResource(R.string.app_name)) },
        actions = {
//            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.high) {
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
//            }
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
        modifier = modifier
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