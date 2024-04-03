package com.example.mapapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mapapp.screens.map.MapScreen
import com.example.mapapp.screens.waypoints.WaypointsListScreen

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            val context = LocalContext.current

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Scaffold(topBar = {
                    TopAppBar(title = {
                        Text(text = context.resources.getString(R.string.app_name))
                    }, actions = {
                        if (currentDestination?.route == Screens.Map.route) IconButton(
                            onClick = {
                                navController.navigate(Screens.WaypointsList.route)
                            }) {
                            Icon(Icons.Default.List, "Waypoints list")
                        }
                    }, navigationIcon = {
                        if (currentDestination?.route == Screens.WaypointsList.route) IconButton(
                            onClick = {
                                navController.navigateUp()
                            }) {
                            Icon(Icons.Default.ArrowBack, "Go back")
                        }
                    })
                }) {
                    NavHost(
                        modifier = Modifier.padding(it),
                        navController = navController,
                        startDestination = Screens.Map.route
                    ) {
                        composable(Screens.Map.route) {
                            MapScreen()
                        }
                        composable(Screens.WaypointsList.route) {
                            WaypointsListScreen()
                        }
                    }
                }
            }
        }
    }
}