package com.example.mapapp

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.utsman.osmandcompose.*
import org.osmdroid.util.GeoPoint
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class TextMarker(
    val geoPoint: GeoPoint,
    val text: String
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

                val markers = remember { mutableStateListOf<TextMarker>() }
                var showDialog by remember { mutableStateOf(false) }
                var tempGeoPoint by remember { mutableStateOf<GeoPoint?>(null) }
                var textInput by remember { mutableStateOf("") }

                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
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
                                        markers.add(TextMarker(tempGeoPoint!!, textInput))
                                        showDialog = false
                                        tempGeoPoint = null
                                        textInput = ""
                                    }
                                }
                            ) { Text("Add") }
                        },
                        dismissButton = {
                            Button(onClick = {
                                showDialog = false
                                textInput = ""
                            }) { Text("Cancel") }
                        }
                    )
                }

                OpenStreetMap(
                    onMapClick = { geoPoint ->
                        tempGeoPoint = geoPoint
                        showDialog = true
                    },
                    modifier = Modifier.fillMaxSize(),
                    cameraState = cameraState
                ) {
                    markers.forEach { textMarker ->
                        val markerState = rememberMarkerState(geoPoint = textMarker.geoPoint)
                        Marker(
                            state = markerState,
                            icon = depokIcon,
                            title = textMarker.text
                        ) {Column(
                            modifier = Modifier
                                .size(100.dp)
                                .background(color = Color.Gray, shape = RoundedCornerShape(7.dp)),
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
        }
    }
}
