package com.example.moodlight.screen.login

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.isVisible
import com.example.moodlight.MyFirebaseMessagingService
import com.example.moodlight.R
import com.example.moodlight.api.ServerClient
import com.example.moodlight.database.UserData
import com.example.moodlight.dialog.LoadingDialog
import com.example.moodlight.model.LoginBodyModel
import com.example.moodlight.model.LoginModel
import com.example.moodlight.screen.MainActivity
import com.example.moodlight.screen.findpassword.FindPasswordActivity
import com.example.moodlight.screen.initial.InitialActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private val initialActivity: InitialActivity = InitialActivity.initialActivity as InitialActivity

    private val loading : LoadingDialog by lazy {
        LoadingDialog(LoginActivity@this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        findViewById<AppCompatButton>(R.id.loginBtn).setOnClickListener {
            login()
        }

        findViewById<TextView>(R.id.loginFindpasswordTv).setOnClickListener {
            val intent : Intent = Intent(applicationContext, FindPasswordActivity::class.java)
            startActivity(intent)
        }

    }

    private fun login() {
        val email: String = findViewById<EditText>(R.id.loginIdEtv).text.toString()
        val password: String = findViewById<EditText>(R.id.loginPasswordEtv).text.toString()

        when {
            email.equals("") -> {
                errorVisible("이메일을 입력해주세요.")
            }
            password.equals("") -> {
                errorVisible("비밀번호를 입력해주세요.")
            }
            else -> {
                val firebaseToken = MyFirebaseMessagingService().getToken(this)
                val loginModel = LoginBodyModel(email, password, firebaseToken)
                ServerClient.getApiService().login(loginModel)
                    .enqueue(object : Callback<LoginModel> {
                        override fun onResponse(
                            call: Call<LoginModel>,
                            response: Response<LoginModel>
                        ) {
                            if (response.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                ServerClient.accessToken = response.body()!!.accessToken
                                CoroutineScope(Dispatchers.IO).launch {
                                    val userData = UserData(email, password)
                                    val loginViewModel : LoginViewModel = LoginViewModel(application)
                                    loginViewModel.insertLoginData(userData)

                                }

                                Log.d("Login", "signInWithEmail:success")
                                val intent: Intent =
                                    Intent(applicationContext, MainActivity::class.java)
                                startActivity(intent)
                                initialActivity.finish()
                                finish()
                            } else {
                                Log.d(TAG, "onResponse: respone : ${response}")
                                errorVisible("가입하지 않은 아이디이거나, 잘못된 비밀번호입니다.")
                            }
                        }

                        override fun onFailure(call: Call<LoginModel>, t: Throwable) {
                            t.printStackTrace()
                        }

                    })
            }
        }
    }

    private fun errorVisible(errorText : String) : Unit {
        findViewById<ImageView>(R.id.loginErrorIv).isVisible = true
        findViewById<TextView>(R.id.loginErrorTv).text = errorText
        findViewById<TextView>(R.id.loginErrorTv).isVisible = true
    }
}