package com.example.moodlight.screen.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.example.moodlight.R
import com.example.moodlight.util.FirebaseUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = Firebase.auth

        findViewById<AppCompatButton>(R.id.registerBtn).setOnClickListener {

            val email : String = findViewById<EditText>(R.id.registerIdEtv).text.toString()
            val password : String = findViewById<EditText>(R.id.registerPasswordEtv).text.toString()
            val nickname : String = findViewById<EditText>(R.id.registerNicknameEtv).text.toString()
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val map = hashMapOf(
                            "nickname" to nickname
                        )
                        FirebaseFirestore.getInstance().collection("users")
                            .document(FirebaseUtil.getUid())
                            .set(map)
                            .addOnSuccessListener {
                                if (task.isSuccessful) {
                                    finish()
                                }
                            }
                            .addOnFailureListener { e ->
                                Log.w("Register", "Error adding document", e)
                            }
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("Register", "createUserWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}