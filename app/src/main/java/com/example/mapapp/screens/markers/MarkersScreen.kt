package com.example.mapapp.screens.markers

import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapapp.datastore.StoreMarkers
import com.example.mapapp.models.TextMarker
import com.example.mapapp.screens.markers.viewmodels.MarkersViewModel
import org.osmdroid.util.GeoPoint




@Preview
@Composable
fun MarkersScreen() {
    val context = LocalContext.current
    val dataStore = StoreMarkers(context)
    val markersViewModel: MarkersViewModel = viewModel()

    val markers = markersViewModel.markers

    LaunchedEffect(Unit) {
        markersViewModel.loading.value = true
        dataStore.getMarkers.collect {
            markersViewModel.getMarkers(it)
            markersViewModel.loading.value = false
            markersViewModel.loaded.value = true
        }
    }
    LaunchedEffect(markers) {
        dataStore.saveMarkers(markers)
    }
    MapMarkerList(markers)

}
@Composable
fun MapMarkerList(markers: List<TextMarker>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Map Markers",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        markers.forEach { marker ->
            MapMarkerItem(marker = marker)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun MapMarkerItem(marker: TextMarker) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = "Map Marker",
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column (
            modifier = Modifier.weight(1f)
        ){
            Text(
                text = marker.text,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 4.dp)

            )
            Text(
                text =  geoPointToLatitudeText(marker.geoPoint),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
            Text(
                text = geoPointToLongitudeText(marker.geoPoint),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete Marker",
            tint = Color.Red,
            modifier = Modifier.clickable { deleteMarker() }
        )
    }
}

fun deleteMarker(){

}

fun geoPointToText(geoPoint: GeoPoint): String {
    return "Lat: ${geoPoint.latitude}, Long: ${geoPoint.longitude}"
}
fun geoPointToLatitudeText(geoPoint: GeoPoint): String {
    return "Lat: ${geoPoint.latitude}"
}
fun geoPointToLongitudeText(geoPoint: GeoPoint): String {
    return "Long: ${geoPoint.longitude}"
}