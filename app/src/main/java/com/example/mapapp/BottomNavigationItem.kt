package com.example.mapapp

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.filled.Place

data class BottomNavigationItem(
    val label: String = "",
    val icon: ImageVector = Icons.Filled.Home,
    val route: String = ""
) {
    fun bottomNavigationItems(): List<BottomNavigationItem> = listOf(
        BottomNavigationItem(
            label = "Map",
            icon = Icons.Filled.Home,
            route = Screens.Map.route
        ),
        BottomNavigationItem(
            label = "Markers",
            icon = Icons.Filled.Place,
            route = Screens.Markers.route
        )
    )
}
