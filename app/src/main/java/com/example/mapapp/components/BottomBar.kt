package com.example.mapapp.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.mapapp.BottomNavigationItem

@Composable
fun BottomBar(navController: NavController) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    if (!BottomNavigationItem().bottomNavigationItems().any {
            it.route == currentDestination?.route
        }) return

    NavigationBar {
        BottomNavigationItem().bottomNavigationItems().forEach { navigationItem ->
            NavigationBarItem(selected = navigationItem.route == currentDestination?.route,
                label = {
                    Text(navigationItem.label)
                },
                icon = {
                    Icon(navigationItem.icon, navigationItem.label)
                },
                onClick = {
                    navController.popBackStack()
                    navController.navigate(navigationItem.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                })
        }
    }
}