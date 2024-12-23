package com.nsicyber.mojimapper.domain.useCases

import com.nsicyber.mojimapper.domain.repository.FirestoreRepository
import javax.inject.Inject

class DeleteOldDataUseCase @Inject
constructor(private val repository: FirestoreRepository) {
    suspend operator fun invoke() {
        repository.deleteOldDataFromFirestore()
    }
}