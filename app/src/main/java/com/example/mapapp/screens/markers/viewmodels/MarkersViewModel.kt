package com.example.mapapp.screens.markers.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.mapapp.models.TextMarker


object MarkersViewModel : ViewModel() {

    private val _markers = mutableListOf<TextMarker>()
    val markers: List<TextMarker> get() = _markers.toList()

    val loaded = mutableStateOf(false)
    val loading = mutableStateOf(false)

    fun getMarkers(markers: List<TextMarker>) {
                _markers.clear()
                _markers.addAll(markers)
    }

    fun addMarker(marker: TextMarker) {
            _markers.add(marker)
    }
}
