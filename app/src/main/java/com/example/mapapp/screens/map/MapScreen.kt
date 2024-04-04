package com.example.mapapp.screens.map

import androidx.compose.runtime.Composable
import com.example.mapapp.components.ProgressIndicator
import com.example.mapapp.screens.map.components.Location
import com.example.mapapp.screens.map.components.LocationStatus
import com.example.mapapp.screens.map.components.Map

@Composable
fun MapScreen() {

    Location { locationWithPermission ->
        when(locationWithPermission.status) {
            LocationStatus.FETCHING -> ProgressIndicator()
            LocationStatus.DENIED -> Map(null)
            LocationStatus.SUCCESS -> Map(locationWithPermission.location)
        }
    }
}