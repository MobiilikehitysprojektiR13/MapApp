package com.example.mapapp

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.utsman.osmandcompose.Marker
import com.utsman.osmandcompose.OpenStreetMap
import com.utsman.osmandcompose.rememberCameraState
import com.utsman.osmandcompose.rememberMarkerState
import org.osmdroid.util.GeoPoint

data class TextMarker(
    val geoPoint: GeoPoint,
    val text: String,
    //val index: Int
)

class MainActivity : AppCompatActivity() {
    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                val context = LocalContext.current

                val cameraState = rememberCameraState {
                    geoPoint = GeoPoint(-6.3970066, 106.8224316)
                    zoom = 12.0
                }

                val depokIcon: Drawable? by remember {
                    mutableStateOf(context.getDrawable(org.osmdroid.library.R.drawable.marker_default))
                }

                val trashcanIcon = Icons.Default.Delete

                val markers = remember { mutableStateListOf<TextMarker>() }
                var showAddMarkerDialog by remember { mutableStateOf(false) }
                var showDeleteMarkerDialog by remember { mutableStateOf(false) }
                var tempGeoPoint by remember { mutableStateOf<GeoPoint?>(null) }
                var textInput by remember { mutableStateOf("") }
                //var markerToDeleteIndex by remember { mutableStateOf(-1) }

                if (showAddMarkerDialog) {
                    AlertDialog(
                        onDismissRequest = { showAddMarkerDialog = false },
                        title = { Text("Add marker name") },
                        text = {
                            TextField(
                                value = textInput,
                                onValueChange = { textInput = it },
                                label = { Text("Marker name") }
                            )
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    if (tempGeoPoint != null && textInput.isNotBlank()) {
                                        markers.add(
                                            TextMarker(
                                                tempGeoPoint!!,
                                                textInput, /*markers.size*/
                                            )
                                        )
                                        showAddMarkerDialog = false
                                        tempGeoPoint = null
                                        textInput = ""
                                    }
                                }
                            ) { Text("Add") }
                        },
                        dismissButton = {
                            Button(onClick = {
                                showAddMarkerDialog = false
                                textInput = ""
                            }) { Text("Cancel") }
                        }
                    )
                }

                if (showDeleteMarkerDialog) {
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
                            Button(onClick = { showDeleteMarkerDialog = false }) { Text("Cancel") }
                        })
                }

                OpenStreetMap(
                    onMapLongClick = { geoPoint ->
                        tempGeoPoint = geoPoint
                        showAddMarkerDialog = true
                    },
                    modifier = Modifier.fillMaxSize(),
                    cameraState = cameraState
                ) {
                    markers.forEach { textMarker ->
                        val markerState = rememberMarkerState(geoPoint = textMarker.geoPoint)
                        //val markerIndex = textMarker.index
                        Marker(
                            state = markerState,
                            icon = depokIcon,
                            title = textMarker.text,
                            snippet = "Moro äijät"
                        ) {
                            Column(
                                modifier = Modifier
                                    .height(100.dp)
                                    .width(100.dp)
                                    .background(
                                        color = Color.White,
                                        shape = RoundedCornerShape(7.dp)
                                    ),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // setup content of info window
                                Text(text = it.title)
                                Text(text = it.snippet, fontSize = 10.sp)
                                IconButton(
                                    onClick = {
                                        //markerToDeleteIndex = markerIndex
                                        showDeleteMarkerDialog = true
                                    }
                                ) {
                                    Icon(
                                        imageVector = trashcanIcon,
                                        contentDescription = "Delete Marker"
                                    )
                                }

                            }
                        }

                    }

                }
            }
        }
    }
}
