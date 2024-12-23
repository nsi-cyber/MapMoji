package com.nsicyber.mojimapper.presentation.cameraScreen

import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.nsicyber.mojimapper.presentation.MainEvent
import com.nsicyber.mojimapper.presentation.MainViewModel
import com.nsicyber.mojimapper.presentation.components.CustomButton
import com.nsicyber.mojimapper.ui.theme.IconLeftArrow
import com.nsicyber.mojimapper.ui.theme.IconPhotoShoot
import com.nsicyber.mojimapper.ui.theme.IconRotate
import com.nsicyber.mojimapper.utils.getCurrentLocation

@Composable
fun CameraScreen(
    mainViewModel: MainViewModel = hiltViewModel(),
    applicationContext: Context,
    onLocationRequest: () -> Unit,
    onBackPressed:()->Unit
) {

    val controller = remember {
        LifecycleCameraController(applicationContext).apply {
            setEnabledUseCases(
                CameraController.IMAGE_CAPTURE
            )
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AndroidView(
            factory = {
                PreviewView(it).apply {
                    this.controller = controller
                    controller.bindToLifecycle(lifecycleOwner)
                }
            },
            modifier = Modifier.fillMaxSize()
        )



        CustomButton(modifier = Modifier.padding(horizontal = 16.dp, vertical = 32.dp).align(Alignment.BottomStart), onClick = {
            onBackPressed()
        }, icon = IconLeftArrow)

        CustomButton(modifier = Modifier.padding(horizontal = 16.dp, vertical = 32.dp).align(Alignment.BottomEnd), onClick = {
            controller.cameraSelector =
                if (controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                    CameraSelector.DEFAULT_FRONT_CAMERA
                } else CameraSelector.DEFAULT_BACK_CAMERA
        }, icon = IconRotate)





        CustomButton(modifier = Modifier.padding(horizontal = 16.dp, vertical = 32.dp).align(Alignment.BottomCenter), icon = IconPhotoShoot,
            onClick = {
                getCurrentLocation(context = applicationContext, onLocationRetrieved = { gps ->
                    mainViewModel.onEvent(
                        MainEvent.TakePhoto(
                            controller = controller,
                            applicationContext = applicationContext,
                            latitude = gps.latitude,
                            longitude = gps.longitude
                        )
                    )

                }, onError = {


                }, requestLocation = {
                    onLocationRequest()
                })


            }
        )
    }


}

