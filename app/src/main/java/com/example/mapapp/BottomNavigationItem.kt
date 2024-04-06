package com.example.mapapp

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.filled.Place
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.vectorResource

data class BottomNavigationItem(
    val label: String = "",
    val icon: ImageVector = Icons.Filled.Home,
    val route: String = ""
) {

    @Composable
    fun bottomNavigationItems(): List<BottomNavigationItem> = listOf(
        BottomNavigationItem(
            label = "Map",
            icon = ImageVector.vectorResource(R.drawable.baseline_map_24),
            route = Screens.Map.route
        ),
        BottomNavigationItem(
            label = "Markers",
            icon = Icons.Filled.Place,
            route = Screens.Markers.route
        )
    )
}
