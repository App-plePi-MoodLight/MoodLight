package com.example.moodlight.screen.main3.setting

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.moodlight.R
import com.example.moodlight.api.ServerClient
import com.example.moodlight.database.UserDatabase
import com.example.moodlight.databinding.ActivityFindPassWordBinding
import com.example.moodlight.model.findpassword.CheckCodeBodyModel
import com.example.moodlight.model.findpassword.CheckCodeModel
import com.example.moodlight.model.findpassword.FindPassWordBodyModel
import com.example.moodlight.model.findpassword.FindPassWordModel
import com.example.moodlight.screen.main3.viewmodel.FindPassWordViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FindPassWordActivity : AppCompatActivity() {
    private lateinit var binding : ActivityFindPassWordBinding
    private val vm : FindPassWordViewModel by lazy {
        ViewModelProvider(this)[FindPassWordViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_pass_word)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_find_pass_word)
        binding.vm  = vm
        binding.lifecycleOwner = this
        setUi()

        vm.email.observe(this, Observer {
            if(vm.email.value!!.matches(Regex("\"/^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}\\\$/i;\\n\""))){
                binding.register1Tv2.visibility = View.GONE
                binding.register1Iv1.visibility = View.GONE
            }
            else if(vm.email.value!!.matches(Regex("\"/^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}\\\$/i;\\n\""))){
                emailDanger("이메일 형식이 일치하지않습니다.")
            }
        })

        binding.appCompatButton.setOnClickListener { 
            getCertCode()
        }
        binding.againCerfButton.setOnClickListener {
            getCertCode()
        }
        binding.nextButton.setOnClickListener {
            checkCertCode()
        }
    }

    private fun checkCertCode() {
        CoroutineScope(Dispatchers.IO).launch {
            val userPassword = UserDatabase.getInstance(this@FindPassWordActivity)!!.userDao().getPassword()
            ServerClient.getApiService().confirmFindPassword(CheckCodeBodyModel(vm.certText.value.toString(), vm.email.value.toString(), userPassword))
                .enqueue(object : Callback<CheckCodeModel>{
                    override fun onResponse(
                        call: Call<CheckCodeModel>,
                        response: Response<CheckCodeModel>
                    ) {
                        if(response.code() == 201 || response.code() == 200){
                            val result = response.body()
                            startActivity(Intent(this@FindPassWordActivity, NewPassWordActivity::class.java))
                            finish()
                        }
                        else{
                            binding.cerfCheckImageView.visibility = View.VISIBLE
                            binding.cerfCheckTextView.visibility = View.VISIBLE
                            vm.certCheckText.value = "인증번호가 일치하지 않습니다."
                        }
                    }

                    override fun onFailure(call: Call<CheckCodeModel>, t: Throwable) {
                        emailDanger("요청을 보내는 중 오류가 발생하였습니다. 다시 시도해주세요.")
                    }

                })
        }
    }

    private fun setUi() {
        binding.cerfLayout.visibility = View.GONE
        binding.cerfTv.visibility = View.GONE
        binding.cerfCheckImageView.visibility = View.GONE
        binding.cerfCheckTextView.visibility = View.GONE
        binding.register1Tv2.visibility = View.GONE
        binding.register1Iv1.visibility = View.GONE
    }

    private fun getCertCode() {
        Log.d(TAG, "getCertCode: ${vm.email.value.toString()}")
        CoroutineScope(Dispatchers.IO).launch { 
            ServerClient.getApiService().findPassword(FindPassWordBodyModel(vm.email.value.toString()))
                .enqueue(object : Callback<FindPassWordModel> {
                    override fun onResponse(
                        call: Call<FindPassWordModel>,
                        response: Response<FindPassWordModel>
                    ) {
                        if(response.code() == 200 || response.code() == 201){
                            val result = response.body()
                            Log.d(TAG, "onResponse: 비밀번호 확인 ${result}")
                            runOnUiThread {
                                binding.cerfLayout.visibility = View.VISIBLE
                                binding.cerfTv.visibility = View.VISIBLE
                                binding.register1Tv2.visibility = View.GONE
                                binding.register1Iv1.visibility = View.GONE
                            }
                        }
                        else{
                            runOnUiThread {
                                emailDanger("이메일을 올바르게 작성했는지 확인하세요.")
                            }
                        }
                    }

                    override fun onFailure(call: Call<FindPassWordModel>, t: Throwable) {
                        Toast.makeText(this@FindPassWordActivity, "요청 중 오류가 발생하였습니다.\n다시 시도해주세요", Toast.LENGTH_SHORT)
                            .show()
                    }

                })
        }
    }

    private fun emailDanger(string: String) {
        binding.register1Tv2.visibility = View.VISIBLE
        binding.register1Iv1.visibility = View.VISIBLE
        vm.registerCheckText.value = string
    }


}