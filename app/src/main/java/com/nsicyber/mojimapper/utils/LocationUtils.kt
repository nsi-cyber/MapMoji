package com.nsicyber.mojimapper.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

fun getCurrentLocation(
    context: Context,
    onLocationRetrieved: (Location) -> Unit,
    onError: (String) -> Unit,
    requestLocation:()->Unit
) {
    val fusedLocationProviderClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)



    // Request the last known location
    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        requestLocation()
        return
    }
    fusedLocationProviderClient.lastLocation
        .addOnSuccessListener { location: Location? ->
            if (location != null) {
                onLocationRetrieved(location)
            } else {
                onError("Unable to retrieve location")
            }
        }
        .addOnFailureListener { exception ->
            onError("Failed to get location: ${exception.message}")
        }
}