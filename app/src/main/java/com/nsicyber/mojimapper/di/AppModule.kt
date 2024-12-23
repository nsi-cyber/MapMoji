package com.nsicyber.mojimapper.di

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.google.mlkit.vision.label.ImageLabeler
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.nsicyber.mojimapper.data.repository.FirestoreRepositoryImpl
import com.nsicyber.mojimapper.data.repository.MlKitRepositoryImpl
import com.nsicyber.mojimapper.domain.repository.FirestoreRepository
import com.nsicyber.mojimapper.domain.repository.MlKitRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }


    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context = context


    @Provides
    @Singleton
    fun provideFirestoreRepository(
        firestore: FirebaseFirestore
    ): FirestoreRepository {
        return FirestoreRepositoryImpl(firestore)
    }


    @Provides
    @Singleton
    fun provideImageLabeler(): ImageLabeler {
        val options = ImageLabelerOptions.Builder().setConfidenceThreshold(0.4f).build()
        return ImageLabeling.getClient(options)
    }

    @Provides
    @Singleton
    fun provideMlKitRepository(imageLabeler: ImageLabeler): MlKitRepository {
        return MlKitRepositoryImpl(imageLabeler)
    }


}
