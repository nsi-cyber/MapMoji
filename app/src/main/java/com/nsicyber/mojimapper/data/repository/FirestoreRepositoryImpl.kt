package com.nsicyber.mojimapper.data.repository

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.nsicyber.mojimapper.data.model.EmojiData
import com.nsicyber.mojimapper.domain.mapFunctions.toDomainModel
import com.nsicyber.mojimapper.domain.model.EmojiMapModel
import com.nsicyber.mojimapper.domain.repository.FirestoreRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import org.imperiumlabs.geofirestore.GeoFirestore
import org.imperiumlabs.geofirestore.listeners.GeoQueryDataEventListener
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class FirestoreRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : FirestoreRepository {

    private val geoFirestore: GeoFirestore by lazy {
        GeoFirestore(firestore.collection("emojis"))
    }


   override suspend fun deleteOldDataFromFirestore(
    ) {


        firestore.collection("emojis")
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


    override suspend fun sendData(data: EmojiData) {
        val documentRef = firestore.collection("emojis").document()
        documentRef.set(data.copy(id = documentRef.id)).await()
        geoFirestore.setLocation(
            documentRef.id,
            GeoPoint(data.latitude, data.longitude)
        )
    }

}
