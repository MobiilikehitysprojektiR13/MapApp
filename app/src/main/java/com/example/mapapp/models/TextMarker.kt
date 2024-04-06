package com.example.mapapp.models
import org.osmdroid.util.GeoPoint

data class TextMarker(
    val geoPoint: GeoPoint,
    val text: String
)