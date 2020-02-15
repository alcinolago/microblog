package br.com.microblog.boticario.firebase.provider

import com.google.firebase.firestore.FirebaseFirestore

class FirebaseDataProviderImpl: FirebaseDataProvider {

    private var db: FirebaseFirestore? = null

    override fun getDatabaseReference(): FirebaseFirestore {

        if (db == null){
            db = FirebaseFirestore.getInstance()
        }

        return db as FirebaseFirestore
    }
}