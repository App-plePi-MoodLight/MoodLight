package com.example.moodlight.screen.main1

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.moodlight.R
import com.example.moodlight.databinding.ActivityPcikMoodBinding
import com.example.moodlight.screen.main1.viewmodel.PickMoodFactory
import com.example.moodlight.screen.main1.viewmodel.PickMoodViewModel

class PickMoodActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPcikMoodBinding
    private val viewModel: PickMoodViewModel by lazy {
        ViewModelProvider(this, PickMoodFactory(this.application))[PickMoodViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pcik_mood)
        binding.viewModel = viewModel
        binding.activity = this
        binding.lifecycleOwner = this
    }

    fun access(view: View){
        startActivity(Intent(this, CommunityActivity::class.java))
    }
}