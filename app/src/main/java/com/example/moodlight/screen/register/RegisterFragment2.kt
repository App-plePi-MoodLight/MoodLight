package com.example.moodlight.screen.register

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.moodlight.R
import com.example.moodlight.databinding.FragmentRegister2Binding
import com.example.moodlight.util.Expression
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
                if (Expression.isValidPw(it))
                    setActive()

                else
                    setFailureInActive()
            }
            else
                setInitial()

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

    private fun setActive () {
        binding.register2Iv1.setImageResource(R.drawable.img_success)
        binding.register2Tv2.setTextColor(Color.parseColor("#009900"))
        binding.register2Tv2.text = "사용가능한 비밀번호입니다."
        binding.register2Btn1.isEnabled = true

    }

    private fun setFailureInActive () {
        binding.register2Iv1.setImageResource(R.drawable.img_danger)
        binding.register2Tv2.setTextColor(Color.parseColor("#fd3939"))
        binding.register2Tv2.text = "6~24자, 영문+숫자 형식에 맞게 입력해주세요."

        if (binding.register2Btn1.isEnabled)
            binding.register2Btn1.isEnabled = false

    }

    private fun setInitial () {
        binding.register2Iv1.setImageResource(R.drawable.img_carbon_information)
        binding.register2Tv2.setTextColor(Color.parseColor("#acacac"))
        binding.register2Tv2.text = "영문, 숫자를 포함한 6~24자 비밀번호"
        if (binding.register2Btn1.isEnabled)
            binding.register2Btn1.isEnabled = false
    }



}