package com.example.moodlight.screen.findpassword

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.moodlight.R
import com.example.moodlight.databinding.ActivityFindPasswordBinding

class FindPasswordActivity : AppCompatActivity() {

    lateinit var binding : ActivityFindPasswordBinding
    val viewModel : FindPasswordViewModel by lazy{
        ViewModelProvider(this).get(FindPasswordViewModel::class.java)
    }

    val findPasswordFragment1 : FindPasswordFragment1 by lazy {
        FindPasswordFragment1()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_find_password)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        supportFragmentManager.beginTransaction()
            .replace(com.example.moodlight.R.id.findpasswordFrame, findPasswordFragment1).commit()

        binding.findpasswordBackBtn.setOnClickListener {
            finish()
        }
    }


}