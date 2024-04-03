package com.example.mapapp.screens

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.mapapp.data.dto.PrimitiveMarker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object WaypointsViewModel : ViewModel() {
    private val _markers = MutableStateFlow(mutableSetOf<PrimitiveMarker>())
    val markers: StateFlow<Set<PrimitiveMarker>> = _markers.asStateFlow()

    val loaded = mutableStateOf(false)

    suspend fun loadMarkers(markers: Set<PrimitiveMarker>) {
        _markers.update {
            markers.toMutableSet()
        }
    }

    fun addMarker(marker: PrimitiveMarker) {
        _markers.update { current ->
            current.toMutableSet().apply { add(marker) }
        }
    }

    fun removeMarker(id: Int) {
        _markers.update { current ->
            current.filterNot { it.id == id }.toMutableSet()
        }
    }
}