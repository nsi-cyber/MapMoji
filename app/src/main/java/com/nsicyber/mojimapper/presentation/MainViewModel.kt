package com.nsicyber.mojimapper.presentation

import android.content.Context
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.common.InputImage
import com.nsicyber.mojimapper.data.model.EmojiData
import com.nsicyber.mojimapper.domain.useCases.DeleteOldDataUseCase
import com.nsicyber.mojimapper.domain.useCases.GetMapDataUseCase
import com.nsicyber.mojimapper.domain.useCases.RecognizeEmojiUseCase
import com.nsicyber.mojimapper.domain.useCases.SendDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val recognizeEmojiUseCase: RecognizeEmojiUseCase,
    private val getMapDataUseCase: GetMapDataUseCase,
    private val sendDataUseCase: SendDataUseCase,
    private val deleteOldDataUseCase: DeleteOldDataUseCase,
) : ViewModel() {

    private val _mainState = MutableStateFlow(MainState())
    val mainState: StateFlow<MainState> = _mainState.asStateFlow()


    fun onEvent(event: MainEvent) {
        when (event) {


            is MainEvent.LoadMap -> {
                loadNearbyMessages(
                    latitude = event.latitude,
                    longitude = event.longitude,
                    radius = event.radius
                )
            }

            is MainEvent.TakePhoto -> takePhoto(
                latitude = event.latitude,
                longitude = event.longitude,
                applicationContext = event.applicationContext,
                controller = event.controller
            )

            MainEvent.DeleteOldData -> deleteOldData()
        }
    }

    private fun deleteOldData(

    ) {
        viewModelScope.launch {
            deleteOldDataUseCase()
        }
    }

    private fun loadNearbyMessages(
        latitude: Double,
        longitude: Double,
        radius: Double,
    ) {
        viewModelScope.launch {
            getMapDataUseCase(latitude, longitude, radius).collect { objects ->


                _mainState.value = mainState.value.copy(
                    mapObject = objects,
                    lastLoadedLatitude = latitude,
                    lastLoadedLongitude = longitude
                )
            }
        }
    }


    private fun takePhoto(
        latitude: Double,
        longitude: Double,
        applicationContext: Context,
        controller: LifecycleCameraController,
    ) {
        controller.takePicture(
            ContextCompat.getMainExecutor(applicationContext),
            object : ImageCapture.OnImageCapturedCallback() {
                @OptIn(ExperimentalGetImage::class)
                override fun onCaptureSuccess(image: ImageProxy) {
                    super.onCaptureSuccess(image)
                    val inputImage = InputImage.fromMediaImage(
                        image.image!!,
                        image.imageInfo.rotationDegrees
                    )
                    viewModelScope.launch {
                        recognizeEmojiUseCase(inputImage).collect { result ->
                            result.fold(
                                onSuccess = { detectedEmoji ->
                                    detectedEmoji?.let {
                                        sendDataUseCase(
                                            EmojiData(
                                                emoji = detectedEmoji,
                                                timestamp = System.currentTimeMillis(),
                                                longitude = longitude,
                                                latitude = latitude
                                            )
                                        ).collect { messageResponse ->
                                            messageResponse.fold(onSuccess = {

                                                _mainState.value = mainState.value.copy(
                                                   onSuccess = true
                                                )
                                            },
                                                onFailure = {

                                                }
                                            )

                                        }
                                    }
                                },
                                onFailure = {

                                }
                            )

                        }
                    }

                }

                override fun onError(exception: ImageCaptureException) {
                    super.onError(exception)
                    Log.e("Camera", "Couldn't take photo: ", exception)
                }
            }
        )
    }

}
