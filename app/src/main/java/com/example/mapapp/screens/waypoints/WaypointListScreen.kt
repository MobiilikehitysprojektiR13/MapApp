package com.example.mapapp.screens.waypoints

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapapp.data.store.WaypointsListStore
import com.example.mapapp.screens.WaypointsViewModel

@Composable
fun WaypointsListScreen() {

    val context = LocalContext.current
    val waypointsListStore = WaypointsListStore(context)

    val waypointsViewModel: WaypointsViewModel = viewModel()
    val markers by waypointsViewModel.markers.collectAsState()

    LaunchedEffect(markers) {
        waypointsListStore.saveWaypointList(markers)
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        markers.forEach {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(modifier = Modifier.weight(1f), text = it.text)
                        IconButton(onClick = {
                            waypointsViewModel.removeMarker(it.id)
                        }) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "")
                        }
                    }
                }
            }
        }
    }
}