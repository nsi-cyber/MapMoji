package com.nsicyber.mojimapper.domain.repository

import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.flow.Flow

interface MlKitRepository {
    fun recognizeEmoji(inputImage: InputImage): Flow<Result<Int?>>
}
