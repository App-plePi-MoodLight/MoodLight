package com.example.moodlight.screen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.moodlight.R
import com.example.moodlight.screen.main1.MainFragment1
import com.example.moodlight.screen.main2.MainFragment2
import com.example.moodlight.screen.main3.MainFragment3
import com.google.android.material.bottomnavigation.BottomNavigationView

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private val mainFragment1 by lazy {MainFragment1()}
    private val mainFragment2 by lazy {MainFragment2()}
    private val mainFragment3 by lazy {MainFragment3()}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        changeFragment(mainFragment1)

        findViewById<BottomNavigationView>(R.id.bottomNavigation).setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.main1 -> {
                    changeFragment(mainFragment1)
                    true
                }
                R.id.main2 -> {
                    changeFragment(mainFragment2)
                    true
                }
                R.id.main3 -> {
                    changeFragment(mainFragment3)
                    true
                }
                else -> false
            }
        }

    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager .beginTransaction()
            .replace(R.id.mainFrame, fragment) .commit()
    }

}