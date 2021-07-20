package com.example.moodlight.screen

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.moodlight.R
import com.example.moodlight.dialog.CommonDialog
import com.example.moodlight.dialog.CommonDialogInterface
import com.example.moodlight.screen.initial.InitialActivity
import com.example.moodlight.screen.main1.MainFragment1
import com.example.moodlight.screen.main2.MainFragment2
import com.example.moodlight.screen.main3.MainFragment3
import com.example.moodlight.util.FirebaseUtil
import com.example.moodlight.util.NetworkStatus
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(), CommonDialogInterface {

    private val mainFragment1 by lazy {MainFragment1()}
    private val mainFragment2 by lazy {MainFragment2()}
    private val mainFragment3 by lazy {MainFragment3()}
    private val networkStatus : Int by lazy {NetworkStatus.getConnectivityStatus(applicationContext)}

    private lateinit var dialog : CommonDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        changeFragment(mainFragment2)

        Log.e("a", System.currentTimeMillis().toString())

        if (networkStatus == NetworkStatus.TYPE_NOT_CONNECTED) {
            Toast.makeText(baseContext, "무드등을 이용하시려면 Wifi연결이 필요합니다.", Toast.LENGTH_SHORT).show()
        }
        dialog = CommonDialog(this, this
            , "회원탈퇴"
            , "정말로 탈퇴를 하시겠습니까?\n탈퇴 이후의 정보는 되돌릴 수 없습니다."
            , "탈퇴하기"
            , "취소")
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

    fun onClickBtnInFragment(i : Int){
        dialogShow()
    }

    private fun dialogShow() {
        dialog.show()
    }

    override fun onCheckBtnClick() {
        FirebaseUtil.getAuth().currentUser!!.delete()
            .addOnCompleteListener {
                if(it.isSuccessful){
                    Toast.makeText(this, "회원탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, InitialActivity::class.java))
                }
                else{
                    Toast.makeText(this, "오류가 발생하였습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "오류가 발생하였습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onCancleBtnClick() {
        dialog.cancel()
    }


}