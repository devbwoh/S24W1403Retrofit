package kr.ac.kumoh.ce.s20240000.s24w1403retrofit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        val navController = rememberNavController()

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
                SongList()
            }
            composable(route = SongScreen.SongDetail.name) {
                SongDetail()
            }
        }
    }
}
