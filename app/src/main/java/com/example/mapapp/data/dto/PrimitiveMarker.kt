package com.example.mapapp.data.dto

import kotlinx.serialization.Serializable
import org.osmdroid.util.GeoPoint

@Serializable
data class PrimitiveMarker(
    val id: Int,
    val geoPoint: PrimitiveGeoPoint,
    val text: String,
) {
    @Serializable
    data class PrimitiveGeoPoint(
        val latitude: Double,
        val longitude: Double
    ) {
        fun toOsmPoint(): GeoPoint = GeoPoint(latitude, longitude)
    }
}
