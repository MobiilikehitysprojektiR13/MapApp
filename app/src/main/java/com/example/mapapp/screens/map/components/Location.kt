package com.example.mapapp.screens.map.components

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context.LOCATION_SERVICE
import android.location.Location
import android.location.LocationManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

enum class LocationStatus {
    FETCHING, DENIED, SUCCESS
}

data class LocationWithPermission(
    val location: Location? = null, val status: LocationStatus = LocationStatus.FETCHING
)

@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Location(content: @Composable (LocationWithPermission) -> Unit) {

    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    val location = remember { mutableStateOf(LocationWithPermission()) }
    val context = LocalContext.current

    val locationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        when {
            isGranted -> locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 2000, 10f
            ) {
                location.value = LocationWithPermission(it, LocationStatus.SUCCESS)
            }

            else -> location.value = LocationWithPermission(null, LocationStatus.DENIED)
        }
    }

    LaunchedEffect(locationPermissionState) {
        val status = locationPermissionState.status
        when {
            status.isGranted -> {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 2000, 10f
                ) {
                    location.value = LocationWithPermission(it, LocationStatus.SUCCESS)
                }
            }

            status.shouldShowRationale -> requestPermissionLauncher.launch(
                Manifest.permission.ACCESS_FINE_LOCATION
            )

            else -> requestPermissionLauncher.launch(
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
    }

    content.invoke(location.value)
}