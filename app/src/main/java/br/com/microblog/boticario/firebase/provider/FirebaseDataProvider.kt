package br.com.microblog.boticario.firebase.provider

import com.google.firebase.firestore.FirebaseFirestore

interface FirebaseDataProvider {

    fun getDatabaseReference(): FirebaseFirestore
}
