package com.nsicyber.mojimapper.presentation

import android.content.Context
import androidx.camera.view.LifecycleCameraController

sealed class MainEvent {
    data class LoadMap(
        val latitude: Double,
        val longitude: Double,
        val radius: Double,
    ) : MainEvent()

    data class TakePhoto(
        val latitude: Double,
        val longitude: Double,
        val applicationContext: Context,
        val controller: LifecycleCameraController
    ) : MainEvent()

    data object DeleteOldData: MainEvent()
}