package com.example.mapapp.ui.theme.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mapapp.Screens
import com.example.mapapp.screens.map.MapScreen
import com.example.mapapp.screens.markers.MarkersScreen

@Composable
fun NavigationHost(
    navController: NavHostController,
    paddingValues: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = Screens.Map.route,
        modifier = Modifier.padding(paddingValues = paddingValues)
    ) {
        composable(Screens.Map.route) {
            MapScreen()
        }
        composable(Screens.Markers.route) {
            MarkersScreen()
        }
    }
}


