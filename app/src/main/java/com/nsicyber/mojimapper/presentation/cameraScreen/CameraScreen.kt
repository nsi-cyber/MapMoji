package com.nsicyber.mojimapper.presentation.cameraScreen

import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.remember
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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.nsicyber.mojimapper.presentation.MainEvent
import com.nsicyber.mojimapper.presentation.MainViewModel
import com.nsicyber.mojimapper.ui.theme.IconCross
import com.nsicyber.mojimapper.ui.theme.IconPhoto
import com.nsicyber.mojimapper.ui.theme.IconRotate
import com.nsicyber.mojimapper.utils.getCurrentLocation

@Composable
fun CameraScreen(
    mainViewModel: MainViewModel = hiltViewModel(),
    applicationContext: Context,
    onLocationRequest: () -> Unit,
    onBackPressed: () -> Unit
) {

    val state by mainViewModel.mainState.collectAsState()

    LaunchedEffect(state.onSuccess) {
        if (state.onSuccess == true) {
            onBackPressed()
        }
    }

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
       Box(){
           AndroidView(
               factory = {
                   PreviewView(it).apply {
                       this.controller = controller
                       controller.bindToLifecycle(lifecycleOwner)
                   }
               },
               modifier = Modifier.fillMaxSize()
           )
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
                   .height(50.dp) // Height of the gradient
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




        Box(
            modifier = Modifier.padding(16.dp)      .size(48.dp)
                .align(Alignment.TopEnd)  .border(
                    width = 2.dp,
                    color = Color.White.copy(alpha = 0.6f),
                    shape = CircleShape
                )
                .clip(CircleShape).clickable { onBackPressed() }
                .background(Color.Black.copy(alpha = 0.5f))

        ){
                    Image(modifier = Modifier.fillMaxSize().padding(14.dp), painter = painterResource(IconCross), contentDescription = "")
                }


    Row(modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {


Spacer(modifier = Modifier.padding(16.dp).size(64.dp))

        Row (
            modifier = Modifier  .padding(horizontal = 16.dp, vertical = 32.dp)
                .clip(CircleShape)
                .clickable {
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
                .background(Color.White)
                .border(
                    width = 2.dp,
                    color = Color.Black.copy(alpha = 0.2f),
                    shape = CircleShape
                )
                .padding(24.dp), horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {


            Image(
                modifier = Modifier.size(40.dp),
                painter = painterResource(IconPhoto),
                contentDescription = "",
                colorFilter = ColorFilter.tint(Color.Black)
            )
            Text(text = "Add Moji", style = TextStyle().copy(Color.Black, fontSize = 20.sp, fontWeight = FontWeight.Bold))

        }

        Box(
            modifier = Modifier.padding(16.dp) .size(48.dp)
                .border(
                    width = 2.dp,
                    color = Color.White.copy(alpha = 0.6f),
                    shape = CircleShape
                )
                .clip(CircleShape).clickable {
                    controller.cameraSelector =
                        if (controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                            CameraSelector.DEFAULT_FRONT_CAMERA
                        } else CameraSelector.DEFAULT_BACK_CAMERA
                }
                .background(Color.Black.copy(alpha = 0.5f))

                .padding(8.dp)
        ){
            Image(modifier = Modifier.fillMaxSize(), painter = painterResource(IconRotate), contentDescription = "")
        }
    }



    }


}



