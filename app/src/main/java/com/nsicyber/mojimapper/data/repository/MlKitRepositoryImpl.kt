package com.nsicyber.mojimapper.data.repository

import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeler
import com.nsicyber.mojimapper.domain.repository.MlKitRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await


class MlKitRepositoryImpl(
    private val imageLabeler: ImageLabeler
) : MlKitRepository {

    override fun recognizeEmoji(inputImage: InputImage): Flow<Result<Int?>> = flow {
        try {
            val emojiList = imageLabeler.process(inputImage).await()
            val face = emojiList.firstOrNull()
            emit(Result.success(face?.index))

        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)


}
