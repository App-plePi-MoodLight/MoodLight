package com.example.moodlight.screen.main3.setting

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.moodlight.R
import com.example.moodlight.api.ServerClient
import com.example.moodlight.database.UserDatabase
import com.example.moodlight.databinding.ActivitySettingBinding
import com.example.moodlight.dialog.LoadingDialog
import com.example.moodlight.model.setting.SuccussChangePasswordModel
import com.example.moodlight.model.setting.UserExistModel
import com.example.moodlight.model.setting.UserUpdateModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates

class SettingActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySettingBinding
    private lateinit var userList : ArrayList<String>
    private lateinit var rdb : UserDatabase
    private lateinit var filepath : Uri
    private lateinit var bitmap : Bitmap
    private lateinit var userId : String
    private lateinit var userEmail : String
    private var isPushCommentAlarm by Delegates.notNull<Boolean>()
    private var isPushLikeAlarm by Delegates.notNull<Boolean>()
    private val loadingDialog : LoadingDialog by lazy {
        LoadingDialog(this@SettingActivity)
    }
    private var distinctCheckName = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        userId = intent.getStringExtra("userId").toString()
        userEmail = intent.getStringExtra("userEmail").toString()
        isPushCommentAlarm = intent.getBooleanExtra("isPushCommentAlarm", true)
        isPushLikeAlarm = intent.getBooleanExtra("isPushLikeAlarm", true)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting)


        binding.passwordChangeBtn.setOnClickListener {
            startActivity(Intent(this, ChangePasswordActivity::class.java))
        }

        binding.distinctCheckBtn.setOnClickListener {
            isDistinctNickName()
        }

        binding.checkBtn.setOnClickListener {
            updateUserInfo()
        }

        setSupportActionBar(binding.main2Toolbar)

        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.back_btn)
    }

    private fun isDistinctNickName() {
        loadingDialog.show()
        CoroutineScope(Dispatchers.IO).launch {
            ServerClient.getApiService().distinctNickName(binding.nickNameEt.text.toString()).enqueue(
                object : Callback<UserExistModel> {
                    override fun onResponse(
                        call: Call<UserExistModel>,
                        response: Response<UserExistModel>
                    ) {
                        loadingDialog.cancel()
                        if(response.code() == 200){
                            val it = response.body()
                            if(!it!!.exist){
                                binding.imageView2.setImageResource(R.drawable.img_success)
                                binding.dangerTv.setTextColor(Color.parseColor("#FF009900"))
                                binding.dangerTv.text = "사용가능한 닉네임입니다."
                                binding.dangerLayout.visibility = View.VISIBLE
                                distinctCheckName = true
                            }
                            else{
                                setDangerResult("이미 존재하는 닉네임입니다.")
                            }
                        }
                        else if(response.code() == 400){
                            setDangerResult("닉네임 글자 수는 3글자 이상 13글자 이하여야합니다.")
                        }
                    }

                    override fun onFailure(call: Call<UserExistModel>, t: Throwable) {
                        setDangerResult("네트워크 오류가 발생하였습니다.")
                    }
                })
        }
    }

    private fun setDangerResult(msg : String) {
        binding.imageView2.setImageResource(R.drawable.img_danger)
        binding.dangerTv.setTextColor(Color.parseColor("#FD3939"))
        binding.dangerTv.text = msg
        binding.dangerLayout.visibility = View.VISIBLE
    }

    private fun updateUserInfo() {
        loadingDialog.show()
        if(binding.nickNameEt.text.isEmpty()){
            setDangerResult("필수 입력 사항입니다.")
        }
        else if(!distinctCheckName){
            setDangerResult("닉네임 중복확인은 필수 사항입니다.")
        }
        else{
            CoroutineScope(Dispatchers.IO).launch {
                ServerClient.getApiService().updateUser(UserUpdateModel(binding.nickNameEt.text.toString(), userId, isPushCommentAlarm, isPushLikeAlarm)).enqueue(
                    object : Callback<SuccussChangePasswordModel> {
                        override fun onResponse(
                            call: Call<SuccussChangePasswordModel>,
                            response: Response<SuccussChangePasswordModel>
                        ) {
                            loadingDialog.cancel()
                            Toast.makeText(this@SettingActivity, "사용자변경에 성공하였습니다.", Toast.LENGTH_SHORT).show()
                            setResult(Activity.RESULT_OK)
                            finish()
                        }

                        override fun onFailure(call: Call<SuccussChangePasswordModel>, t: Throwable) {
                            setDangerResult("사용자 정보 변경에 실패하였습니다. 다시 시도해주세요")
                        }

                    })
            }
        }
    }

    private fun fileChooser() {
        var i = Intent()
            .setType("image/*")
            .setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(i, "Choose Picture"), 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 100&& resultCode == RESULT_OK&&data != null){
            filepath = data.data!!
            var bitmap = MediaStore.Images.Media.getBitmap(contentResolver, data.data!!)
            binding.imageView.setImageBitmap(bitmap)

            binding.imageView.setBackground(ShapeDrawable(OvalShape()));
            binding.imageView.setClipToOutline(true);
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