package kr.ac.kumoh.ce.s20240000.s24w1403retrofit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.launch
import kr.ac.kumoh.ce.s20240000.s24w1403retrofit.ui.theme.S24W1403RetrofitTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            S24W1403RetrofitTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val viewModel: SongViewModel = viewModel()
    val songList by viewModel.songList.observeAsState(emptyList())

    // Scaffold 안에 있던 것 이동
    val navController = rememberNavController()

    val drawerState = rememberDrawerState(DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerSheet(drawerState) {
                navController.navigate(it) {
                    launchSingleTop = true
                    popUpTo(it) { inclusive = true }
                }
            }
        },
        gesturesEnabled = true,
    ) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            NavHost(
                navController = navController,
                //startDestination = SongScreen.Singer.name,
                startDestination = SongScreen.Song.name,
                modifier = Modifier.padding(innerPadding),
            ) {
                composable(route = SongScreen.Singer.name) {
                    SingerList()
                }
                composable(route = SongScreen.Song.name) {
                    SongList() {
                        navController.navigate(it) {
                            launchSingleTop = true
                            popUpTo(it) { inclusive = true }
                        }
                    }
                }
                composable(
                    route = SongScreen.SongDetail.name + "/{id}",
                    arguments = listOf(navArgument("id") {
                        type = NavType.IntType
                    })
                ) {
                    val id = it.arguments?.getInt("id") ?: -1
                    val song = songList.find { song -> song.id == id }
                    if (song != null)
                        SongDetail(song)
                }
            }
        }
    }
}

@Composable
fun DrawerSheet(
    drawerState: DrawerState,
    onNavigate: (String) -> Unit,
) {
    val scope = rememberCoroutineScope()

    ModalDrawerSheet {
        NavigationDrawerItem(
            label = { Text("가수 리스트") },
            selected = false,
            onClick = {
                onNavigate(SongScreen.Singer.name)
                scope.launch {
                    drawerState.close()
                }
            },
            icon = {
                Icon(
                    Icons.Filled.Face,
                    contentDescription = "가수 리스트 아이콘"
                )
            }
        )
        NavigationDrawerItem(
            label = { Text("노래 리스트") },
            selected = false,
            onClick = {
                onNavigate(SongScreen.Song.name)
                scope.launch {
                    drawerState.close()
                }
            },
            icon = {
                Icon(
                    Icons.Filled.Favorite,
                    contentDescription = "노래 리스트 아이콘"
                )
            }
        )
    }
}
