package com.example.mapapp.screens.markers.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.mapapp.models.TextMarker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update


object MarkersViewModel : ViewModel() {

    private val _markers = MutableStateFlow<List<TextMarker>>(emptyList())
    val markers: StateFlow<List<TextMarker>> = _markers

    val loaded = mutableStateOf(false)
    val loading = mutableStateOf(false)

    fun getMarkers(markers: List<TextMarker>) {
        _markers.update {
            markers.toMutableList()
        }
    }

    fun addMarker(marker: TextMarker) {
        _markers.update { current ->
            current.toMutableList().apply { add(marker) }
        }
    }

    fun getNextIndex(): Int = _markers.value.maxOfOrNull { it.index }?.plus(1) ?: 0

    fun deleteMarker(id: Int) {
        _markers.update { current ->
            current.filterNot { it.index == id }.toMutableList()
        }
    }
}
