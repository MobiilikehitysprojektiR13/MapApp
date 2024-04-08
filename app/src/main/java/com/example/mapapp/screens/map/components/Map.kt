package com.example.mapapp.screens.map.components

import android.annotation.SuppressLint
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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.mapapp.components.ProgressIndicator
import com.example.mapapp.datastore.StoreMarkers
import com.example.mapapp.screens.map.dialogs.AddMarkerDialog
import com.example.mapapp.screens.markers.viewmodels.MarkersViewModel
import com.utsman.osmandcompose.MapProperties
import com.utsman.osmandcompose.Marker
import com.utsman.osmandcompose.OpenStreetMap
import com.utsman.osmandcompose.ZoomButtonVisibility
import com.utsman.osmandcompose.rememberCameraState
import com.utsman.osmandcompose.rememberMarkerState
import org.osmdroid.library.R
import org.osmdroid.util.GeoPoint

@SuppressLint("UseCompatLoadingForDrawables")
@Composable
fun Map(location: Location?) {


    val context = LocalContext.current
    val dataStore = StoreMarkers(context)
    val markersViewModel: MarkersViewModel = viewModel()

    val markers by markersViewModel.markers.collectAsState()
    val showAddMarkerDialog = remember { mutableStateOf(false) }
    var tempGeoPoint by remember { mutableStateOf<GeoPoint?>(null) }

    LaunchedEffect(Unit) {
            markersViewModel.loading.value = true
        dataStore.getMarkers.collect {
            markersViewModel.getMarkers(it)
            markersViewModel.loading.value = false
            markersViewModel.loaded.value = true
        }
    }


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

    LaunchedEffect(markers) {
        dataStore.saveMarkers(markers)
    }

    if (showAddMarkerDialog.value) {
        AddMarkerDialog(showAddMarkerDialog, tempGeoPoint)
    }



    Box(modifier = Modifier.fillMaxSize()) {
        if (markersViewModel.loading.value) {
            ProgressIndicator()
        } else {
            OpenStreetMap(
                onMapLongClick = { geoPoint ->
                    tempGeoPoint = geoPoint
                    showAddMarkerDialog.value = true
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

