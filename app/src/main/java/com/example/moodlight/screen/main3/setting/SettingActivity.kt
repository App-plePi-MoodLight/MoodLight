package com.example.moodlight.screen.main3.setting

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import com.example.moodlight.hash.sha
import com.example.moodlight.R
import com.example.moodlight.database.UserDatabase
import com.example.moodlight.databinding.ActivitySettingBinding
import com.example.moodlight.util.FirebaseUtil
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class SettingActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySettingBinding
    private lateinit var db : FirebaseFirestore
    private lateinit var userList : ArrayList<String>
    private lateinit var rdb : UserDatabase
    private lateinit var filepath : Uri
    private lateinit var bitmap : Bitmap
    var curPw = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting)

        rdb = UserDatabase.getInstance(this)!!
        db = FirebaseUtil.getFireStoreInstance()
        getUserList()
        getCurUserPw()
        loadProFileImage()
        binding.imageView.setBackground(ShapeDrawable(OvalShape()));
        binding.imageView.setClipToOutline(true);
        binding.distinctCheckBtn.setOnClickListener {
            if (binding.nickNameEt.text.toString().isEmpty()){
                dangerResult("빈칸을 입력해주세요.")
            }
            else if (binding.nickNameEt.text.toString() in userList){
                dangerResult("이미 존재하는 닉네임입니다.")
            }
            else{
                binding.successLayout.visibility = View.VISIBLE
                binding.dangerLayout.visibility = View.INVISIBLE
            }
        }

        binding.IVBtn.setOnClickListener {
            fileChooser()
        }

        binding.checkBtn.setOnClickListener {
            if(binding.newPwEt.text.isEmpty() or binding.newPwAgainEt.text.isEmpty() or binding.defaultPwEt.text.isEmpty()){
                dangerPwResult("빈칸을 입력해주세요.")
//                if(sha.encryptSHA(binding.defaultPwEt.text.toString()) != curPw){
//                    Log.d(TAG, "onCreate: ${sha.encryptSHA(binding.defaultPwEt.text.toString()) != curPw}")
//                    dangerPwResult("기존의 비밀번호가 일치하지않습니다.")
//                    if(binding.newPwEt.text.toString() != binding.newPwAgainEt.text.toString()){
//                        dangerPwResult("비밀번호가 서로 일치하지 않습니다.")
//                    }
//                    else{
////                        binding.successPwLayout.visibility = View.VISIBLE
////                        binding.dangerPwLayout.visibility = View.INVISIBLE
//                    }
//                }
            }
            else if(sha.encryptSHA(binding.defaultPwEt.text.toString()) != curPw){
                dangerPwResult("기존의 비밀번호가 일치하지않습니다.")
            }
            else if(binding.newPwEt.text.toString() != binding.newPwAgainEt.text.toString()){
                dangerPwResult("비밀번호가 서로 일치하지 않습니다.")
            }
            else{
                binding.successPwLayout.visibility = View.VISIBLE
                binding.dangerPwLayout.visibility = View.INVISIBLE
                changePw(binding.newPwEt.text.toString(), binding.nickNameEt.text.toString())
                var imageRef = FirebaseStorage.getInstance().reference.child("image/${FirebaseUtil.getAuth().currentUser!!.uid}.jpg")
                imageRef.putFile(filepath)
                    .addOnSuccessListener {
                        Toast.makeText(this, "정보 수정이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener{
                        Toast.makeText(this, "정보 수정에 실패하였습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                    }
            }
        }


        setSupportActionBar(binding.main2Toolbar)

        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.left_btn)
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

    private fun loadProFileImage() {
        val storageRef = FirebaseStorage.getInstance().getReference().child("image/${FirebaseUtil.getAuth().currentUser!!.uid}.jpg")
        val localfile = File.createTempFile("tempImage", "jpg")
        storageRef.getFile(localfile).addOnSuccessListener {
            bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
            binding.imageView.setImageBitmap(bitmap)
        }.addOnFailureListener{
            binding.imageView.setImageResource(R.drawable.basic_profile)
        }
        binding.imageView.setBackground(ShapeDrawable(OvalShape()));
        binding.imageView.setClipToOutline(true);
    }

    private fun changePw(s : String, nickname : String) {
        val auth = FirebaseUtil.getAuth()
        auth.currentUser!!.updatePassword(s)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    Toast.makeText(this, "비밀번호 변경이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    db.collection("users").document(FirebaseUtil.getUid()).update("password", sha.encryptSHA(s), "nickname", nickname)
                        .addOnCompleteListener {
                            Toast.makeText(this, "", Toast.LENGTH_SHORT).show()
                        }

                }
                else{
                    Toast.makeText(this, "오류가 발생하였습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "오류가 발생하였습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun dangerPwResult(s: String) {
        binding.successPwLayout.visibility = View.INVISIBLE
        binding.dangerPwLayout.visibility = View.VISIBLE
        binding.dangerPwTv.text  = s
    }

    private fun getCurUserPw() {
        val auth = FirebaseUtil.getAuth()
        db.collection("users").document(auth.currentUser!!.uid).get()
            .addOnCompleteListener {
                curPw = it.result!!.get("password").toString()
            }
    }

    private fun dangerResult(s: String) {
        binding.successLayout.visibility = View.INVISIBLE
        binding.dangerLayout.visibility = View.VISIBLE
        binding.dangerTv.text = s
    }

    private fun getUserList() {
        db.collection("users").document("Storage").get()
            .addOnCompleteListener {
                if(it.isSuccessful){
                    userList = it.result!!.get("nicknameArray") as ArrayList<String>
                }
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