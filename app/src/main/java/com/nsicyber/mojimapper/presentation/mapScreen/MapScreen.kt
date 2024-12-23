package com.nsicyber.mojimapper.presentation.mapScreen

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.nsicyber.mojimapper.presentation.MainEvent
import com.nsicyber.mojimapper.presentation.MainViewModel
import com.nsicyber.mojimapper.presentation.components.CustomButton
import com.nsicyber.mojimapper.ui.theme.IconGPS
import com.nsicyber.mojimapper.ui.theme.IconPhotoShoot
import com.nsicyber.mojimapper.utils.getCurrentLocation
import kotlinx.coroutines.launch
import kotlin.math.pow


@Composable
fun MapScreen(
    mainViewModel: MainViewModel = hiltViewModel(),
    applicationContext: Context,
    onLocationRequest: () -> Unit,
    onCameraPage: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val state by mainViewModel.mainState.collectAsState()


    val cameraPositionState = rememberCameraPositionState {}

    var uiSettings by remember {
        mutableStateOf(MapUiSettings(zoomControlsEnabled = false))
    }

    var properties by remember {
        mutableStateOf(MapProperties(mapType = MapType.SATELLITE))
    }

    LaunchedEffect(Unit) {
        getCurrentLocation(
            context = applicationContext,
            onLocationRetrieved = { location ->
                val userLatLng = LatLng(location.latitude, location.longitude)
                mainViewModel.onEvent(
                    MainEvent.LoadMap(
                        latitude = location.latitude,
                        longitude = location.longitude,
                        radius = calculateVisibleRadius(cameraPositionState.position.zoom)
                    )
                )
                scope.launch {
                    cameraPositionState.animate(
                        CameraUpdateFactory.newLatLngZoom(
                            userLatLng,
                            17f
                        ), 1000
                    )

                }

            },
            onError = { onLocationRequest() },
            requestLocation = onLocationRequest
        )

    }

    Box(modifier = Modifier.fillMaxSize()) {


        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = properties,
            uiSettings = uiSettings
        ) {
            repeat(state.mapObject.size) { index ->

                with(state.mapObject) {
                    MarkerComposable(
                        state = MarkerState(
                            position = LatLng(
                                get(index).latitude,
                                get(index).longitude
                            )
                        ),
                    ) {


                        Text(
                            text = get(index).emoji,
                            fontSize = 38.sp
                        )


                    }
                }

            }

        }


        if (cameraPositionState.isMoving == false) {
            if (cameraPositionState.position.target.latitude != state.lastLoadedLatitude &&
                cameraPositionState.position.target.longitude != state.lastLoadedLongitude
            ) {
                Box(modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 32.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable {
                        mainViewModel.onEvent(
                            MainEvent.LoadMap(
                                latitude = cameraPositionState.position.target.latitude,
                                longitude = cameraPositionState.position.target.longitude,
                                radius = calculateVisibleRadius(cameraPositionState.position.zoom)
                            )
                        )
                    }

                    .align(Alignment.BottomCenter)

                    .background(Color.White)
                    .padding(12.dp)
                ) {
                    Text(text = "Search for Moji's", color = Color.Black)

                }
            }
        }


        CustomButton(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 32.dp)
                .align(Alignment.BottomStart), onClick = {
                getCurrentLocation(
                    context = applicationContext,
                    onLocationRetrieved = { location ->
                        val userLatLng = LatLng(location.latitude, location.longitude)

                        scope.launch {
                            cameraPositionState.animate(
                                CameraUpdateFactory.newLatLngZoom(
                                    userLatLng,
                                    17f
                                ), 1000
                            )

                        }

                    },
                    onError = {onLocationRequest()},
                    requestLocation = onLocationRequest
                )
            }, icon = IconGPS
        )


        CustomButton(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 32.dp)
                .align(Alignment.BottomEnd), onClick = {
                onCameraPage()
            }, icon = IconPhotoShoot
        )
    }


}

fun calculateVisibleRadius(zoom: Float): Double {
    val earthCircumference = 40_075_017
    return ((earthCircumference / (2.0.pow(zoom.toDouble()) * 256)) * 1000)
}