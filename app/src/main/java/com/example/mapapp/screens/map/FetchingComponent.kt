package com.example.mapapp.screens.map

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.time.delay
import java.time.Duration

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FetchingComponent() {
    val fetchText = remember { mutableStateOf("Fetching location") }
    val counter = remember { mutableIntStateOf(0) }

    LaunchedEffect(fetchText.value) {
        while (true) {
            delay(Duration.ofMillis(500))
            counter.intValue = (counter.intValue + 1) % 4
            fetchText.value = "Fetching location" + ".".repeat(counter.intValue)
        }
    }

    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = fetchText.value, fontSize = 20.sp)
    }
}