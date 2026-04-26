package com.app.cities.presentation.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.cities.domain.model.City
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapScreen(
    city: City,
    onBack: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    val cameraPositionState = rememberCameraPositionState()

    LaunchedEffect(city) {
        cameraPositionState.animate(
            update = CameraUpdateFactory.newLatLngZoom(
                LatLng(city.lat, city.lon),
                10f
            )
        )
    }

    Box(modifier.fillMaxSize()) {

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            Marker(
                state = MarkerState(
                    position = LatLng(city.lat, city.lon)
                ),
                title = city.name
            )
        }

        onBack?.let {
            Button(
                onClick = it,
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Back")
            }
        }
    }
}