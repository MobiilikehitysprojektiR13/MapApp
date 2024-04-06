package com.example.mapapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.mapapp.ui.theme.MapAppTheme
import org.osmdroid.util.GeoPoint
import com.example.mapapp.ui.theme.components.NavigationHost
import androidx.navigation.compose.rememberNavController
import com.example.mapapp.components.BottomBar

data class TextMarker(
    val geoPoint: GeoPoint,
    val text: String,
    //val index: Int
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MapAppTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()

                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(modifier = Modifier.fillMaxSize(),

                     bottomBar = {
                        if (BottomNavigationItem().bottomNavigationItems().any {
                                it.route == navBackStackEntry?.destination?.route
                            }) {
                            BottomBar(navController = navController)
                        }
                    }) { paddingValues ->
                        NavigationHost(navController = navController, paddingValues = paddingValues)
                    }
                }
            }
        }
    }
}
