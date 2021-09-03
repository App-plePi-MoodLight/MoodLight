package com.example.moodlight.screen.register

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
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
import com.example.moodlight.data.IsExistData
import com.example.moodlight.data.JoinBodyData
import com.example.moodlight.data.LoginData
import com.example.moodlight.database.UserData
import com.example.moodlight.databinding.FragmentRegister3Binding
import com.example.moodlight.hash.sha
import com.example.moodlight.screen.MainActivity
import com.example.moodlight.screen.initial.InitialActivity
import com.example.moodlight.util.FirebaseUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterFragment3 : Fragment() {

    private val viewModel: RegisterViewModel by lazy {
        ViewModelProvider(requireActivity()).get(RegisterViewModel::class.java)
    }
    private lateinit var binding: FragmentRegister3Binding
    private val initialActivity: InitialActivity =
        InitialActivity.initialActivity as InitialActivity

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



        Log.d(TAG, "onCreateView: 비밀번호 ${sha.encryptSHA(viewModel.password.value)}")

        CoroutineScope(Dispatchers.IO).launch {
            FirebaseUtil.getFireStoreInstance().collection("users")
                .document("Storage")
                .get()
                .addOnSuccessListener {
                    nicknameArray = it.get("nicknameArray")!! as ArrayList<String>
                }

        }


        viewModel.nickname.observe(requireActivity(), Observer {


            if(!it.equals("")) {
                    ServerClient.getApiService().isExistNickname(it)
                        .enqueue(object : Callback<IsExistData> {

                            override fun onResponse(
                                call: Call<IsExistData>,
                                response: Response<IsExistData>
                            ) {
                                if (response.isSuccessful) {
                                    val isExistNickname = response.body()!!.exist
                                    if (isExistNickname)
                                        setOverlapInActive()
                                    else
                                        setActive()
                                }
                                else
                                    Toast.makeText(requireContext(), response.message()+"\n"+"ERRORCODE: "+response.code().toString(), Toast.LENGTH_SHORT).show()
                            }

                            override fun onFailure(call: Call<IsExistData>, t: Throwable) {
                                t.printStackTrace()
                            }

                        })
                }
                else
                    setFailureInActive()



/*            if (!it.equals("")) {
                for (i in 0 until nicknameArray.size) {
                    if (it.equals(nicknameArray[i])) {
                        setOverlapInActive()
                        break
                    } else {
                        setActive()
                    }
                }
            } else
                setFailureInActive()*/

        })

        binding.register3Btn1.setOnClickListener {
            val email = viewModel.email.value
            val password = viewModel.password.value
            val nickname = viewModel.nickname.value
            registerAndLogin(email!!, password!!, nickname!!)
        }

        binding.register3Etv1.setOnKeyListener(View.OnKeyListener { v, keyCode, event -> keyCode == KeyEvent.KEYCODE_ENTER })

        return binding.root
    }

    private fun setActive() {
        binding.register3Iv1.setImageResource(R.drawable.img_success)
        binding.register3Tv2.setTextColor(Color.parseColor("#009900"))
        binding.register3Tv2.text = "사용가능한 닉네임입니다."
        binding.register3Btn1.isEnabled = true
        if (binding.register3Tv2.visibility == View.INVISIBLE) {
            binding.register3Tv2.visibility = View.VISIBLE
            binding.register3Iv1.visibility = View.VISIBLE

        }
    }

    private fun setFailureInActive() {
        binding.register3Iv1.setImageResource(R.drawable.img_danger)
        binding.register3Tv2.setTextColor(Color.parseColor("#fd3939"))
        binding.register3Tv2.text = "닉네임을 입력해주세요."

        if (binding.register3Tv2.visibility == View.INVISIBLE) {
            binding.register3Tv2.visibility = View.VISIBLE
            binding.register3Iv1.visibility = View.VISIBLE
        }
        if (binding.register3Btn1.isEnabled)
            binding.register3Btn1.isEnabled = false
    }

    private fun setOverlapInActive() {
        binding.register3Iv1.setImageResource(R.drawable.img_danger)
        binding.register3Tv2.setTextColor(Color.parseColor("#fd3939"))
        binding.register3Tv2.text = "이미 사용중인 닉네임입니다."

        if (binding.register3Tv2.visibility == View.INVISIBLE) {
            binding.register3Tv2.visibility = View.VISIBLE
            binding.register3Iv1.visibility = View.VISIBLE
        }
        if (binding.register3Btn1.isEnabled)
            binding.register3Btn1.isEnabled = false
    }

    private fun registerAndLogin(email: String, password: String, nickname: String) {
        val joinBodyData : JoinBodyData = JoinBodyData(nickname, email, password, "user")
        val call : Call<JoinBodyData> = ServerClient.getApiService().joinRequest(joinBodyData)

        call.enqueue(object : Callback<JoinBodyData> {

            override fun onResponse(call: Call<JoinBodyData>, response: Response<JoinBodyData>) {
                if (response.isSuccessful) {
                    Log.w("register", "register success!")
                    val joinBodyData: JoinBodyData = response.body()!!
                    saveLoginData(joinBodyData)

                    val loginData : LoginData = LoginData(joinBodyData.email, joinBodyData.password)
                    ServerClient.getApiService().login(loginData)
                        .enqueue(object : Callback<LoginData>{
                            // login start

                            override fun onResponse(
                                call: Call<LoginData>,
                                response: Response<LoginData>
                            ) {
                                if(response.isSuccessful) {
                                    Log.w("register", "login success!")
                                    val intent : Intent = Intent(requireContext(), MainActivity::class.java)
                                    startActivity(intent)
                                    initialActivity.finish()
                                    requireActivity().finish()
                                }
                                else
                                    Toast.makeText(requireContext(), response.message()+"\n"+"ERRORCODE: "+response.code().toString(), Toast.LENGTH_SHORT).show()
                            }

                            override fun onFailure(call: Call<LoginData>, t: Throwable) {
                                t.printStackTrace()
                            }

                            // login end
                        })


                }
                else
                    Toast.makeText(requireContext(), response.message()+"\n"+"ERRORCODE: "+response.code().toString(), Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<JoinBodyData>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun saveLoginData(joinBodyData: JoinBodyData) : Unit {
        CoroutineScope(Dispatchers.IO).launch {
            val userData: UserData = UserData(joinBodyData.email, joinBodyData.password)
            viewModel.insertLoginData(userData)
        }
    }
}