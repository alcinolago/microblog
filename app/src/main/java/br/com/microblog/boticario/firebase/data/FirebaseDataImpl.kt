package br.com.microblog.boticario.firebase.data

import br.com.microblog.boticario.Constants
import br.com.microblog.boticario.firebase.provider.FirebaseDataProvider
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query

class FirebaseDataImpl(private val firebaseDataProvider: FirebaseDataProvider) : FirebaseData {

    override fun getPosts(userId: String): Query {
        return firebaseDataProvider.getDatabaseReference()
            .collection(Constants.FIRESTORE_POST)
            .whereEqualTo("userId", userId)
    }

    override fun getProfile(userId: String): DocumentReference {
        return firebaseDataProvider.getDatabaseReference()
            .collection(Constants.FIRESTORE_USERS)
            .document(userId)
    }
}