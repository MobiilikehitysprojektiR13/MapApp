package com.example.mapapp.models
import org.osmdroid.util.GeoPoint

data class TextMarker(
    val index: Int,
    val geoPoint: GeoPoint,
    val text: String
)