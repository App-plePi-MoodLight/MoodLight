package com.example.moodlight.screen.main3

import androidx.core.view.isVisible
import com.example.moodlight.databinding.FragmentMain3Binding
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

        fun setAnimation(binding : FragmentMain3Binding) : Unit {
            binding.main3ProfileIv.postDelayed({
                binding.main3ProfileIv.isVisible = true
            }, 50L)

            binding.main3Tv1.postDelayed({
                binding.main3UserNameTv.isVisible = true
            }, 200L)
            binding.main3EmailTv.postDelayed({
                binding.main3EmailTv.isVisible = true
            }, 250L)
            binding.main3Tv1.postDelayed({
                binding.main3Tv1.isVisible = true
            }, 300L)
            binding.main3Btn1.postDelayed({
                binding.main3Btn1.isVisible = true
            }, 350L)
            binding.main3Btn2.postDelayed({
                binding.main3Btn2.isVisible = true
            }, 350L)
            binding.main3Tv2.postDelayed({
                binding.main3Tv2.isVisible = true
            }, 400L)
            binding.main3Tv3.postDelayed({
                binding.main3Tv3.isVisible = true
            }, 450L)
            binding.main3CommentSwitch.postDelayed({
                binding.main3CommentSwitch.isVisible = true
            }, 500L)
            binding.main3Tv4.postDelayed({
                binding.main3Tv4.isVisible = true
            }, 550L)
            binding.main3LikeSwitch.postDelayed({
                binding.main3LikeSwitch.isVisible = true
            }, 600L)
            binding.main3Tv5.postDelayed({
                binding.main3Tv5.isVisible = true
            }, 650L)
            binding.main3Tv6.postDelayed({
                binding.main3Tv6.isVisible = true
            }, 650L)
            binding.helpBtn1.postDelayed({
                binding.helpBtn1.isVisible = true
            }, 650L)
            binding.main3SubscriptionTv.postDelayed({
                binding.main3SubscriptionTv.isVisible = true
            }, 700L)
            binding.main3LogoutBtn.postDelayed({
                binding.main3LogoutBtn.isVisible = true
            }, 750L)
            binding.main3WithdrawalTv.postDelayed({
                binding.main3WithdrawalTv.isVisible = true
            }, 800L)
            binding.layout1.postDelayed({
                binding.layout1.isVisible = true
            }, 850L)
        }

        fun setVisible(binding : FragmentMain3Binding) : Unit {
            binding.main3ProfileIv.isVisible = true
            binding.main3UserNameTv.isVisible = true
            binding.main3EmailTv.isVisible = true
            binding.main3Tv1.isVisible = true
            binding.main3Btn1.isVisible = true
            binding.main3Tv2.isVisible = true
            binding.main3Tv3.isVisible = true
            binding.main3CommentSwitch.isVisible = true
            binding.main3Tv4.isVisible = true
            binding.main3LikeSwitch.isVisible = true
            binding.main3Tv5.isVisible = true
            binding.main3SubscriptionTv.isVisible = true
            binding.main3LogoutBtn.isVisible = true
            binding.main3WithdrawalTv.isVisible = true
            binding.layout1.isVisible = true
            binding.helpBtn1.isVisible = true
            binding.main3Btn2.isVisible = true
            binding.main3Tv6.isVisible = true
            binding.helpBtn1.isVisible = true
        }

    }
}