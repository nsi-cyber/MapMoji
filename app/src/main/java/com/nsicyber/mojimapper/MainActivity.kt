package com.nsicyber.mojimapper

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.nsicyber.mojimapper.presentation.navigation.NavigationGraph
import com.nsicyber.mojimapper.ui.theme.MojiMapperTheme
import dagger.hilt.android.AndroidEntryPoint


private const val CAMERA_PERMISSION_REQUEST_CODE = 101
private const val LOCATION_PERMISSION_REQUEST_CODE = 102

@AndroidEntryPoint

class MainActivity : ComponentActivity() {
    private val hasCameraPermission = mutableStateOf(false)
    private val hasLocationPermission = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MojiMapperTheme {
                LaunchedEffect(Unit) {
                    requestCameraPermission(
                        this@MainActivity,
                        onPermissionGranted = {
                            hasCameraPermission.value = true

                        }
                    )
                    requestLocationPermission(
                        this@MainActivity,
                        onPermissionGranted = {
                            hasLocationPermission.value = true

                        }
                    )
                }
                NavigationGraph(applicationContext = this,
                    requestCameraPermission = {
                        requestCameraPermission(
                            this,
                            onPermissionGranted = {
                                hasCameraPermission.value = true

                            }
                        )
                    }, requestLocationPermission = {
                        requestLocationPermission(
                            this,
                            onPermissionGranted = {
                                hasLocationPermission.value = true

                            }
                        )
                    })
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_PERMISSION_REQUEST_CODE -> {
                handleCameraPermissionResult(
                    requestCode,
                    grantResults,
                    onPermissionGranted = { hasCameraPermission.value = true },
                    onPermissionDenied = { hasCameraPermission.value = false }
                )
            }

            LOCATION_PERMISSION_REQUEST_CODE -> {
                handleLocationPermissionResult(
                    requestCode,
                    grantResults,
                    onPermissionGranted = { hasLocationPermission.value = true },
                    onPermissionDenied = { hasLocationPermission.value = false }
                )
            }
        }
    }
}

fun requestCameraPermission(
    activity: Activity,
    onPermissionGranted: () -> Unit,
) {
    val locationPermission = android.Manifest.permission.CAMERA

    if (ContextCompat.checkSelfPermission(
            activity,
            locationPermission
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        // Kullanıcı izni daha önce verdi
        onPermissionGranted()
    } else {
        // İzin verilmemiş, kullanıcıdan izin istemek için bir popup göster
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(locationPermission),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }
}

fun requestLocationPermission(
    activity: Activity,
    onPermissionGranted: () -> Unit,
) {
    val locationPermission = android.Manifest.permission.ACCESS_FINE_LOCATION

    if (ContextCompat.checkSelfPermission(
            activity,
            locationPermission
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        // Kullanıcı izni daha önce verdi
        onPermissionGranted()
    } else {
        // İzin verilmemiş, kullanıcıdan izin istemek için bir popup göster
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(locationPermission),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }
}

fun handleCameraPermissionResult(
    requestCode: Int,
    grantResults: IntArray,
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit
) {
    if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Lokasyon izni verildi
            onPermissionGranted()
        } else {
            // Lokasyon izni reddedildi
            onPermissionDenied()
        }
    }
}

fun handleLocationPermissionResult(
    requestCode: Int,
    grantResults: IntArray,
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit
) {
    if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Lokasyon izni verildi
            onPermissionGranted()
        } else {
            // Lokasyon izni reddedildi
            onPermissionDenied()
        }
    }
}

