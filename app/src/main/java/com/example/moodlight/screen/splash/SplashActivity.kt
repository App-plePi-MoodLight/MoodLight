package com.example.moodlight.screen.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import com.example.moodlight.R
import com.example.moodlight.database.UserData
import com.example.moodlight.database.UserDatabase
import com.example.moodlight.screen.MainActivity
import com.example.moodlight.screen.initial.InitialActivity
import com.example.moodlight.screen.login.LoginActivity
import com.example.moodlight.screen.onboarding.OnboardingActivity
import com.example.moodlight.util.FirebaseUtil
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*

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
            if(FirebaseUtil.getAuth().currentUser != null){
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            }

            else {
                intent = Intent(applicationContext, OnboardingActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, 2500)

    }
}