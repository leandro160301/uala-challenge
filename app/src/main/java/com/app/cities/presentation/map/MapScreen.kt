package com.app.cities.presentation.map

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import com.app.cities.domain.model.City
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    city: City,
    onBack: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val mapInstanceKey = "${city.id}-${configuration.orientation}"

    Column(modifier = modifier.fillMaxSize()) {

        TopAppBar(
            title = {
                Text("${city.name}, ${city.country}")
            },
            navigationIcon = {
                onBack?.let {
                    IconButton(onClick = it) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            }
        )

        key(mapInstanceKey) {
            val targetPosition = LatLng(city.lat, city.lon)
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(targetPosition, MAP_ZOOM)
            }
            val markerState = remember(targetPosition) {
                MarkerState(position = targetPosition)
            }
            var isMapLoaded by remember { mutableStateOf(false) }

            LaunchedEffect(isMapLoaded) {
                if (!isMapLoaded) return@LaunchedEffect

                cameraPositionState.move(
                    update = CameraUpdateFactory.newLatLngZoom(targetPosition, MAP_ZOOM)
                )
            }

            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                googleMapOptionsFactory = {
                    GoogleMapOptions().camera(
                        CameraPosition.fromLatLngZoom(targetPosition, MAP_ZOOM)
                    )
                },
                onMapLoaded = {
                    isMapLoaded = true
                }
            ) {
                MapEffect(city.id, configuration.orientation, isMapLoaded) { googleMap ->
                    googleMap.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(targetPosition, MAP_ZOOM)
                    )
                }

                Marker(
                    state = markerState,
                    title = city.name
                )
            }
        }
    }
}

private const val MAP_ZOOM = 10f
