package com.example.moodlight.screen.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.example.moodlight.R
import com.example.moodlight.database.UserData
import com.example.moodlight.database.UserDatabase
import com.example.moodlight.screen.MainActivity
import com.example.moodlight.screen.initial.InitialActivity
import com.example.moodlight.util.FirebaseUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private val initialActivity: InitialActivity = InitialActivity.initialActivity as InitialActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        findViewById<AppCompatButton>(R.id.loginBtn).setOnClickListener {
            val email: String = findViewById<EditText>(R.id.loginIdEtv).text.toString()
            val password: String = findViewById<EditText>(R.id.loginPasswordEtv).text.toString()

            FirebaseUtil.getAuth().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val userData = UserData(email, password)

                        val db = UserDatabase.getInstance(applicationContext)
                            CoroutineScope(Dispatchers.IO).launch {
                            db!!.userDao().insert(userData)
                        }

                        Log.d("Login", "signInWithEmail:success")
                        val intent: Intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intent)
                        initialActivity.finish()
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("Login", "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

    }
}