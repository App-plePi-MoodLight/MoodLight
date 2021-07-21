package com.example.moodlight.screen.main3

import android.animation.LayoutTransition
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.moodlight.R
import com.example.moodlight.database.UserDatabase
import com.example.moodlight.databinding.FragmentMain3Binding
import com.example.moodlight.screen.initial.InitialActivity
import com.example.moodlight.dialog.CommonDialog
import com.example.moodlight.screen.MainActivity
import com.example.moodlight.screen.main3.setting.SettingActivity
import com.example.moodlight.util.FirebaseUtil
import com.example.moodlight.util.GetTime
import com.google.android.gms.common.internal.service.Common
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainFragment3 : Fragment() {
    private var activity : MainActivity? = MainActivity()

    private lateinit var binding: FragmentMain3Binding
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



        binding.main3CommentSwitch.setOnCheckedChangeListener { buttonView, isChecked ->

            Main3Helper.setCommentAlarm(isChecked)
            viewModel.commentIsChecked.value = isChecked
        }

        binding.main3LikeSwitch.setOnCheckedChangeListener { buttonView, isChecked ->

            Main3Helper.setLikeAlarm(isChecked)
            viewModel.likeIsChecked.value = isChecked
        }

        binding.main3Btn1.setOnClickListener {
            startActivity(Intent(requireContext(), SettingActivity::class.java))
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


    @SuppressLint("SetTextI18n")
    private fun setUi() {
        CoroutineScope(Dispatchers.IO).launch {
            FirebaseUtil.getFireStoreInstance().collection("users")
                .document(FirebaseUtil.getUid())
                .get()
                .addOnCompleteListener {
                    val millisTime = it.result!!.get("joinTime") as Long
                    val time = GetTime.getTime(millisTime)
                    viewModel.username.value = it.result!!.get("nickname") as String
                    viewModel.email.value = FirebaseUtil.getAuth().currentUser!!.email
                    viewModel.main3Tv1Text.value = "무드등을 시작한지 $time 지났어요!"
                    viewModel.subscription.value = GetTime.modifyJoinTime(millisTime).toString()
                    viewModel.commentIsChecked.value = it.result!!.get("commentAlarm") as Boolean
                    viewModel.likeIsChecked.value = it.result!!.get("likeAlarm") as Boolean
                }
        }
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