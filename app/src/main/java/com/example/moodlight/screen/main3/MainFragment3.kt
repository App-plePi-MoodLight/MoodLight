package com.example.moodlight.screen.main3

import android.animation.LayoutTransition
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toFile
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.moodlight.R
import com.example.moodlight.api.ServerClient
import com.example.moodlight.database.UserData
import com.example.moodlight.database.UserDatabase
import com.example.moodlight.databinding.FragmentMain3Binding
import com.example.moodlight.screen.initial.InitialActivity
import com.example.moodlight.dialog.CommonDialog
import com.example.moodlight.model.setting.UserModel
import com.example.moodlight.screen.MainActivity
import com.example.moodlight.screen.main3.setting.SettingActivity
import com.example.moodlight.util.FirebaseUtil
import com.example.moodlight.util.GetTime
import com.google.android.gms.common.internal.service.Common
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField
import java.util.*
import javax.security.auth.callback.Callback

class MainFragment3 : Fragment() {
    private var activity : MainActivity? = MainActivity()

    private lateinit var binding: FragmentMain3Binding
    private lateinit var bitmap : Bitmap
    private val viewModel: Main3ViewModel by lazy {
        ViewModelProvider(this).get(Main3ViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main3, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.fragment = this


        (binding.wholeLayout as ViewGroup).layoutTransition.apply {
            val appearingAnimator = ObjectAnimator.ofFloat(view, "translationX", -1000f, 0f)
            val disappearingAnimator = ObjectAnimator.ofFloat(view, "translationX", 0f, 1000f)
            this.setAnimator(LayoutTransition.APPEARING, appearingAnimator)
            this.setAnimator(LayoutTransition.DISAPPEARING, disappearingAnimator)
            this.setStartDelay(LayoutTransition.APPEARING, 2000L)
            this.setDuration(LayoutTransition.APPEARING, 700L)
        }


        if (viewModel.email.value.equals(""))
            Main3Helper.setAnimation(binding)
        else
            Main3Helper.setVisible(binding)
        loadProFileImage()


        binding.main3CommentSwitch.setOnCheckedChangeListener { buttonView, isChecked ->

            Main3Helper.setCommentAlarm(isChecked)
            viewModel.commentIsChecked.value = isChecked
        }

        binding.main3LikeSwitch.setOnCheckedChangeListener { buttonView, isChecked ->

            Main3Helper.setLikeAlarm(isChecked)
            viewModel.likeIsChecked.value = isChecked
        }

        binding.main3Btn1.setOnClickListener {
            startActivity(Intent(requireActivity(), SettingActivity::class.java))
        }

        binding.main3WithdrawalTv.setOnClickListener {
            activity?.onClickBtnInFragment(1)

        }
        binding.main3LogoutBtn.setOnClickListener {
            activity?.onClickBtnInFragment(2)
        }

        setUi()



        return binding.root
    }

    private fun loadProFileImage() {
//        val storageRef = FirebaseStorage.getInstance().getReference().child("image/${FirebaseUtil.getAuth().currentUser!!.uid}.jpg")
//        val localfile = File.createTempFile("tempImage", "jpg")
//        storageRef.getFile(localfile).addOnSuccessListener {
//            bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
//            binding.main3ProfileIv.setImageBitmap(bitmap)
//        }.addOnFailureListener{
//            binding.main3ProfileIv.setImageResource(R.drawable.basic_profile)
//        }
//        binding.main3ProfileIv.setBackground(ShapeDrawable(OvalShape()));
//        binding.main3ProfileIv.setClipToOutline(true);
    }


    @SuppressLint("SetTextI18n")
    private fun setUi() {
        val db = UserDatabase.getInstance(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            ServerClient.getApiService().getUserInfo()
                .enqueue(object : retrofit2.Callback<UserModel>{
                    override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                        val it = response.body();
                        Log.d(TAG, "onResponse: result $it $response")
                        if(response.code() == 200){
                            Log.d(TAG, "onResponse: it time : ${it!!.created_date}")
                            setAnimation()
                            var sf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.ss'Z'")
                            var date = sf.parse(it.created_date)
                            var minus = GetTime.getTime(date.time)

                            viewModel.username.value = it!!.nickname
                            viewModel.email.value = it.email
                            viewModel.main3Tv1Text.value = "무드등을 시작한지 ${minus}지났어요."
                        }
                    }

                    override fun onFailure(call: Call<UserModel>, t: Throwable) {
                        t.printStackTrace()
                    }

                })
        }
//        CoroutineScope(Dispatchers.IO).launch {
//            FirebaseUtil.getFireStoreInstance().collection("users")
//                .document(FirebaseUtil.getUid())
//                .get()
//                .addOnCompleteListener {
//                    val millisTime = it.result!!.get("joinTime") as Long
//                    val time = GetTime.getTime(millisTime)
//                    viewModel.username.value = it.result!!.get("nickname") as String
//                    viewModel.email.value = FirebaseUtil.getAuth().currentUser!!.email
//                    viewModel.main3Tv1Text.value = "무드등을 시작한지 $time 지났어요!"
//                    viewModel.subscription.value = GetTime.modifyJoinTime(millisTime).toString()
//                    viewModel.commentIsChecked.value = it.result!!.get("commentAlarm") as Boolean
//                    viewModel.likeIsChecked.value = it.result!!.get("likeAlarm") as Boolean
//                }
//        }
    }
    public fun signOut(view: View): Unit {
        CoroutineScope(Dispatchers.IO).launch {
            UserDatabase.getInstance(requireContext())!!.userDao().deleteUserLoginTable()
        }
        FirebaseUtil.getAuth().signOut()
        val intent = Intent(requireContext(), InitialActivity::class.java)
        requireActivity().startActivity(intent)
        requireActivity().finish()
    }

    private fun setAnimation(): Unit {
        binding.main3ProfileIv.postDelayed({
            binding.main3ProfileIv.isVisible = true
        }, 50L)

        binding.main3Tv1.postDelayed({
            binding.main3UserNameTv.isVisible = true
        }, 200L)
        binding.main3EmailTv.postDelayed({
            binding.main3EmailTv.isVisible = true
        }, 250L)
        binding.main3Tv1.postDelayed({
            binding.main3Tv1.isVisible = true
        }, 300L)
        binding.main3Btn1.postDelayed({
            binding.main3Btn1.isVisible = true
        }, 350L)
        binding.main3Tv2.postDelayed({
            binding.main3Tv2.isVisible = true
        }, 400L)
        binding.main3Tv3.postDelayed({
            binding.main3Tv3.isVisible = true
        }, 450L)
        binding.main3CommentSwitch.postDelayed({
            binding.main3CommentSwitch.isVisible = true
        }, 500L)
        binding.main3Tv4.postDelayed({
            binding.main3Tv4.isVisible = true
        }, 550L)
        binding.main3LikeSwitch.postDelayed({
            binding.main3LikeSwitch.isVisible = true
        }, 600L)
        binding.main3Tv5.postDelayed({
            binding.main3Tv5.isVisible = true
        }, 650L)
        binding.main3SubscriptionTv.postDelayed({
            binding.main3SubscriptionTv.isVisible = true
        }, 700L)
        binding.main3LogoutBtn.postDelayed({
            binding.main3LogoutBtn.isVisible = true
        }, 750L)
        binding.main3WithdrawalTv.postDelayed({
            binding.main3WithdrawalTv.isVisible = true
        }, 800L)

    }

    override fun onResume() {
        super.onResume()
        loadProFileImage()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = getActivity() as MainActivity
    }

    override fun onDetach() {
        super.onDetach()
        activity = null
    }
}