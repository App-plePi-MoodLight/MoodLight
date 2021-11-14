package com.example.moodlight.screen.findpassword

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.moodlight.R
import com.example.moodlight.api.ServerClient
import com.example.moodlight.databinding.ActivityFindPasswordBinding
import com.example.moodlight.model.ConfirmCheckModel
import com.example.moodlight.model.FindPasswordRequestModel
import com.example.moodlight.model.SuccessResponseModel
import com.example.moodlight.util.Expression
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FindPasswordActivity : AppCompatActivity() {

    lateinit var binding : ActivityFindPasswordBinding
    val viewModel : FindPasswordViewModel by lazy{
        ViewModelProvider(this).get(FindPasswordViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_find_password)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.findpasswordConfirmBtn.setOnClickListener {
            if (viewModel.email.value == "") {
                setFailureAlarm(binding.findpasswordErrorIv1, binding.findpasswordErrorTv1
                    , "이메일을 입력해주세요.")
            }
            else {
                if (Expression.isValidEmail(viewModel.email.value)) {
                    sentFindPassword()
                }
                else{
                    setFailureAlarm(binding.findpasswordErrorIv1, binding.findpasswordErrorTv1
                        , "이메일 형식에 맞게 입력해주세요.")
                }
            }
        }

        binding.findpasswordReConfirmBtn.setOnClickListener {
            setSuccessAlarm(binding.findpasswordErrorIv2, binding.findpasswordErrorTv2
                , "인증번호를 재전송하였습니다.")
        }

        binding.findpasswordChangeBtn.setOnClickListener {
            checkAndIntent()
        }

        binding.findpasswordBackBtn.setOnClickListener {
            finish()
        }
    }

    private fun setSuccessAlarm (imageView: ImageView, textView: TextView, alarmText : String) {
        imageView.setImageResource(R.drawable.img_success)
        textView.setTextColor(Color.parseColor("#009900"))
        textView.text = alarmText
        if (imageView.visibility == View.INVISIBLE) {
            imageView.visibility = View.VISIBLE
            textView.visibility = View.VISIBLE
        }
    }

    private fun setFailureAlarm (imageView: ImageView, textView: TextView, alarmText : String) {
        imageView.setImageResource(R.drawable.img_danger)
        textView.setTextColor(Color.parseColor("#fd3939"))
        textView.text = alarmText

        if (imageView.visibility == View.INVISIBLE) {
            imageView.visibility = View.VISIBLE
            textView.visibility = View.VISIBLE
        }
    }

    private fun sentFindPassword() {
        ServerClient.getApiService().sentFindPassword(FindPasswordRequestModel(viewModel.email.value!!))
            .enqueue(object : Callback<SuccessResponseModel> {

                override fun onResponse(
                    call: Call<SuccessResponseModel>,
                    response: Response<SuccessResponseModel>
                ) {
                    if(response.isSuccessful) {
                        setSuccessAlarm(binding.findpasswordErrorIv1, binding.findpasswordErrorTv1
                            , "인증번호를 전송하였습니다.")
                        visibleConfirmView()
                        binding.findpasswordEmailEtv.isClickable = false
                        binding.findpasswordEmailEtv.isFocusable = false
                        binding.findpasswordConfirmBtn.isEnabled = false
                        binding.findpasswordChangeBtn.isEnabled = true
                    }
                    else {
                        Toast.makeText(applicationContext, "error : ${response.code()}", Toast.LENGTH_SHORT).show()
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

                    }
                    else {
                        Toast.makeText(applicationContext, "error : ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<SuccessResponseModel>, t: Throwable) {
                    t.printStackTrace()
                }

            })
    }
}