package com.nsicyber.mojimapper.data.repository

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.nsicyber.mojimapper.common.Constants
import com.nsicyber.mojimapper.data.model.EmojiData
import com.nsicyber.mojimapper.domain.mapFunctions.toDomainModel
import com.nsicyber.mojimapper.domain.model.EmojiMapModel
import com.nsicyber.mojimapper.domain.repository.FirestoreRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.imperiumlabs.geofirestore.GeoFirestore
import org.imperiumlabs.geofirestore.extension.setLocation
import org.imperiumlabs.geofirestore.listeners.GeoQueryDataEventListener
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class FirestoreRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : FirestoreRepository {

    private val geoFirestore: GeoFirestore by lazy {
        GeoFirestore(firestore.collection(Constants.Firestore.COLLECTION))
    }


   override suspend fun deleteOldDataFromFirestore(
    ) {


        firestore.collection(Constants.Firestore.COLLECTION)
            .whereLessThan("timestamp", System.currentTimeMillis() - (2 * 60 * 60 * 1000))
            .get()
            .addOnSuccessListener { querySnapshot ->
                val batch = firestore.batch()
                for (document in querySnapshot.documents) {
                    batch.delete(document.reference)
                }
                batch.commit()
            }

    }

    override fun getMapData(
        latitude: Double,
        longitude: Double,
        radius: Double
    ): Flow<List<EmojiMapModel>> {
        return callbackFlow {
            val geoQuery =
                geoFirestore.queryAtLocation(GeoPoint(latitude, longitude), radius)
            val emojiList = mutableListOf<EmojiMapModel>()

            geoQuery.addGeoQueryDataEventListener(object : GeoQueryDataEventListener {

                override fun onDocumentChanged(
                    documentSnapshot: DocumentSnapshot,
                    location: GeoPoint
                ) {
                }

                override fun onDocumentEntered(
                    documentSnapshot: DocumentSnapshot,
                    location: GeoPoint
                ) {
                    val emojiData = documentSnapshot.toObject(EmojiData::class.java)
                    if (emojiData?.id != "") {

                        emojiList.add(emojiData?.toDomainModel()!!)

                        trySend(emojiList.toList())


                    }
                }

                override fun onDocumentExited(documentSnapshot: DocumentSnapshot) {
                    val emojiData = documentSnapshot.toObject(EmojiData::class.java)
                    if (emojiData?.id != "") {

                        emojiList.add(emojiData?.toDomainModel()!!)
                        trySend(emojiList.toList())
                    }
                }


                override fun onDocumentMoved(
                    documentSnapshot: DocumentSnapshot,
                    location: GeoPoint
                ) {
                }

                override fun onGeoQueryError(exception: Exception) {
                    close(exception)
                }

                override fun onGeoQueryReady() {}
            })

            awaitClose { geoQuery.removeAllListeners() }
        }
    }




    override suspend fun sendData(data: EmojiData): Flow<Result<String>> = callbackFlow {
        val documentRef = firestore.collection(Constants.Firestore.COLLECTION).document()
        val geoPoint = GeoPoint(data.latitude, data.longitude)

        try {

            // Save the data in Firestore
            documentRef.set(data.copy(id = documentRef.id))
                .addOnSuccessListener {
                    // Data saved, now set the GeoFirestore location
                    geoFirestore.setLocation(documentRef.id, geoPoint) { locationException ->
                        if (locationException != null) {
                            // Emit error if GeoFirestore operation fails
                            trySend(Result.failure(locationException))
                            close(locationException)
                        } else {
                            // Successfully completed
                            trySend(Result.success("Data sent successfully"))
                            close()
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    // Emit error if Firestore operation fails
                    trySend(Result.failure(exception))
                    close(exception)
                }
        } catch (e: Exception) {
            // Emit error for unexpected exceptions
            trySend(Result.failure(e))
            close(e)
        }

        // Close the flow when finished
        awaitClose { }
    }

}
