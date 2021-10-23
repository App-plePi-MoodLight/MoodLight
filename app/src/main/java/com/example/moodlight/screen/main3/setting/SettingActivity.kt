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
import com.example.moodlight.api.ServerClient
import com.example.moodlight.database.UserDatabase
import com.example.moodlight.databinding.ActivitySettingBinding
import com.example.moodlight.util.FirebaseUtil
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class SettingActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySettingBinding
    private lateinit var userList : ArrayList<String>
    private lateinit var rdb : UserDatabase
    private lateinit var filepath : Uri
    private lateinit var bitmap : Bitmap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting)


        binding.IVBtn.setOnClickListener {
            fileChooser()
        }
        binding.passwordChangeBtn.setOnClickListener {
            startActivity(Intent(this, ChangePasswordActivity::class.java))
        }

        binding.distinctCheckBtn.setOnClickListener {
            isDistinctNickName()
        }

        loadProFileImage()
        setSupportActionBar(binding.main2Toolbar)

        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.left_btn)
    }

    private fun isDistinctNickName() {
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
//        val storageRef = FirebaseStorage.getInstance().getReference().child("image/${FirebaseUtil.getAuth().currentUser!!.uid}.jpg")
//        val localfile = File.createTempFile("tempImage", "jpg")
//        storageRef.getFile(localfile).addOnSuccessListener {
//            bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
//            binding.imageView.setImageBitmap(bitmap)
//        }.addOnFailureListener{
//            binding.imageView.setImageResource(R.drawable.basic_profile)
//        }
//        binding.imageView.setBackground(ShapeDrawable(OvalShape()));
//        binding.imageView.setClipToOutline(true);
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