package com.example.mapapp.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.mapapp.models.TextMarker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.osmdroid.util.GeoPoint

class StoreMarkers(private val context: Context) {
    companion object {
        private val Context.dataStorage: DataStore<Preferences> by preferencesDataStore("markerStorage")
        val MARKERS_KEY = stringPreferencesKey("markers")
    }

    val getMarkers: Flow<List<TextMarker>> = context.dataStorage.data.map { preferences ->
        val serializedMarkers = preferences[MARKERS_KEY]
        serializedMarkers?.split(";")?.mapNotNull { markerString ->
            val markerComponents = markerString.split(",")
            if (markerComponents.size == 3) {
                val (latitude, longitude, name) = markerComponents
                TextMarker(GeoPoint(latitude.toDouble(), longitude.toDouble()), name)
            } else {
                // Log or handle invalid marker data
                null
            }
        } ?: emptyList()
    }
    suspend fun saveMarkers(markers: List<TextMarker>) {
        val serializedMarkers = markers.joinToString(";") { marker ->
            "${marker.geoPoint.latitude},${marker.geoPoint.longitude},${marker.text}"

        }
        context.dataStorage.edit { preferences ->
            preferences[MARKERS_KEY] = serializedMarkers
        }
    }
}
