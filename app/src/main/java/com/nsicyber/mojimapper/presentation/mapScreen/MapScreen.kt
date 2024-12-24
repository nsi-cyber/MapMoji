package com.nsicyber.mojimapper.presentation.mapScreen

import android.content.Context
import android.location.Location
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
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
import com.nsicyber.mojimapper.ui.theme.IconCompass
import com.nsicyber.mojimapper.ui.theme.IconGps
import com.nsicyber.mojimapper.ui.theme.IconLocation
import com.nsicyber.mojimapper.ui.theme.IconPhoto
import com.nsicyber.mojimapper.ui.theme.IconTerrain
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
        mutableStateOf(MapUiSettings(zoomControlsEnabled = false, compassEnabled = false))
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
        Box {
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Black.copy(alpha = 0.6f), Color.Transparent),
                            startY = 0f,
                            endY = 100f
                        )
                    )
                    .align(Alignment.TopCenter)
            )


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.4f)),
                            startY = 0f,
                            endY = 100f
                        )
                    )
                    .align(Alignment.BottomCenter)
            )
        }



        if (cameraPositionState.isMoving == false) {
            if (isDistanceMoreThan15Meters(
                    lat1 = cameraPositionState.position.target.latitude,
                    lat2 = state.lastLoadedLatitude,
                    lon1 = cameraPositionState.position.target.longitude,
                    lon2 = state.lastLoadedLongitude
                ) || state.lastLoadedRadius < calculateVisibleRadius(cameraPositionState.position.zoom)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 32.dp)
                        .clip(CircleShape)
                        .clickable {
                            mainViewModel.onEvent(
                                MainEvent.LoadMap(
                                    latitude = cameraPositionState.position.target.latitude,
                                    longitude = cameraPositionState.position.target.longitude,
                                    radius = calculateVisibleRadius(cameraPositionState.position.zoom)
                                )
                            )
                        }

                        .align(Alignment.TopCenter)

                        .background(Color.White)
                        .padding(vertical = 12.dp, horizontal = 24.dp)
                ) {
                    Image(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(IconLocation),
                        contentDescription = ""
                    )

                    Text(text = "Search for Moji's", color = Color.Black)

                }
            }
        }





        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 16.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.End
        ) {

            // Map Type Toggle Button
            CustomButton(
                modifier = Modifier,
                onClick = {
                    properties = properties.copy(
                        mapType = when (properties.mapType) {
                            MapType.NORMAL -> MapType.SATELLITE
                            MapType.SATELLITE -> MapType.TERRAIN
                            MapType.TERRAIN -> MapType.NORMAL
                            else -> MapType.NORMAL
                        }
                    )
                },
                icon = IconTerrain
            )
            CustomButton(
                modifier = Modifier, onClick = {

                    scope.launch {
                        cameraPositionState.animate(
                            CameraUpdateFactory.newCameraPosition(
                                CameraPosition.Builder()
                                    .target(cameraPositionState.position.target)
                                    .zoom(cameraPositionState.position.zoom)
                                    .bearing(0f)
                                    .tilt(cameraPositionState.position.tilt)
                                    .build()
                            )
                        )

                    }


                }, icon = IconCompass
            )


            CustomButton(
                modifier = Modifier, onClick = {
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
                        onError = { onLocationRequest() },
                        requestLocation = onLocationRequest
                    )
                }, icon = IconGps
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(CircleShape)
                    .border(
                        width = 2.dp,
                        color = Color.White.copy(alpha = 0.6f),
                        shape = CircleShape
                    )
                    .clickable {
                        onCameraPage()
                    }
                    .background(Color.Black.copy(alpha = 0.4f))

                    .padding(24.dp), horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {


                Image(
                    modifier = Modifier
                        .size(40.dp)
                        .padding(end = 12.dp),
                    painter = painterResource(IconPhoto),
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(Color.White)
                )
                Text(
                    text = "Add Moji",
                    style = TextStyle().copy(
                        Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

            }
        }


    }


}

fun isDistanceMoreThan15Meters(
    lat1: Double, lon1: Double,
    lat2: Double, lon2: Double
): Boolean {
    val result = FloatArray(1)
    Location.distanceBetween(lat1, lon1, lat2, lon2, result)
    return result[0] > 100
}

fun calculateVisibleRadius(zoom: Float): Double {
    val earthCircumference = 40_075_017
    return ((earthCircumference / (2.0.pow(zoom.toDouble()) * 256)) * 1000)
}