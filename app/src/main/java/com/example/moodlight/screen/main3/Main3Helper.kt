package com.example.moodlight.screen.main3

import android.content.Context
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.moodlight.api.ServerClient
import com.example.moodlight.databinding.FragmentMain3Binding
import com.example.moodlight.model.setting.SuccussChangePasswordModel
import com.example.moodlight.model.setting.UserUpdateModel
import retrofit2.Call
import retrofit2.Response

class Main3Helper {
    companion object {
        fun setAlarm(viewModel : Main3ViewModel, updateModel: UserUpdateModel, context: Context, likeOrComment : Int): Unit {
            ServerClient.getApiService().updateUser(updateModel)
                .enqueue(object : retrofit2.Callback<SuccussChangePasswordModel> {
                    override fun onResponse(
                        call: Call<SuccussChangePasswordModel>,
                        response: Response<SuccussChangePasswordModel>
                    ) {
                        if(!response.isSuccessful){
                            when(likeOrComment){
                                0-> viewModel.commentIsChecked.value = !updateModel.usePushMessageOnComment
                                1-> viewModel.likeIsChecked.value = !updateModel.usePushMessageOnLike
                            }
                            Toast.makeText(context, "오류가 발생하였습니다. 다시 시도해주세요", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<SuccussChangePasswordModel>, t: Throwable) {
                        when(likeOrComment){
                            0-> viewModel.commentIsChecked.value = !updateModel.usePushMessageOnComment
                            1-> viewModel.likeIsChecked.value = !updateModel.usePushMessageOnLike
                        }
                        Toast.makeText(context, "오류가 발생하였습니다. 다시 시도해주세요", Toast.LENGTH_SHORT).show()
                    }

                })
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