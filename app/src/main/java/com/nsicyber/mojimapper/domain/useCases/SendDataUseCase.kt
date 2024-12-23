package com.nsicyber.mojimapper.domain.useCases

import com.nsicyber.mojimapper.data.model.EmojiData
import com.nsicyber.mojimapper.domain.repository.FirestoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject



class SendDataUseCase @Inject
constructor(private val repository: FirestoreRepository) {
    suspend operator fun invoke(data: EmojiData) {
        repository.sendData(data)
    }
}

