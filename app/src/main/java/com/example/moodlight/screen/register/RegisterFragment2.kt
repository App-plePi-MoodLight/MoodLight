package com.example.moodlight.screen.register

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.moodlight.R
import com.example.moodlight.databinding.FragmentRegister2Binding
import com.example.moodlight.util.AppUtil
import com.example.moodlight.util.Expression

class RegisterFragment2 : Fragment() {


    private val viewModel : RegisterViewModel by lazy {
        ViewModelProvider(requireActivity()).get(RegisterViewModel::class.java)
    }
    private lateinit var binding : FragmentRegister2Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_register2,
            container, false)
        binding.viewModel = viewModel

        viewModel.password.observe(requireActivity(), Observer {

            if (!it.equals("")) {
                if (Expression.isValidPw(it)) {
                    AppUtil.setSuccessAlarm(binding.register2Iv1, binding.register2Tv2,
                        "사용가능한 비밀번호입니다.")
                    if (!binding.register2Btn1.isEnabled) {
                        binding.register2Btn1.isEnabled = true
                    }
                }
                else {
                    AppUtil.setFailureAlarm(binding.register2Iv1, binding.register2Tv2,
                        "6~24자, 영문+숫자 형식에 맞게 입력해주세요.")
                    if (binding.register2Btn1.isEnabled) {
                        binding.register2Btn1.isEnabled = false
                    }
                }
            }
            else {
                AppUtil.setInitialAlarm(binding.register2Iv1, binding.register2Tv2,
                    binding.register2Btn1, "영문, 숫자를 포함한 6~24자 비밀번호")
            }
        })

        binding.register2Btn1.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().replace(
                R.id.registerFrame,
                RegisterFragment3()
            ).commit()
        }

        binding.register2Etv1.setOnKeyListener(View.OnKeyListener { v, keyCode, event -> keyCode == KeyEvent.KEYCODE_ENTER })



        return binding.root
    }

}