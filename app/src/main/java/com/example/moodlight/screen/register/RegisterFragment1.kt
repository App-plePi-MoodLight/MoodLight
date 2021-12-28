package com.example.moodlight.screen.register

import android.os.Bundle
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
import com.example.moodlight.databinding.FragmentRegister1Binding
import com.example.moodlight.model.IsExistModel
import com.example.moodlight.util.AppUtil
import com.example.moodlight.util.Expression
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterFragment1 : Fragment() {

    private val viewModel : RegisterViewModel by lazy {
        ViewModelProvider(requireActivity()).get(RegisterViewModel::class.java)
    }
    private lateinit var binding : FragmentRegister1Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_register1,
            container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = requireActivity()

        viewModel.email.observe( requireActivity(), Observer {
            // it is email.
            if(!it.equals("")) {
                if (Expression.isValidEmail(it)) {
                    ServerClient.getApiService().isExistEmail(it)
                        .enqueue(object : Callback<IsExistModel> {

                            override fun onResponse(
                                call: Call<IsExistModel>,
                                response: Response<IsExistModel>
                            ) {
                                if (response.isSuccessful) {
                                    val isExistEmail: Boolean = response.body()!!.exist

                                    if (isExistEmail) {
                                        AppUtil.setFailureAlarm(binding.register1Iv1,
                                            binding.register1Tv2,
                                            "이미 사용중인 이메일입니다.")
                                        if (binding.register1Btn1.isEnabled) {
                                            binding.register1Btn1.isEnabled = false
                                        }
                                    } else {
                                        AppUtil.setSuccessAlarm(binding.register1Iv1,
                                            binding.register1Tv2,
                                            "사용가능한 이메일입니다.")
                                        if (!binding.register1Btn1.isEnabled) {
                                            binding.register1Btn1.isEnabled = true
                                        }
                                    }
                                } else {
                                    Toast.makeText(requireContext(),
                                        response.message() + "\n" + "ERRORCODE: " + response.code()
                                            .toString(),
                                        Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onFailure(call: Call<IsExistModel>, t: Throwable) {
                                t.printStackTrace()
                            }

                        })
                }
            }
            else {
                AppUtil.setFailureAlarm(binding.register1Iv1,
                    binding.register1Tv2, "이메일 형식에 맞게 입력해주세요.")
                if (binding.register1Btn1.isEnabled) {
                    binding.register1Btn1.isEnabled = false
                }
            }

        })

        binding.register1Tv1.setOnClickListener {
            Toast.makeText(requireContext(), "You are admin.", Toast.LENGTH_SHORT).show()
            viewModel.adminKey = "moodlightadmin"
        }


        binding.register1Btn1.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().replace(
                R.id.registerFrame,
                RegisterFragment2()
            ).commit()
        }

        binding.register1layout1.setOnKeyListener(View.OnKeyListener { v, keyCode, event -> keyCode == KeyEvent.KEYCODE_ENTER })

        return binding.root
    }

}