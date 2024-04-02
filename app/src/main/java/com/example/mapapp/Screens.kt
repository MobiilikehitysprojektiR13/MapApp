package com.example.mapapp

sealed class  Screens(val route: String) {
    data object Map : Screens("map")
    data object Markers : Screens("markers")
}