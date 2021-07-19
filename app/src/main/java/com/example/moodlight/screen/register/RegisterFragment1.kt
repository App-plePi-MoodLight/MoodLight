package com.example.moodlight.screen.register

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.moodlight.R
import com.example.moodlight.databinding.FragmentRegister1Binding
import com.example.moodlight.util.Expression
import com.example.moodlight.util.FirebaseUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
        lateinit var emailArray : ArrayList<String>

        CoroutineScope(Dispatchers.IO).launch {
            FirebaseUtil.getFireStoreInstance().collection("users")
                .document("Storage")
                .get()
                .addOnSuccessListener {
                    emailArray = it.get("emailArray")!! as ArrayList<String>
                    Log.e("test",emailArray[0])
                }
        }


        viewModel.email.observe( requireActivity(), Observer {

            if (!it.equals("")) {

                if (Expression.isValidEmail(it)) {
                    for (i in 0 until emailArray.size) {
                        if (it.equals(emailArray[i])) {
                            setOverlapInActive()
                            break
                        } else {
                            setActive()
                        }
                    }
                }
                else
                    setFailureInActive()
            }
        })


        binding.register1Btn1.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().replace(
                R.id.registerFrame,
                RegisterFragment2()
            ).commit()
        }

        binding.register1Etv1.setOnKeyListener(View.OnKeyListener { v, keyCode, event -> keyCode == KeyEvent.KEYCODE_ENTER })

        return binding.root
    }

    private fun setActive () {
        binding.register1Iv1.setImageResource(R.drawable.img_success)
        binding.register1Tv2.setTextColor(Color.parseColor("#009900"))
        binding.register1Tv2.text = "사용가능한 이메일입니다."
        binding.register1Btn1.isEnabled = true
        if (binding.register1Tv2.visibility == View.INVISIBLE) {
            binding.register1Tv2.visibility = View.VISIBLE
            binding.register1Iv1.visibility = View.VISIBLE
        }
    }

    private fun setFailureInActive () {
        binding.register1Iv1.setImageResource(R.drawable.img_danger)
        binding.register1Tv2.setTextColor(Color.parseColor("#fd3939"))
        binding.register1Tv2.text = "이메일 형식에 맞게 입력해주세요."

        if (binding.register1Tv2.visibility == View.INVISIBLE) {
            binding.register1Tv2.visibility = View.VISIBLE
            binding.register1Iv1.visibility = View.VISIBLE
        }
        if (binding.register1Btn1.isEnabled)
            binding.register1Btn1.isEnabled = false
    }

    private fun setOverlapInActive () {
        binding.register1Iv1.setImageResource(R.drawable.img_danger)
        binding.register1Tv2.setTextColor(Color.parseColor("#fd3939"))
        binding.register1Tv2.text = "이미 사용중인 이메일입니다."

        if (binding.register1Tv2.visibility == View.INVISIBLE) {
            binding.register1Tv2.visibility = View.VISIBLE
            binding.register1Iv1.visibility = View.VISIBLE
        }
        if (binding.register1Btn1.isEnabled)
            binding.register1Btn1.isEnabled = false
    }
}