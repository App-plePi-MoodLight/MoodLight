package com.example.moodlight.screen.findpassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.moodlight.R
import com.example.moodlight.api.ServerClient
import com.example.moodlight.databinding.FragmentFindPassword1Binding
import com.example.moodlight.dialog.LoadingDialog
import com.example.moodlight.model.ConfirmCheckModel
import com.example.moodlight.model.FindPasswordRequestModel
import com.example.moodlight.model.SuccessResponseModel
import com.example.moodlight.util.AppUtil
import com.example.moodlight.util.Expression
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FindPasswordFragment1 : Fragment() {

    val viewModel : FindPasswordViewModel by lazy{
        ViewModelProvider(requireActivity()).get(FindPasswordViewModel::class.java)
    }

    val findPasswordFragment2 : FindPasswordFragment2 by lazy {
        FindPasswordFragment2()
    }

    lateinit var binding : FragmentFindPassword1Binding

    private val loading : LoadingDialog by lazy {
        LoadingDialog(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_find_password1, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.findpasswordConfirmBtn.setOnClickListener {
            if (viewModel.email.value == "") {
                AppUtil.setFailureAlarm(binding.findpasswordErrorIv1, binding.findpasswordErrorTv1
                    , "이메일을 입력해주세요.")
            }
            else {
                if (Expression.isValidEmail(viewModel.email.value)) {
                    sentFindPassword()
                }
                else{
                    AppUtil.setFailureAlarm(binding.findpasswordErrorIv1, binding.findpasswordErrorTv1
                        , "이메일 형식에 맞게 입력해주세요.")
                }
            }
        }

        binding.findpasswordReConfirmBtn.setOnClickListener {
            AppUtil.setSuccessAlarm(binding.findpasswordErrorIv2, binding.findpasswordErrorTv2
                , "인증번호를 재전송하였습니다.")
            viewModel.confirmNum.value = ""
            sentFindPassword()
        }

        binding.findpasswordChangeBtn.setOnClickListener {
            checkAndIntent()
        }


        return binding.root
    }

    private fun sentFindPassword() {
        loading.show()
        ServerClient.getApiService().sentFindPassword(FindPasswordRequestModel(viewModel.email.value!!))
            .enqueue(object : Callback<SuccessResponseModel> {

                override fun onResponse(
                    call: Call<SuccessResponseModel>,
                    response: Response<SuccessResponseModel>
                ) {
                    if(response.isSuccessful) {
                        loading.dismiss()
                        AppUtil.setSuccessAlarm(binding.findpasswordErrorIv1, binding.findpasswordErrorTv1
                            , "인증번호를 전송하였습니다.")
                        visibleConfirmView()
                        binding.findpasswordEmailEtv.isClickable = false
                        binding.findpasswordEmailEtv.isFocusable = false
                        binding.findpasswordConfirmBtn.isEnabled = false
                        binding.findpasswordChangeBtn.isEnabled = true
                    }
                    else {
                        Toast.makeText(requireContext(), "error : ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<SuccessResponseModel>, t: Throwable) {
                    t.printStackTrace()
                }

            })
    }

    private fun visibleConfirmView () {
        binding.findpasswordTv2.visibility = View.VISIBLE
        binding.findpasswordConfirmEtv.visibility = View.VISIBLE
        binding.findpasswordReConfirmBtn.visibility = View.VISIBLE
    }

    private fun checkAndIntent() {
        val confirmCheckModel: ConfirmCheckModel =
            ConfirmCheckModel(viewModel.confirmNum.value!!, viewModel.email.value!!)
        ServerClient.getApiService().checkConfirm(confirmCheckModel)
            .enqueue(object : Callback<SuccessResponseModel> {

                override fun onResponse(
                    call: Call<SuccessResponseModel>,
                    response: Response<SuccessResponseModel>
                ) {
                    if (response.isSuccessful) {
                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(com.example.moodlight.R.id.findpasswordFrame, findPasswordFragment2).commit()
                    }
                    else {
                        Toast.makeText(requireContext(), "error : ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<SuccessResponseModel>, t: Throwable) {
                    t.printStackTrace()
                }

            })
    }

}