package com.example.mapapp.screens.map.dialog

import android.util.Log
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapapp.data.dto.PrimitiveMarker
import com.example.mapapp.screens.WaypointsViewModel
import org.osmdroid.util.GeoPoint

@Composable
fun MarkerCreateDialog(showAddMarkerDialog: MutableState<Boolean>, geoPoint: GeoPoint?) {

    val waypointsViewModel: WaypointsViewModel = viewModel()
    val markers = waypointsViewModel.markers.collectAsState()

    val textInput = remember { mutableStateOf("") }

    Log.e("MarkerCreateDialog size: ", markers.value.size.toString())

    AlertDialog(onDismissRequest = { showAddMarkerDialog.value = false },
        title = { Text("Add marker name") },
        text = {
            TextField(value = textInput.value, onValueChange = {
                textInput.value = it
            }, label = {
                Text("Marker name")
            })
        },
        confirmButton = {
            Button(onClick = {
                if (geoPoint != null && textInput.value.isNotBlank()) {
                    waypointsViewModel.addMarker(
                        PrimitiveMarker(
                            id = (markers.value.maxOfOrNull { it.id }?.plus(1)) ?: 0,
                            geoPoint = PrimitiveMarker.PrimitiveGeoPoint(
                                latitude = geoPoint.latitude, longitude = geoPoint.longitude
                            ),
                            text = textInput.value
                        )
                    )
                    showAddMarkerDialog.value = false
                    textInput.value = ""
                }
            }) { Text("Add") }
        },
        dismissButton = {
            Button(onClick = {
                showAddMarkerDialog.value = false
                textInput.value = ""
            }) { Text("Cancel") }
        })
}