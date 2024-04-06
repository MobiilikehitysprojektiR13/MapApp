package com.example.mapapp.screens.markers

import android.util.Log
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapapp.datastore.StoreMarkers
import com.example.mapapp.screens.markers.viewmodels.MarkersViewModel
import org.osmdroid.util.GeoPoint


@Preview
@Composable
fun MarkersScreen() {
    val context = LocalContext.current
    val markersStore = StoreMarkers(context)
    val markersViewModel: MarkersViewModel = viewModel()

    val markers by MarkersViewModel.markers.collectAsState()

    LaunchedEffect(markers) {
        markersStore.saveMarkers(markers)
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
                            Log.d("DeleteMarker", "Deleting marker with index: ${it.index}")
                            markersViewModel.deleteMarker(it.index)
                        }
                        ) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "")
                        }
                    }
                }
            }
        }
    }

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