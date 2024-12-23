package com.nsicyber.mojimapper.domain.useCases

import com.google.mlkit.vision.common.InputImage
import com.nsicyber.mojimapper.domain.repository.MlKitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class RecognizeEmojiUseCase @Inject constructor(
    private val repository: MlKitRepository
) {
    operator fun invoke(bitmap: InputImage): Flow<Result<Int?>> =
        flow {
            try {
                repository.recognizeEmoji(bitmap)
                    .collect { result1 ->
                        emit(result1)
                    }
            } catch (e: Exception) {
                emit(Result.failure(e))
            }
        }

}
