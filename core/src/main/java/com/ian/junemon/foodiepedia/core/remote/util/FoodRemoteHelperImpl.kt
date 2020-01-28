package com.ian.junemon.foodiepedia.core.remote.util

import android.net.Uri
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference
import com.junemon.model.DataHelper
import com.junemon.model.FirebaseResult
import com.junemon.model.data.FoodEntity
import com.junemon.model.data.dto.mapToRemoteDomain
import com.junemon.model.domain.FoodRemoteDomain
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

/**
 * Created by Ian Damping on 07,January,2020
 * Github https://github.com/iandamping
 * Indonesia.
 */
class FoodRemoteHelperImpl @Inject constructor(
    private val storagePlaceReference: StorageReference,
    private val databasePlaceReference: DatabaseReference
) : FoodRemoteHelper {

    @ExperimentalCoroutinesApi
    override suspend fun getFirebaseData(): Flow<DataHelper<List<FoodRemoteDomain>>> {
        val container: MutableList<FoodEntity> = mutableListOf()
        return callbackFlow {
            databasePlaceReference.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    close(p0.toException())
                }

                override fun onDataChange(p0: DataSnapshot) {
                    p0.children.forEach {
                        container.add(it.getValue(FoodEntity::class.java)!!)
                    }
                    if (!this@callbackFlow.channel.isClosedForSend) {
                        offer(DataHelper.RemoteSourceValue(container.mapToRemoteDomain()))
                    }
                }
            })
            awaitClose { cancel() }
        }
    }

    @ExperimentalCoroutinesApi
    override fun uploadFirebaseData(
        data: FoodRemoteDomain,
        imageUri: Uri
    ): Flow<FirebaseResult<Nothing>> {
        return callbackFlow {
            val reference = storagePlaceReference.child(imageUri.lastPathSegment!!)
            reference.putFile(imageUri).apply {
                addOnSuccessListener {
                    reference.downloadUrl.addOnSuccessListener {
                        data.foodImage = it.toString()
                        databasePlaceReference.push().setValue(data)
                    }
                }
                addOnCompleteListener {
                    if (it.isSuccessful) {
                        if (!this@callbackFlow.channel.isClosedForSend) {
                            offer(FirebaseResult.SuccessPush)
                        }
                    }
                }
                addOnFailureListener {
                    if (!this@callbackFlow.channel.isClosedForSend) {
                        offer(FirebaseResult.ErrorPush(it))
                    }
                }
                addOnCanceledListener { close() }
            }
            awaitClose { cancel() }
        }
    }
}