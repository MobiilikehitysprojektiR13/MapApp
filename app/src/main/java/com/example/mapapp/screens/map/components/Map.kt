package com.example.mapapp.screens.map.components

import android.annotation.SuppressLint
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapapp.R
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

    LaunchedEffect("load_markers") {
        dataStore.getMarkers.collect {
            if (it.isNotEmpty() && !markersViewModel.loaded.value) {
                markersViewModel.loaded.value = true
                markersViewModel.getMarkers(it)
            }
        }
    }

    val cameraState = rememberCameraState {
        GeoPoint(39.1422222222, 34.1702777778)
    }

    val liveMarkerState = rememberMarkerState(
        geoPoint = GeoPoint(39.1422222222, 34.1702777778)
    )

    val updateCameraState by remember { mutableStateOf(true) }

    if (location != null && updateCameraState) {
        cameraState.apply {
            geoPoint = GeoPoint(location.latitude, location.longitude)
            zoom = 12.0
        }

        liveMarkerState.apply {
            geoPoint = GeoPoint(location.latitude, location.longitude)
        }
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
                Marker(state = liveMarkerState,
                    icon = context.getDrawable(R.drawable.baseline_my_location_24)?.apply {
                        setTint(Color.Red.toArgb())
                    })
                markers.forEach { textMarker ->
                    val markerState = rememberMarkerState(geoPoint = textMarker.geoPoint)
                    Marker(
                        state = markerState,
                        icon = context.getDrawable(org.osmdroid.library.R.drawable.marker_default),
                        title = textMarker.text
                    ) {
                        Column(
                            modifier = Modifier
                                .height(70.dp)
                                .width(150.dp)
                                .background(
                                    color = Color.White, shape = RoundedCornerShape(8.dp)
                                ),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = it.title)
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
                }, modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomEnd)
            ) {
                Icon(
                    ImageVector.vectorResource(R.drawable.baseline_my_location_24),
                    contentDescription = "Go to location"
                )
            }
        }
    }
}

