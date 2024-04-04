package com.example.mapapp.screens.markers

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


// dummy
data class MapMarkerData(val name: String, val coordinates: String)

// dummy
val mapMarkers = listOf(
    MapMarkerData("Marker 1", "Latitude: 1.234, Longitude: 5.678"),
    MapMarkerData("Marker 2", "Latitude: 2.345, Longitude: 6.789"),
    MapMarkerData("Marker 3", "Latitude: 3.456, Longitude: 7.890")
)

@Preview
@Composable
fun MarkersScreen() {
    MapMarkerList()

}
@Composable
fun MapMarkerList() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Map Markers",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            mapMarkers.forEach { marker ->
                MapMarkerItem(marker = marker)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
}

@Composable
fun MapMarkerItem(marker: MapMarkerData) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Map Marker",
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = marker.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 4.dp)

                )
                Text(
                    text = marker.coordinates,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White,
                )
            }
        }
}
