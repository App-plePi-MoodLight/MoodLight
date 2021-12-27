package com.example.moodlight.screen.splash

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.moodlight.R
import com.example.moodlight.api.ServerClient
import com.example.moodlight.database.UserDatabase
import com.example.moodlight.dialog.TerminationDialog
import com.example.moodlight.model.LoginBodyModel
import com.example.moodlight.model.LoginModel
import com.example.moodlight.screen.MainActivity
import com.example.moodlight.screen.onboarding.OnboardingActivity
import com.example.moodlight.util.AppUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashActivity : AppCompatActivity() {

    val fadeinAnim: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.fade_in) }
    val fadeinAnim2: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.fade_in_2000) }
    private lateinit var firebaseToekn : String
    var id: String? = null
    var password: String? = null

    private val dialog : TerminationDialog by lazy {
        TerminationDialog(Activity(), SplashActivity@this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        lateinit var intent: Intent

        if (AppUtil.isNotConnectNetwork(applicationContext)) {
            dialog.show()
        }

        val db = UserDatabase.getInstance(applicationContext)

        findViewById<TextView>(R.id.textView3).startAnimation(fadeinAnim)
        findViewById<TextView>(R.id.textView4).startAnimation(fadeinAnim2)

        CoroutineScope(Dispatchers.IO).launch {
                id = db!!.userDao().getId()
                password = db!!.userDao().getPassword()
        }
        CoroutineScope(Dispatchers.IO).launch {
            AppUtil.getToken().addOnCompleteListener {
                runOnUiThread {
                    firebaseToekn = it.result!!
                    Log.d(TAG, "onCreate: ${firebaseToekn}")
                    login(firebaseToekn)
                }
            }
        }


    }

    private fun login(token : String) {
        val handler: Handler = Handler()

        handler.postDelayed(Runnable {
            Log.e(TAG, "onCreate: ${id} ${password}")
            if (id != null && password != null) {
                Log.d(TAG, "onCreate: firebasetoken ${firebaseToekn}")
                val loginModel: LoginBodyModel = LoginBodyModel(id!!, password!!, token)
                ServerClient.getApiService().login(loginModel)
                    .enqueue(object : Callback<LoginModel> {

                        override fun onResponse(
                            call: Call<LoginModel>,
                            response: Response<LoginModel>
                        ) {
                            if (response.isSuccessful) {
                                ServerClient.accessToken=
                                    response.body()!!.accessToken
                                // Sign in success, update UI with the signed-in user's information

                                Log.d("Login", "signInWithEmail:success")
                                val intent: Intent =
                                    Intent(applicationContext, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                Log.e("z",response.code().toString())
                            }
                        }

                        /**
                         * Invoked when a network exception occurred talking to the server or when an unexpected exception
                         * occurred creating the request or processing the response.
                         */
                        override fun onFailure(call: Call<LoginModel>, t: Throwable) {
                            t.printStackTrace()
                        }
                    })
            } else {

                intent = Intent(applicationContext, OnboardingActivity::class.java)
                startActivity(intent)
                finish()
            }

        }, 2500)
    }
}
