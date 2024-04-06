package com.example.mapapp.screens.map.dialogs

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
import com.example.mapapp.models.TextMarker
import com.example.mapapp.screens.markers.viewmodels.MarkersViewModel
import org.osmdroid.util.GeoPoint

@Composable
fun AddMarkerDialog(
    showAddMarkerDialog: MutableState<Boolean>, geoPoint: GeoPoint?
) {
    val markersViewModel: MarkersViewModel = viewModel()
    val markers = markersViewModel.markers

    val textInput = remember { mutableStateOf("") }


        AlertDialog(
            onDismissRequest = { showAddMarkerDialog.value = false },
            title = { Text("Add marker name") },
            text = {
                TextField(
                    value = textInput.value,
                    onValueChange = { textInput.value = it },
                    label = { Text("Marker name") }
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (geoPoint != null && textInput.value.isNotBlank()) {
                            showAddMarkerDialog.value = false
                            markersViewModel.addMarker(
                                TextMarker(
                                    geoPoint = geoPoint,
                                    text = textInput.value
                                )
                            )
                        }
                    }
                ) { Text("Add") }
            },
            dismissButton = {
                Button(onClick = {
                    showAddMarkerDialog.value = false
                    textInput.value = ""
                }) { Text("Cancel") }
            }
        )
    }