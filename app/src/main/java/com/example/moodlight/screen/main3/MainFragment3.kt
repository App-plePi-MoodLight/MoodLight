package com.example.moodlight.screen.main3

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.moodlight.R
import com.example.moodlight.databinding.FragmentMain3Binding
import com.example.moodlight.util.FirebaseUtil
import com.example.moodlight.util.GetTime
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainFragment3 : Fragment() {

    private lateinit var binding : FragmentMain3Binding
    private val viewModel : Main3ViewModel by lazy {
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

        binding.main3CommentSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked)
                Main3Helper.setCommentAlarm(isChecked)
            else
                Main3Helper.setCommentAlarm(isChecked)
        }

        binding.main3LikeSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked)
                Main3Helper.setLikeAlarm(isChecked)
            else
                Main3Helper.setLikeAlarm(isChecked)
        }

        setUi()

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun setUi () : Unit {
        CoroutineScope(Dispatchers.IO).launch{
            FirebaseUtil.getFireStoreInstance().collection("users")
                .document(FirebaseUtil.getUid())
                .get()
                .addOnCompleteListener{
                    val millisTime = it.result!!.get("joinTime") as Long
                    val time = GetTime.getTime(millisTime)

                    viewModel.email.value = it.result!!.get("email") as String
                    viewModel.username.value = it.result!!.get("nickname") as String
                    viewModel.main3Tv1Text.value = "무드등을 시작한지 $time 지났어요!"
                    viewModel.subscription.value = GetTime.modifyJoinTime(millisTime).toString()
                    viewModel.commentIsChecked.value = it.result!!.get("commentAlarm") as Boolean
                    viewModel.likeIsChecked.value = it.result!!.get("likeAlarm") as Boolean

                }
        }
    }

}