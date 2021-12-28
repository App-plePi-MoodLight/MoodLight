package com.example.moodlight.screen.register

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.moodlight.R
import com.example.moodlight.api.ServerClient
import com.example.moodlight.databinding.FragmentRegister3Binding
import com.example.moodlight.dialog.LoadingDialog
import com.example.moodlight.model.IsExistModel
import com.example.moodlight.model.JoinBodyModel
import com.example.moodlight.util.AppUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterFragment3 : Fragment() {

    private val viewModel: RegisterViewModel by lazy {
        ViewModelProvider(requireActivity()).get(RegisterViewModel::class.java)
    }
    private lateinit var binding: FragmentRegister3Binding

    private val loading : LoadingDialog by lazy {
        LoadingDialog(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            layoutInflater, R.layout.fragment_register3,
            container, false
        )
        binding.viewModel = viewModel
        lateinit var nicknameArray: ArrayList<String>

        viewModel.nickname.observe(requireActivity(), Observer {


            if (!it.equals("")) {

                ServerClient.getApiService().isExistNickname(it)
                    .enqueue(object : Callback<IsExistModel> {

                        override fun onResponse(
                            call: Call<IsExistModel>,
                            response: Response<IsExistModel>
                        ) {
                            if (response.isSuccessful) {
                                val isExistNickname : Boolean = response.body()!!.exist

                                if (isExistNickname) {
                                    AppUtil.setFailureAlarm(binding.register3Iv1,
                                        binding.register3Tv2,
                                        "이미 사용중인 닉네임입니다.")
                                    if (!binding.register3Btn1.isEnabled) {
                                        binding.register3Btn1.isEnabled = true
                                    }
                                }
                                else {
                                    AppUtil.setSuccessAlarm(binding.register3Iv1,
                                        binding.register3Tv2,
                                        "사용가능한 닉네임입니다.")
                                    if (!binding.register3Btn1.isEnabled) {
                                        binding.register3Btn1.isEnabled = true
                                    }
                                }
                            }
                        }

                        override fun onFailure(call: Call<IsExistModel>, t: Throwable) {
                            t.printStackTrace()
                        }

                    })

            } else {
                AppUtil.setFailureAlarm(binding.register3Iv1,
                    binding.register3Tv2, "세글자 이상의 닉네임을 입력해주세요.")
                if (!binding.register3Btn1.isEnabled) {
                    binding.register3Btn1.isEnabled = true
                }
            }

        })

        binding.register3Btn1.setOnClickListener {
            val email = viewModel.email.value
            val password = viewModel.password.value
            val nickname = viewModel.nickname.value
            loading.show()
            registerAndMoveNextPage(email!!, password!!, nickname!!)
        }

        binding.register3Etv1.setOnKeyListener(View.OnKeyListener { v, keyCode, event -> keyCode == KeyEvent.KEYCODE_ENTER })

        return binding.root
    }

    private fun registerAndMoveNextPage(email: String, password: String, nickname: String) {
        val joinBodyModel : JoinBodyModel = JoinBodyModel(nickname, email, password,viewModel.adminKey)
        val call : Call<JoinBodyModel> = ServerClient.getApiService().joinRequest(joinBodyModel)

        call.enqueue(object : Callback<JoinBodyModel> {

            override fun onResponse(call: Call<JoinBodyModel>, response: Response<JoinBodyModel>) {
                loading.dismiss()
                if (response.isSuccessful) {
                    Log.w("register", "register success!")

                    requireActivity().supportFragmentManager.beginTransaction().replace(
                        R.id.registerFrame,
                        RegisterFragment4()
                    ).commit()

                }
                else
                    Toast.makeText(requireContext(), "when : "+response.message()+"\n"+"ERRORCODE: "+response.code().toString(), Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<JoinBodyModel>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

}