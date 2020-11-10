package com.onscreen.watcher.fcm.database

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.onscreen.watcher.fcm.vo.FirebaseModelResponse
import com.onscreen.watcher.fcm.vo.ZoomPropertiesModel
import com.onscreen.watcher.utils.AppConstants
import javax.inject.Inject


class FirebaseDatabaseManager @Inject constructor(private val database: FirebaseFirestore) :
    FirebaseDatabaseInterface {

    override fun addZoomProperties(
        zoomProperties: ZoomPropertiesModel,
        token: String,
        onResult: (Boolean) -> Unit
    ) {
        database.collection(AppConstants.PREF_ZOOM_COLLECTION).document(token)
            .set(zoomProperties)
            .addOnSuccessListener {
                Log.d("TAG", "DocumentSnapshot successfully written!")
                onResult(true)
            }.addOnFailureListener { e ->
                Log.e("TAG", "Error writing document", e)
                onResult(false)
            }
    }

    override fun getZoomPropertiesListener(
        token: String,
        onResult: (ZoomPropertiesModel) -> Unit
    ) {
        database.collection(AppConstants.PREF_ZOOM_COLLECTION).document(token)
            .addSnapshotListener { queryDocumentSnapshots, e ->
                if (e != null) {
                    Log.w("TAG", "Listen failed.", e)
                    return@addSnapshotListener
                }
                if (queryDocumentSnapshots?.exists()!!) {
                    // convert document to POJO
                    onResult(queryDocumentSnapshots.toObject(ZoomPropertiesModel::class.java)!!)
                }

            }
    }
}