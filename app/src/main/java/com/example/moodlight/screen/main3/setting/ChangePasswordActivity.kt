package com.example.moodlight.screen.main3.setting

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.moodlight.R
import com.example.moodlight.api.ServerClient
import com.example.moodlight.database.UserData
import com.example.moodlight.database.UserDatabase
import com.example.moodlight.databinding.ActivityChangePasswordBinding
import com.example.moodlight.dialog.LoadingDialog
import com.example.moodlight.model.setting.ChangePasswordModel
import com.example.moodlight.model.setting.SuccussChangePasswordModel
import com.example.moodlight.screen.findpassword.FindPasswordActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var binding : ActivityChangePasswordBinding
    private lateinit var db : UserDatabase
    private val loadingDialog : LoadingDialog by lazy {
        LoadingDialog(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)


        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_password)
        db = UserDatabase.getInstance(this)!!

        setSupportActionBar(binding.main2Toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.back_btn)


        binding.checkBtn.setOnClickListener {
            checkIsRightPw()
        }
        binding.findPasswordText.setOnClickListener {
            startActivity(Intent(this, FindPasswordActivity::class.java))
        }
    }

    private fun checkIsRightPw() {
        //초기화
        binding.danger1.visibility = View.GONE
        binding.danger2.visibility = View.GONE
        binding.danger3.visibility = View.GONE


        if(binding.defaultPwEt.text.isEmpty() or
            binding.newPwEt.text.isEmpty() or
            binding.newPwAgainEt.text.isEmpty()){

            Toast.makeText(this, "필수로 입력해야되는 항목입니다.", Toast.LENGTH_SHORT).show()
        }
        else{
            CoroutineScope(Dispatchers.IO).launch {
                val userDb = db.userDao().getUserFromUserLoginTable() as List<com.example.moodlight.database.UserData>
                var isChangePw = true
                runOnUiThread {
                    Log.d(TAG, "checkIsRightPw: password : ${userDb[0].password}")
                    if(userDb[0].password != binding.defaultPwEt.text.toString()){
                        binding.danger1.visibility = View.VISIBLE
                        isChangePw = false
                    }
                    if(binding.newPwEt.length()<8 || binding.newPwEt.length() >16){
                        binding.danger2.visibility = View.VISIBLE
                        isChangePw = false
                    }
                    if(binding.newPwAgainEt.length()<8 || binding.newPwAgainEt.length() >16){
                        binding.danger3.visibility = View.VISIBLE
                        isChangePw = false
                    }
                    if(isChangePw){
                        changePw()
                    }

                }
            }
        }
    }

    private fun changePw() {
        if(binding.newPwAgainEt.text.toString() == binding.newPwEt.text.toString()){
            loadingDialog.show()
            CoroutineScope(Dispatchers.IO).launch {
                
                ServerClient.getApiService().changePassword(
                    ChangePasswordModel(binding.newPwEt.text.toString(), binding.defaultPwEt.text.toString()
                    )).enqueue(object : Callback<SuccussChangePasswordModel> {
                    override fun onResponse(
                        call: Call<SuccussChangePasswordModel>,
                        response: Response<SuccussChangePasswordModel>
                    ) {
                        val result = response.body()!!
                        if(result.success){
                            CoroutineScope(Dispatchers.IO).launch {
                                val user = UserDatabase.getInstance(this@ChangePasswordActivity)!!.userDao().getUserFromUserLoginTable()
                                UserDatabase.getInstance(this@ChangePasswordActivity)!!.userDao().updateLoginTable(UserData(user[0].loginID ,user[0].id, binding.newPwEt.text.toString()))
                                val user1 = UserDatabase.getInstance(this@ChangePasswordActivity)!!.userDao().getPassword()
                                Log.d(TAG, "onResponse: $user1")
                            }
                            Toast.makeText(this@ChangePasswordActivity, "비밀번호 변경에 성공하였습니다.", Toast.LENGTH_SHORT)
                                .show()
                            finish()
                        }
                        else{
                            Toast.makeText(this@ChangePasswordActivity, "비밀번호 변경에 실패하셨습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT)
                                .show()
                        }
                        runOnUiThread {
                            loadingDialog.dismiss()
                        }
                    }

                    override fun onFailure(call: Call<SuccussChangePasswordModel>, t: Throwable) {
                        Toast.makeText(this@ChangePasswordActivity, "비밀번호 변경에 실패하셨습니다. 다시 시도해주세요", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
        else{
            binding.textView1.text = "새로운 비밀번호가 일치하지 않습니다."
            binding.danger3.visibility = View.VISIBLE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}