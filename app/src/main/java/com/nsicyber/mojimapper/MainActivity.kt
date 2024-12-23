package com.nsicyber.mojimapper

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
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
                NavigationGraph(applicationContext = this,
                    requestCameraPermission = {
                        checkAndRequestCameraPermissionAndStatus(
                            activity = this@MainActivity,
                        )
                    }, requestLocationPermission = {
                        checkAndRequestGpsPermissionAndStatus(
                            activity = this@MainActivity,
                        )
                    })
            }
        }
    }


    private fun checkAndRequestGpsPermissionAndStatus(
        activity: Activity,
    ) {
        val hasLocationPermission = ContextCompat.checkSelfPermission(
            activity,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED

        if (!hasLocationPermission) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                100
            )
            return
        }
        val locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (!isGpsEnabled) {
            AlertDialog.Builder(activity)
                .setTitle("GPS Kapalı")
                .setMessage("Konum hizmetleri kapalı. Lütfen GPS'i etkinleştirin.")
                .setPositiveButton("Ayarlar") { _, _ ->
                    // Kullanıcıyı GPS ayarlarına yönlendir
                    activity.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
                .setNegativeButton("İptal") { _, _ -> }
                .setCancelable(false)
                .show()
        }
    }

    private fun checkAndRequestCameraPermissionAndStatus(
        activity: Activity,
    ) {
        val hasLocationPermission = ContextCompat.checkSelfPermission(
            activity,
            android.Manifest.permission.CAMERA
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED

        if (!hasLocationPermission) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(android.Manifest.permission.CAMERA),
                101
            )
            return

        }
    }



}


