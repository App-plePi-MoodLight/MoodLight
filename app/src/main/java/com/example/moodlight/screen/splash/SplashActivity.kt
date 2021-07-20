package com.example.moodlight.screen.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.moodlight.R
import com.example.moodlight.database.UserData
import com.example.moodlight.database.UserDatabase
import com.example.moodlight.screen.MainActivity
import com.example.moodlight.screen.initial.InitialActivity
import com.example.moodlight.screen.login.LoginActivity
import com.example.moodlight.util.FirebaseUtil
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        lateinit var intent : Intent

        var userDataList : List<UserData>?
        val db = UserDatabase.getInstance(applicationContext)


        GlobalScope.launch {

//            if (db!!.userDao().getuserLoginTable()!!.isNotEmpty()) {
//                userDataList = db!!.userDao().getuserLoginTable()
//                val email : String = userDataList!![0].id
//                vail password : String = userDataList!![0].password
//
//                Fiirebase.auth.signInWithEmailAndPassword(email, password)
//                i    .addOnCompleteListener(this@SplashActivity) { task ->
//                        if (task.isSuccessful) {
//
//                            Log.d("Login", "signInWithEmail:success")
//                            intent = Intent(applicationContext, MainActivity::class.java)
//               i             startActivity(intent)
//              i              finish()
//             i           }
//                    }
//            }
            if(FirebaseUtil.getAuth().currentUser != null){
                startActivity(Intent(applicationContext, MainActivity::class.java))
            }
            else {
                intent = Intent(applicationContext, InitialActivity::class.java)
                startActivity(intent)
                finish()
            }

            delay(3000)


        }

    }
}