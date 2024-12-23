package com.nsicyber.mojimapper.domain.useCases

import com.nsicyber.mojimapper.domain.model.EmojiMapModel
import com.nsicyber.mojimapper.domain.repository.FirestoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMapDataUseCase  @Inject
constructor(private val repository: FirestoreRepository) {
    operator fun invoke(
        latitude: Double,
        longitude: Double,
        radius: Double,
    ): Flow<List<EmojiMapModel>> {
        return repository.getMapData(latitude, longitude, radius)
    }
}