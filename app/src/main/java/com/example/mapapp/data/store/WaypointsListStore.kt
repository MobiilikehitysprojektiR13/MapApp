package com.example.mapapp.data.store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.mapapp.data.dto.PrimitiveMarker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class WaypointsListStore(private val context: Context) {

    private val json = Json

    companion object {
        private val Context.dataStorage: DataStore<Preferences> by preferencesDataStore("waypoints_list")
        val WAYPOINTS_LIST_TOKEN_KEY = stringPreferencesKey("waypoints")
    }

    val getWaypointsList: Flow<Set<PrimitiveMarker>> = context.dataStorage.data.map { preferences ->
        preferences[WAYPOINTS_LIST_TOKEN_KEY]?.let {
            json.decodeFromString<Set<PrimitiveMarker>>(it)
        } ?: emptySet()
    }

    suspend fun saveWaypointList(waypointsList: Set<PrimitiveMarker>) {
        context.dataStorage.edit { preferences ->
            preferences[WAYPOINTS_LIST_TOKEN_KEY] = json.encodeToString(waypointsList)
        }
    }
}