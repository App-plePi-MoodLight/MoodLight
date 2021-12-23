package com.example.moodlight.screen.main3.setting

import android.content.ContentValues.TAG
import com.example.moodlight.R

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.moodlight.api.ServerClient
import com.example.moodlight.database.UserDatabase
import com.example.moodlight.databinding.ActivityNewPassWordBinding
import com.example.moodlight.model.setting.ChangePasswordModel
import com.example.moodlight.model.setting.SuccussChangePasswordModel
import com.example.moodlight.screen.main3.viewmodel.NewPassWordViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewPassWordActivity : AppCompatActivity() {
    private lateinit var binding : ActivityNewPassWordBinding
    private val vm : NewPassWordViewModel by lazy {
        ViewModelProvider(this)[NewPassWordViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_pass_word)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_pass_word)
        binding.vm = vm
        binding.lifecycleOwner = this


        binding.checkBtn.setOnClickListener {
            Log.d(TAG, "onCreate: password : ${vm.newPassWord.value} ${vm.newPassWordAgain.value}")
            if(!vm.newPassWord.value.equals(vm.newPassWordAgain.value)){
                vm.dangerText.value = "비밀번호가 서로 일치하지않습니다."
                binding.danger3.visibility = View.VISIBLE
            }
            else if(vm.newPassWord.value!!.length in 8..16){
                vm.dangerText.value = "비밀번호는 8~16자 이내입니다."
                binding.danger3.visibility = View.VISIBLE
            }
            else{
                changePw()
            }
        }
    }

    private fun changePw() {
        CoroutineScope(Dispatchers.IO).launch {
            val userPassWord = UserDatabase.getInstance(this@NewPassWordActivity)!!.userDao().getPassword()
            ServerClient.getApiService().changePassword(ChangePasswordModel(vm.newPassWord.value.toString(), userPassWord))
                .enqueue(object : Callback<SuccussChangePasswordModel> {
                    override fun onResponse(
                        call: Call<SuccussChangePasswordModel>,
                        response: Response<SuccussChangePasswordModel>
                    ) {
                        if(response.code() == 200){
                            binding.danger3.visibility = View.GONE
                            Toast.makeText(this@NewPassWordActivity, "비밀번호 변경에 성공하였습니다.", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        else{
                            binding.danger3.visibility = View.VISIBLE
                            vm.dangerText.value = "오류가 발생하였습니다. 다시 시도해주세요."
                        }
                    }

                    override fun onFailure(call: Call<SuccussChangePasswordModel>, t: Throwable) {
                        binding.danger3.visibility = View.VISIBLE
                        vm.dangerText.value = "오류가 발생하였습니다. 다시 시도해주세요."
                    }

                })
        }
    }
}