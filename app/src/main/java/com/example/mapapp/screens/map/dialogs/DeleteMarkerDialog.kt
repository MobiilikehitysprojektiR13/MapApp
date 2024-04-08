package com.example.mapapp.screens.map.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

//moved
@Composable
fun DeleteMarkerDialog() {
    var showDeleteMarkerDialog by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = { showDeleteMarkerDialog = false },
        title = { Text("Delete Marker") },
        text = { Text("Are you sure you want to delete this marker?") },
        confirmButton = {
            Button(
                onClick = {
                    //markers.removeAt(markerToDeleteIndex)
                    showDeleteMarkerDialog = false
                }
            ) { Text("Delete") }
        },
        dismissButton = {
            Button(onClick = {
                showDeleteMarkerDialog = false
            }) { Text("Cancel") }
        })
}