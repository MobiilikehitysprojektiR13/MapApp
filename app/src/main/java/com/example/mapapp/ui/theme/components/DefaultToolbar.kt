package com.example.mapapp.ui.theme.components


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.example.mapapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultToolbar(navController: NavController, root: Boolean) {

    val context = LocalContext.current

    TopAppBar(
        title = {
            Text(text = context.getString(R.string.app_name),
                textAlign = TextAlign.Center)
        },
        navigationIcon = {
            if (!root) IconButton(onClick = {
                navController.navigateUp()
            }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
            }
        },
    )
}