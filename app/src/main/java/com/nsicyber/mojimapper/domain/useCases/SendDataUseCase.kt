package com.nsicyber.mojimapper.domain.useCases

import com.nsicyber.mojimapper.data.model.EmojiData
import com.nsicyber.mojimapper.domain.repository.FirestoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class SendDataUseCase @Inject
constructor(private val repository: FirestoreRepository) {
     operator fun invoke(data: EmojiData): Flow<Result<String>> =
            flow {
                try {
                    repository.sendData(data)
                        .collect { result1 ->
                            emit(result1)
                        }
                } catch (e: Exception) {
                    emit(Result.failure(e))
                }
            }

}

