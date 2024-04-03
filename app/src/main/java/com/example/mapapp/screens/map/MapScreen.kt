package com.example.mapapp.screens.map

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapapp.data.store.WaypointsListStore
import com.example.mapapp.screens.Location
import com.example.mapapp.screens.LocationStatus
import com.example.mapapp.screens.WaypointsViewModel
import com.example.mapapp.screens.map.dialog.MarkerCreateDialog
import com.utsman.osmandcompose.CameraProperty
import com.utsman.osmandcompose.CameraState
import com.utsman.osmandcompose.MapProperties
import com.utsman.osmandcompose.Marker
import com.utsman.osmandcompose.MarkerState
import com.utsman.osmandcompose.OpenStreetMap
import com.utsman.osmandcompose.ZoomButtonVisibility
import org.osmdroid.library.R
import org.osmdroid.util.GeoPoint


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MapScreen() {
    val context = LocalContext.current
    val waypointsListStore = WaypointsListStore(context)

    val waypointsViewModel: WaypointsViewModel = viewModel()
    val markers by waypointsViewModel.markers.collectAsState()

    val showAddMarkerDialog = remember { mutableStateOf(false) }
    val tempGeoPoint = remember { mutableStateOf<GeoPoint?>(null) }

    val cameraDefaultState = remember { mutableStateOf<GeoPoint?>(null) }

    LaunchedEffect("load_markers") {
        waypointsListStore.getWaypointsList.collect {
            if (it.isNotEmpty() && !waypointsViewModel.loaded.value) {
                Log.e("MapScreen: ", "Loaded")
                waypointsViewModel.loaded.value = true
                waypointsViewModel.loadMarkers(it)
            }
        }
    }

    LaunchedEffect(markers) {
        waypointsListStore.saveWaypointList(markers)
    }

    if (showAddMarkerDialog.value) MarkerCreateDialog(showAddMarkerDialog, tempGeoPoint.value)

    Location {
        when (it.status) {
            LocationStatus.SUCCESS -> cameraDefaultState.value =
                GeoPoint(it.location!!.latitude, it.location.longitude)

            LocationStatus.FETCHING -> FetchingComponent()

            LocationStatus.DENIED -> {
                Toast.makeText(
                    context, "No location access, map moved to zero coordinates", Toast.LENGTH_SHORT
                ).show()
                cameraDefaultState.value = GeoPoint(0.0, 0.0)
            }
        }

        if (it.status == LocationStatus.SUCCESS || it.status == LocationStatus.DENIED) OpenStreetMap(
            modifier = Modifier.fillMaxSize(), cameraState = CameraState(
                CameraProperty(
                    geoPoint = cameraDefaultState.value!!, zoom = 12.0
                )
            ), onMapLongClick = { geoPoint ->
                tempGeoPoint.value = geoPoint
                showAddMarkerDialog.value = true
            }, properties = MapProperties(zoomButtonVisibility = ZoomButtonVisibility.NEVER)
        ) {
            markers.forEach { marker ->
                Marker(
                    id = "${marker.id}",
                    title = marker.text,
                    state = MarkerState(geoPoint = marker.geoPoint.toOsmPoint()),
                    icon = context.getDrawable(R.drawable.ic_menu_offline)
                ) { windowData ->
                    Text(
                        modifier = Modifier
                            .border(2.dp, Color.Gray, RoundedCornerShape(8.dp))
                            .background(Color.LightGray)
                            .padding(8.dp), text = windowData.title
                    )
                }
            }
        }
    }
}