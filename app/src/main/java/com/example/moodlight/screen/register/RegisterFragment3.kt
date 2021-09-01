package com.example.moodlight.screen.register

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.moodlight.hash.sha
import com.example.moodlight.R
import com.example.moodlight.databinding.FragmentRegister3Binding
import com.example.moodlight.screen.initial.InitialActivity
import com.example.moodlight.screen.login.LoginActivity
import com.example.moodlight.util.FirebaseUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

            if (!it.equals("")) {
                for (i in 0 until nicknameArray.size) {
                    if (it.equals(nicknameArray[i])) {
                        setOverlapInActive()
                        break
                    } else {
                        setActive()
                    }
                }
            } else
                setFailureInActive()

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
        FirebaseUtil.getAuth().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val map = hashMapOf(
                        "nickname" to nickname,
                        "email" to viewModel.email.value,
                        "joinTime" to System.currentTimeMillis(),
                        "commentAlarm" to false,
                        "likeAlarm" to false,
                        "password" to sha.encryptSHA(viewModel.password.value)
                    )

                    CoroutineScope(Dispatchers.IO).launch {
                        val map = hashMapOf(
                            "nickname" to nickname,
                            "joinTime" to System.currentTimeMillis(),
                            "commentAlarm" to false,
                            "likeAlarm" to false,
                            "password" to sha.encryptSHA(viewModel.password.value),
                            "token" to FirebaseUtil.getFirebaseMessagingInstance().token
                        )

                        FirebaseUtil.getFireStoreInstance().collection("users")
                            .document(FirebaseUtil.getUid())
                            .set(map)
                    }

                    CoroutineScope(Dispatchers.IO).launch {
                        lateinit var nicknameArray : ArrayList<String>
                        lateinit var emailArray : ArrayList<String>
                        FirebaseUtil.getFireStoreInstance().collection("users")
                            .document("Storage")
                            .get()
                            .addOnSuccessListener {
                                nicknameArray = it.get("nicknameArray") as ArrayList<String>
                                emailArray = it.get("emailArray") as ArrayList<String>
                                nicknameArray.add(viewModel.nickname.value!!)
                                emailArray.add(viewModel.email.value!!)
                                val map2 = hashMapOf(
                                    "nicknameArray" to nicknameArray,
                                    "emailArray" to emailArray
                                )
                                FirebaseUtil.getFireStoreInstance().collection("users")
                                    .document("Storage")
                                    .update(map2 as Map<String, Any>)
                            }
                    }

                    if (task.isSuccessful) {
                        FirebaseUtil.getAuth().signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val intent: Intent =
                                        Intent(requireContext(), LoginActivity::class.java)
                                    startActivity(intent)
                                    initialActivity.finish()
                                    requireActivity().finish()
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(
                                        "Login",
                                        "signInWithEmail:failure",
                                        task.exception
                                    )

                                    Toast.makeText(
                                        requireContext(), "Authentication failed.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    }
                }
            }
    }
}