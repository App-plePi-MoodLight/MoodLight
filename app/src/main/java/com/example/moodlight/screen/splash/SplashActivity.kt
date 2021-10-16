package com.example.moodlight.screen.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.moodlight.R
import com.example.moodlight.api.ServerClient
import com.example.moodlight.database.UserData
import com.example.moodlight.database.UserDatabase
import com.example.moodlight.model.LoginModel
import com.example.moodlight.screen.MainActivity
import com.example.moodlight.screen.onboarding.OnboardingActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashActivity : AppCompatActivity() {

    val fadeinAnim : Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.fade_in) }
    val fadeinAnim2 : Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.fade_in_2000) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        lateinit var intent : Intent

        findViewById<TextView>(R.id.textView3).startAnimation(fadeinAnim)
        findViewById<TextView>(R.id.textView4).startAnimation(fadeinAnim2)



        Handler(Looper.getMainLooper()).postDelayed({
            val db : UserDatabase = Room.databaseBuilder(applicationContext, UserDatabase::class.java,
                "moodlight-db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()

            Log.e("asd", db.userDao().getUserFromUserLoginTable().toString())

            if (db.userDao().getIdFromUserLoginTable() != null) {
                val id: String = db.userDao().getUserFromUserLoginTable()!![0]!!.id
                val password: String = db.userDao().getUserFromUserLoginTable()!![0]!!.password

                val loginModel: LoginModel = LoginModel(id, password)
                ServerClient.getApiService().login(loginModel)
                    .enqueue(object : Callback<LoginModel> {

                        override fun onResponse(
                            call: Call<LoginModel>,
                            response: Response<LoginModel>
                        ) {
                            if (response.isSuccessful) {
                                ServerClient.accessToken = response.body()!!.accessToken

                                // Sign in success, update UI with the signed-in user's information
                                val userData = UserData(id, password)

                                val db = UserDatabase.getInstance(applicationContext)
                                Log.d("Login", "signInWithEmail:success")
                                val intent: Intent =
                                    Intent(applicationContext, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }

                        /**
                         * Invoked when a network exception occurred talking to the server or when an unexpected exception
                         * occurred creating the request or processing the response.
                         */
                        override fun onFailure(call: Call<LoginModel>, t: Throwable) {
                            TODO("Not yet implemented")
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