package com.example.moodlight.screen.register

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
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
import com.example.moodlight.database.UserData
import com.example.moodlight.database.UserDatabase
import com.example.moodlight.databinding.FragmentRegister4Binding
import com.example.moodlight.model.RegisterConfirmModel
import com.example.moodlight.screen.MainActivity
import com.example.moodlight.screen.initial.InitialActivity
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

        viewModel.confirmCode.observe(requireActivity(), Observer {
            if(it != null && !it.equals("")) {
                setActive()
            } else {
                setFailureInActive()
            }
        })

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
                                val intent : Intent = Intent(requireContext(), MainActivity::class.java)
                                startActivity(intent)
                                initialActivity.finish()
                                requireActivity().finish()
                            } else {
                                Toast.makeText(requireContext(), response.code().toString(), Toast.LENGTH_SHORT).show()
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

    private fun setActive() {
        binding.register4Btn1.isEnabled = true
        if (binding.register4Tv2.visibility == View.VISIBLE) {
            binding.register4Tv2.visibility = View.INVISIBLE
            binding.register4Iv1.visibility = View.INVISIBLE

        }
    }

    private fun setFailureInActive() {
        binding.register4Iv1.setImageResource(R.drawable.img_danger)
        binding.register4Tv2.setTextColor(Color.parseColor("#fd3939"))
        binding.register4Tv2.text = "인증번호를 입력해주세요."

        if (binding.register4Tv2.visibility == View.INVISIBLE) {
            binding.register4Tv2.visibility = View.VISIBLE
            binding.register4Iv1.visibility = View.VISIBLE
        }
        if (binding.register4Btn1.isEnabled)
            binding.register4Btn1.isEnabled = false
    }

    private fun saveLoginData() : Unit {
        val userDataBase : UserDatabase = UserDatabase.getInstance(requireContext())!!
        CoroutineScope(Dispatchers.IO).launch {
            val userData: UserData = UserData(viewModel.email.value!!, viewModel.password.value!!)
            userDataBase.userDao().insert(userData)
        }
    }

}