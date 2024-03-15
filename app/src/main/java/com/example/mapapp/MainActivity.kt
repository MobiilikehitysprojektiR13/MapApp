package com.example.mapapp

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.utsman.osmandcompose.Marker
import com.utsman.osmandcompose.OpenStreetMap
import com.utsman.osmandcompose.rememberCameraState
import com.utsman.osmandcompose.rememberMarkerState
import org.osmdroid.util.GeoPoint


class MainActivity : AppCompatActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
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

                val depokMarkerState = rememberMarkerState(
                    geoPoint = GeoPoint(-6.3970066, 106.8224316)
                )

                val depokIcon: Drawable? by remember {
                    mutableStateOf(context.getDrawable(org.osmdroid.library.R.drawable.moreinfo_arrow))
                }

                OpenStreetMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraState = cameraState
                ) {
                    Marker(
                        state = depokMarkerState,
                        icon = depokIcon
                    )
                }
            }
        }
    }
}