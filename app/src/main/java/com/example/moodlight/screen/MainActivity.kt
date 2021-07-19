package com.example.moodlight.screen

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.moodlight.R
import com.example.moodlight.screen.main1.MainFragment1
import com.example.moodlight.screen.main2.MainFragment2
import com.example.moodlight.screen.main3.MainFragment3
import com.example.moodlight.util.NetworkStatus
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private val mainFragment1 by lazy {MainFragment1()}
    private val mainFragment2 by lazy {MainFragment2()}
    private val mainFragment3 by lazy {MainFragment3()}
    private val networkStatus : Int by lazy {NetworkStatus.getConnectivityStatus(applicationContext)}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        changeFragment(mainFragment2)

        if (networkStatus == NetworkStatus.TYPE_NOT_CONNECTED) {
            Toast.makeText(baseContext, "무드등을 이용하시려면 Wifi연결이 필요합니다.", Toast.LENGTH_SHORT).show()
        }
//        val navView: BottomNavigationView = findViewById(R.id.bottomNavigation)
//// sets background color for the whole bar
//        navView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))

        findViewById<BottomNavigationView>(R.id.bottomNavigation).setOnItemSelectedListener { item ->
            when (item.itemId) {
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
        findViewById<FloatingActionButton>(R.id.faBtn).setOnClickListener {
            //병주 클래스
        }

    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager .beginTransaction()
            .replace(R.id.mainFrame, fragment) .commit()
    }

}