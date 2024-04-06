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
import androidx.navigation.compose.rememberNavController
import com.example.mapapp.components.BottomBar
import com.example.mapapp.ui.theme.MapAppTheme
import com.example.mapapp.ui.theme.components.NavigationHost


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
