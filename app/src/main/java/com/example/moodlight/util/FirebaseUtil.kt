package com.example.moodlight.util

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging

class FirebaseUtil {
    companion object {
        fun getUid () : String {
            return FirebaseAuth.getInstance().uid!!
        }

        fun getFireStoreInstance () : FirebaseFirestore {
            return FirebaseFirestore.getInstance();
        }

        fun getFirebaseMessagingInstance() : FirebaseMessaging {
            return FirebaseMessaging.getInstance()
        }

        fun getAuth () : FirebaseAuth {
            return FirebaseAuth.getInstance();
        }

    }


}