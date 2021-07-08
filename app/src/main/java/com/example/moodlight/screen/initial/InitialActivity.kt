package com.example.moodlight.screen.initial

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import com.example.moodlight.R
import com.example.moodlight.screen.login.LoginActivity
import com.example.moodlight.screen.register.RegisterActivity

class InitialActivity : AppCompatActivity() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        public lateinit var initialActivity : Activity 
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial)
        initialActivity = this@InitialActivity
        var intent : Intent
        findViewById<AppCompatButton>(R.id.callLoginBtn).setOnClickListener {
            intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
        }

        findViewById<AppCompatButton>(R.id.callRegisterBtn).setOnClickListener {
            intent = Intent(applicationContext, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}