package com.example.moodlight.screen

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.moodlight.R
import com.example.moodlight.api.ServerClient
import com.example.moodlight.database.UserDatabase
import com.example.moodlight.dialog.*
import com.example.moodlight.model.setting.DeleteUserModel
import com.example.moodlight.screen.initial.InitialActivity
import com.example.moodlight.screen.main1.PickMoodActivity
import com.example.moodlight.screen.main2.MainFragment2
import com.example.moodlight.screen.main3.MainFragment3
import com.example.moodlight.screen.main3.setting.SettingActivity
import com.example.moodlight.screen.mainstatics.MainStatisticsFragment
import com.example.moodlight.util.NetworkStatus
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(), CommonDialogInterface, LogoutDialogInterface {
    private val mainFragment2 by lazy { MainFragment2() }
    private val mainFragment3 by lazy { MainFragment3() }
    private val mainStatisticsFragment by lazy { MainStatisticsFragment() }
    private val networkStatus: Int by lazy { NetworkStatus.getConnectivityStatus(applicationContext) }

    private lateinit var dialog: CommonDialog
    private lateinit var logoutDialog: LogoutDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.moodlight.R.layout.activity_main)
        changeFragment(mainStatisticsFragment)
        findViewById<BottomNavigationView>(com.example.moodlight.R.id.bottomNavigation).selectedItemId =
            R.id.nullItem

        dialog = CommonDialog(
            this, this, "회원탈퇴", "정말로 탈퇴를 하시겠습니까?\n탈퇴 이후의 정보는 되돌릴 수 없습니다.", "탈퇴하기", "취소"
        )
        logoutDialog = LogoutDialog(this, this, "로그아웃", "로그아웃을 하시겠습니까?", "로그아웃", "취소")


        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            val msg = token!!
            Log.d(TAG, msg)
            Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
        })


        findViewById<BottomNavigationView>(com.example.moodlight.R.id.bottomNavigation).setOnItemSelectedListener { item ->
            when (item.itemId) {
                com.example.moodlight.R.id.main2 -> {
                    changeFragment(mainFragment2)
                    true
                }
                com.example.moodlight.R.id.main3 -> {
                    changeFragment(mainFragment3)
                    true
                }
                else -> false
            }
        }
        findViewById<FloatingActionButton>(R.id.faBtn).setOnClickListener {
            startActivity(Intent(this, PickMoodActivity::class.java))
        }
    }
        fun changeFragment(fragment: Fragment) {
            supportFragmentManager.beginTransaction()
                .replace(com.example.moodlight.R.id.mainFrame, fragment).commit()
        }

        fun onClickBtnInFragment(i: Int) {
            when (i) {
                1 -> {
                    dialogShow()
                }
                2 -> {
                    logoutDialogShow()
                }
                3 ->{
                    val intent = Intent(this@MainActivity, SettingActivity::class.java)
                    startActivityForResult(intent, 100)
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
            CoroutineScope(Dispatchers.IO).launch {
                ServerClient.getApiService().deleteUser().enqueue(object : Callback<DeleteUserModel> {
                    override fun onResponse(
                        call: Call<DeleteUserModel>,
                        response: Response<DeleteUserModel>
                    ) {
                        val result = response.body()
                        if(response.code() == 200){
                            if(!result!!.success){
                                Toast.makeText(this@MainActivity, "회원탈퇴에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                            }
                            else{
                                CoroutineScope(Dispatchers.IO).launch{
                                    UserDatabase.getInstance(this@MainActivity)!!.userDao().deleteUserLoginTable()
                                }
                                Toast.makeText(this@MainActivity, result.message, Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this@MainActivity, InitialActivity::class.java))
                                dialog.dismiss()
                                finish()
                            }
                        }
                    }

                    override fun onFailure(call: Call<DeleteUserModel>, t: Throwable) {
                        Toast.makeText(this@MainActivity, "회원탈퇴에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                    }

                })
            }
        }

    override fun onCancleBtnClick() {
        dialog.cancel()
    }

    override fun onClickLogout() {
        CoroutineScope(Dispatchers.IO).launch{
            UserDatabase.getInstance(this@MainActivity)!!.userDao().deleteUserLoginTable()
        }
        logoutDialog.dismiss()
        startActivity(Intent(this, InitialActivity::class.java))
        finish()
    }

    override fun onCancelLogout() {
        logoutDialog.dismiss()
    }
}
