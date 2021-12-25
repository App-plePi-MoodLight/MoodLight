package com.example.moodlight.screen.register

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.moodlight.R
import com.example.moodlight.api.ServerClient
import com.example.moodlight.database.UserData
import com.example.moodlight.databinding.FragmentRegister4Binding
import com.example.moodlight.model.LoginModel
import com.example.moodlight.model.RegisterConfirmModel
import com.example.moodlight.screen.MainActivity
import com.example.moodlight.screen.initial.InitialActivity
import com.example.moodlight.util.AppUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterFragment4 : Fragment() {

    private val viewModel: RegisterViewModel by lazy {
        ViewModelProvider(requireActivity()).get(RegisterViewModel::class.java)
    }
    private val initialActivity: InitialActivity =
        InitialActivity.initialActivity as InitialActivity

    private lateinit var binding : FragmentRegister4Binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            layoutInflater, R.layout.fragment_register4,
            container, false
        )
        binding.viewModel = viewModel

        AppUtil.setSuccessAlarm(binding.register4Iv1, binding.register4Tv2,
        "입력하신 이메일로 인증번호를 발송하였습니다.")

        binding.register4Btn1.setOnClickListener {
            if (binding.register4Etv1.text != null && binding.register4Etv1.text.toString() != "") {
                val email = viewModel.email.value!!
                val confirmCode = binding.register4Etv1.text.toString()
                ServerClient.getApiService().confirmRequest(RegisterConfirmModel(email, confirmCode))
                    .enqueue(object : Callback<RegisterConfirmModel> {

                        override fun onResponse(
                            call: Call<RegisterConfirmModel>,
                            response: Response<RegisterConfirmModel>
                        ) {
                            if (response.isSuccessful) {
                                saveLoginData()
                                login()
                            } else {
                                AppUtil.setFailureAlarm(binding.register4Iv1, binding.register4Tv2,
                                    "인증번호가 일치하지 않습니다.")
                            }
                        }

                        override fun onFailure(call: Call<RegisterConfirmModel>, t: Throwable) {
                            TODO("Not yet implemented")
                        }

                    })

            }
        }


        return binding.root
    }

    private fun saveLoginData() : Unit {
        CoroutineScope(Dispatchers.IO).launch {
            val userData: UserData = UserData(viewModel.email.value!!, viewModel.password.value!!)
            viewModel.insertLoginData(userData)
        }
    }

    private fun login() : Unit {
        val loginModel : LoginModel = LoginModel(viewModel.email.value!!, viewModel.password.value!!)
        ServerClient.getApiService().login(loginModel)
            .enqueue(object : Callback<LoginModel> {

                override fun onResponse(
                    call: Call<LoginModel>,
                    response: Response<LoginModel>
                ) {
                    if (response.isSuccessful) {
                        ServerClient.accessToken = response.body()!!.accessToken
                        // Sign in success, update UI with the signed-in user's information

                        Log.d("Login", "signInWithEmail:success")
                        val intent : Intent = Intent(requireContext(), MainActivity::class.java)
                        startActivity(intent)
                        initialActivity.finish()
                        requireActivity().finish()

                    } else {
                        Log.d(ContentValues.TAG, "onResponse: respone : ${response}")
                    }
                }

                override fun onFailure(call: Call<LoginModel>, t: Throwable) {
                    t.printStackTrace()
                }
            })
    }

}