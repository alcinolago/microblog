package br.com.microblog.boticario.firebase.data

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query

interface FirebaseData {
    fun getPosts(): Query
    fun getProfile(userId: String): DocumentReference
}