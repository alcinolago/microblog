package br.com.microblog.boticario.firebase.data

import android.provider.SyncStateContract
import br.com.microblog.boticario.Constants
import br.com.microblog.boticario.firebase.provider.FirebaseDataProvider
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query

class FirebaseDataImpl(private val firebaseDataProvider: FirebaseDataProvider) : FirebaseData {

    override fun getOpenedPautas(userId: String): Query {
        return firebaseDataProvider.getDatabaseReference()
            .collection(Constants.FIRESTORE_PAUTAS)
            .whereEqualTo("userId", userId)
            .whereEqualTo("state", "1")
    }

    override fun getClosedPautas(userId: String): Query {
        return firebaseDataProvider.getDatabaseReference()
            .collection(Constants.FIRESTORE_PAUTAS)
            .whereEqualTo("userId", userId)
            .whereEqualTo("state", "0")
    }

    override fun getProfile(userId: String): DocumentReference {
        return firebaseDataProvider.getDatabaseReference()
            .collection(Constants.FIRESTORE_USERS)
            .document(userId)
    }
}