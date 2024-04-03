package com.example.mapapp.screens.map.components

import android.graphics.drawable.Drawable
import android.location.Location
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.mapapp.TextMarker
import com.example.mapapp.datastore.StoreMarkers
import com.utsman.osmandcompose.MapProperties
import com.utsman.osmandcompose.Marker
import com.utsman.osmandcompose.OpenStreetMap
import com.utsman.osmandcompose.ZoomButtonVisibility
import com.utsman.osmandcompose.rememberCameraState
import com.utsman.osmandcompose.rememberMarkerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.osmdroid.library.R
import org.osmdroid.util.GeoPoint

@Composable
fun Map(location: Location?) {


    val context = LocalContext.current
    val dataStore = StoreMarkers(context)

    val cameraState = rememberCameraState {
        GeoPoint(39.1422222222, 34.1702777778)

    }
    val liveMarkerState = rememberMarkerState(
        geoPoint = GeoPoint(39.1422222222, 34.1702777778)
    )

    var updateCameraState by remember { mutableStateOf(true) }

    if (location != null && updateCameraState) {
        cameraState.apply {
            geoPoint = GeoPoint(location.latitude, location.longitude)
            zoom = 12.0
        }

            liveMarkerState.apply {
                geoPoint = GeoPoint(location.latitude, location.longitude)
            }

    }


    val locationIcon: Drawable? by remember {
        mutableStateOf(context.getDrawable(R.drawable.ic_menu_mylocation))
    }

    val locationIconPainter = rememberAsyncImagePainter(model = locationIcon)

    val poiIcon: Drawable? by remember {
        mutableStateOf(context.getDrawable(R.drawable.marker_default))
    }

    val trashcanIcon = Icons.Default.Delete

    val markers = remember { mutableStateListOf<TextMarker>() }
    LaunchedEffect(Unit) {
        dataStore.getMarkers.collect { markersList ->
            markers.clear()
            markers.addAll(markersList)
        }
    }
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
                            CoroutineScope(Dispatchers.IO).launch {
                                dataStore.saveMarkers(markers)
                            }

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
                Button(onClick = {
                    showDeleteMarkerDialog = false
                }) { Text("Cancel") }
            })
    }

    Box(modifier = Modifier.fillMaxSize()) {
        OpenStreetMap(
            onMapLongClick = { geoPoint ->
                updateCameraState = false
                tempGeoPoint = geoPoint
                showAddMarkerDialog = true
            },
            modifier = Modifier.fillMaxSize(),
            cameraState = cameraState,
            properties = MapProperties(zoomButtonVisibility = ZoomButtonVisibility.NEVER)
        ) {

            Marker(
                state = liveMarkerState,
                icon = locationIcon
            )
            markers.forEach { textMarker ->
                val markerState = rememberMarkerState(geoPoint = textMarker.geoPoint)
                //val markerIndex = textMarker.index
                Marker(
                    state = markerState,
                    icon = poiIcon,
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

        if (location != null) {
            FloatingActionButton(
                onClick = {
                    cameraState.apply {
                        geoPoint = GeoPoint(location.latitude, location.longitude)
                        zoom = 12.0
                    }

                        liveMarkerState.apply {
                            geoPoint = GeoPoint(location.latitude, location.longitude)
                        }
                    },
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomEnd)
            ) {
                Icon(painter = locationIconPainter, contentDescription = "Go to location")
            }
        }
    }
}

