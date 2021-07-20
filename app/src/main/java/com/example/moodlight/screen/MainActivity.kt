package com.example.moodlight.screen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.moodlight.R
import com.example.moodlight.dialog.CommonDialog
import com.example.moodlight.dialog.CommonDialogInterface
import com.example.moodlight.dialog.LogoutDialog
import com.example.moodlight.dialog.LogoutDialogInterface
import com.example.moodlight.screen.initial.InitialActivity
import com.example.moodlight.screen.main1.CommunityActiviy
import com.example.moodlight.screen.main2.MainFragment2
import com.example.moodlight.screen.main3.MainFragment3
import com.example.moodlight.util.FirebaseUtil
import com.example.moodlight.util.NetworkStatus
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(), CommonDialogInterface, LogoutDialogInterface {


    private val mainFragment2 by lazy {MainFragment2()}
    private val mainFragment3 by lazy {MainFragment3()}
    private val networkStatus : Int by lazy {NetworkStatus.getConnectivityStatus(applicationContext)}

    private lateinit var dialog : CommonDialog
    private lateinit var logoutDialog: LogoutDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        changeFragment(mainFragment2)

        Log.e("a", System.currentTimeMillis().toString())

        if (networkStatus == NetworkStatus.TYPE_NOT_CONNECTED) {
            Toast.makeText(baseContext, "무드등k을 이용하시려면 Wifi연결이 필요합니다.", Toast.LENGTH_SHORT).show()
        }
        dialog = CommonDialog(this, this
            , "회원탈퇴"
            , "정말로 탈퇴를 하시겠습니까?\n탈퇴 이후의 정보는 되돌릴 수 없습니다."
            , "탈퇴하기"
            , "취소")
        logoutDialog = LogoutDialog(this, this, "로그아웃", "로그아웃을 하시겠습니까?", "로그아웃", "취소")


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
            startActivity(Intent(this, CommunityActiviy::class.java))
            //병주 클래스
        }

    }

    fun changeFragment(fragment: Fragment) {
        supportFragmentManager .beginTransaction()
            .replace(R.id.mainFrame, fragment) .commit()
    }

    fun onClickBtnInFragment(i : Int){
        when(i){
            1->{
                dialogShow()
            }
            2->{
                logoutDialogShow()
            }
        }
    }

    private fun logoutDialogShow() {
        logoutDialog.show()
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
                    finish()
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

    override fun onClickLogout() {
        FirebaseUtil.getAuth().signOut()
        startActivity(Intent(this, InitialActivity::class.java))
        finish()
    }

    override fun onCancelLogout() {
        logoutDialog.cancel()
    }


}