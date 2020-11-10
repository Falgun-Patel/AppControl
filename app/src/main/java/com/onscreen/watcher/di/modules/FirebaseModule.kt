package com.onscreen.watcher.di.modules

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import dagger.Module
import dagger.Provides

@Module
class FirebaseModule {

    @Provides
    fun firebaseFirestore(): FirebaseFirestore {
        var mFireStore: FirebaseFirestore = FirebaseFirestore.getInstance()
        mFireStore.firestoreSettings =
            FirebaseFirestoreSettings.Builder().build()

        return mFireStore
    }
}