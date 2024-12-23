package com.nsicyber.mojimapper.domain.repository


import com.nsicyber.mojimapper.data.model.EmojiData
import com.nsicyber.mojimapper.domain.model.EmojiMapModel
import kotlinx.coroutines.flow.Flow


interface FirestoreRepository {
    suspend fun deleteOldDataFromFirestore()

     fun getMapData(
        latitude: Double,
        longitude: Double,
        radius: Double
    ): Flow<List<EmojiMapModel>>


    suspend fun sendData(data: EmojiData): Flow<Result<String>>


}
