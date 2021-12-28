package com.example.moodlight

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import com.example.moodlight.database.UserDatabase
import com.example.moodlight.database.UserNotificationData
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        Log.d(TAG, "onNewToken: firebasetoken1 $p0")
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        Log.d(TAG, "onMessageReceived: messgaerk dhlktdam dkan message")
    }
}