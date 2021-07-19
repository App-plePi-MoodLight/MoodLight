package com.example.moodlight.screen.main3

import com.example.moodlight.util.FirebaseUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Main3Helper {
    companion object {
        fun setCommentAlarm(comment: Boolean): Unit {
            CoroutineScope(Dispatchers.IO).launch {
                FirebaseUtil.getFireStoreInstance().collection("users")
                    .document(FirebaseUtil.getUid())
                    .update(
                        hashMapOf(
                            "commentAlarm" to comment
                        ) as Map<String, Any>
                    )
            }
        }

        fun setLikeAlarm(like: Boolean): Unit {
            CoroutineScope(Dispatchers.IO).launch {
                FirebaseUtil.getFireStoreInstance().collection("users")
                    .document(FirebaseUtil.getUid())
                    .update(
                        hashMapOf(
                            "likeAlarm" to like
                        ) as Map<String, Any>
                    )
            }
        }
    }
}