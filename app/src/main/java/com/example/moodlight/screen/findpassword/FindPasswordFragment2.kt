package com.example.moodlight.screen.findpassword

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
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
import com.example.moodlight.databinding.FragmentFindPassword2Binding
import com.example.moodlight.model.ConfirmFindPasswordModel
import com.example.moodlight.model.SuccessResponseModel
import com.example.moodlight.util.AppUtil
import com.example.moodlight.util.Expression
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FindPasswordFragment2 : Fragment() {

    val viewModel : FindPasswordViewModel by lazy{
        ViewModelProvider(requireActivity()).get(FindPasswordViewModel::class.java)
    }

    lateinit var binding : FragmentFindPassword2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_find_password2, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.password.observe(requireActivity(), Observer {
            if (!it.equals("")) {
                if (Expression.isValidPw(it)) {
                    AppUtil.setSuccessAlarm(binding.findpasswordFragment2ErrorIv1, binding.findpasswordFragment2ErrorTv1
                        ,"사용 가능한 비밀번호입니다.")
                }
                else {
                    AppUtil.setFailureAlarm(binding.findpasswordFragment2ErrorIv1, binding.findpasswordFragment2ErrorTv1
                    ,"6~24자, 영문+숫자 형식에 맞게 입력해주세요.")
                }
            }
        })

        viewModel.rePassword.observe(requireActivity(), Observer {
            if (!it.equals("")) {
                if (viewModel.password.value == it) {
                    AppUtil.setSuccessAlarm(binding.findpasswordFragment2ErrorIv2, binding.findpasswordFragment2ErrorTv2
                        ,"비밀번호가 일치합니다.")
                    binding.findpasswordFragment2ChangeBtn.isEnabled = true
                    binding.findpasswordPasswordEtv.isClickable = false
                    binding.findpasswordPasswordEtv.isFocusable = false
                    binding.findpasswordPasswordReConfirmEtv.isClickable = false
                    binding.findpasswordPasswordReConfirmEtv.isFocusable = false
                }
                else {
                    AppUtil.setFailureAlarm(binding.findpasswordFragment2ErrorIv2, binding.findpasswordFragment2ErrorTv2
                        ,"비밀번호가 일치하지 않습니다.")
                }
            }
        })

        binding.findpasswordFragment2ChangeBtn.setOnClickListener {
            val confirmFindPasswordModel = ConfirmFindPasswordModel(viewModel.confirmNum.value!!,
            viewModel.email.value!!, viewModel.password.value!!)
            ServerClient.getApiService().confirmFindPassword(confirmFindPasswordModel)
                .enqueue(object : Callback<SuccessResponseModel> {

                    override fun onResponse(
                        call: Call<SuccessResponseModel>,
                        response: Response<SuccessResponseModel>
                    ) {
                        if (response.isSuccessful) {
                            CoroutineScope(Dispatchers.IO).launch {
                                val user = UserDatabase.getInstance(requireContext())!!.userDao().getUserFromUserLoginTable()
                                UserDatabase.getInstance(requireContext())!!.userDao().updateLoginTable(UserData(user[0].loginID ,user[0].id, viewModel.rePassword.value.toString(), user[0].likeAlarm, user[0].commentAlarm))
                                val user1 = UserDatabase.getInstance(requireContext())!!.userDao().getPassword()
                                Log.d(TAG, "onResponse: $user1")
                            }
                            Toast.makeText(requireContext(), "비밀번호가 성공적으로 변경되었습니다.", Toast.LENGTH_SHORT).show()
                            requireActivity().finish()
                        }
                        else {
                            Toast.makeText(requireContext(), "error : ${response.code()}", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<SuccessResponseModel>, t: Throwable) {
                        Toast.makeText(requireActivity(), "에러가 발생하였습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                    }

                })
        }

        // Inflate the layout for this fragment
        return binding.root
    }

}