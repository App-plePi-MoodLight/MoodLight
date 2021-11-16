package com.example.moodlight.screen.mainstatics

import android.animation.LayoutTransition
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.moodlight.R
import com.example.moodlight.api.ServerClient
import com.example.moodlight.databinding.FragmentMainStatisticsBinding
import com.example.moodlight.model.moodcount.MoodCountModel
import com.example.moodlight.model.moodcount.MoodCountModelItem
import com.example.moodlight.model.question_response.QuestionResponseModel
import com.example.moodlight.screen.main2.calendar.Main2CalendarViewModel
import com.example.moodlight.util.AppUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainStatisticsFragment : Fragment() {

    private lateinit var binding : FragmentMainStatisticsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_statistics, container, false)

        (binding.wholeLayout as ViewGroup).layoutTransition.apply {
            val appearingAnimator = ObjectAnimator.ofFloat(view, "translationX", -1000f, 0f)
            val disappearingAnimator = ObjectAnimator.ofFloat(view, "translationX", 0f, 1000f)
            this.setAnimator(LayoutTransition.APPEARING, appearingAnimator)
            this.setAnimator(LayoutTransition.DISAPPEARING, disappearingAnimator)
            this.setStartDelay(LayoutTransition.APPEARING, 2000L)
            this.setDuration(LayoutTransition.APPEARING, 700L)
        }

        setNowQuestion()
        setNowDate()
        setLastQuestion()
        setLastDate()

        AppUtil.setBaseCalendarList(ViewModelProvider(requireActivity()).get(Main2CalendarViewModel::class.java))

        setAnimation()

        return binding.root
    }

    private fun setNowQuestion() {
        Log.e("test",AppUtil.getNowDate())
        ServerClient.getApiService().getQuestion(AppUtil.getNowDate())
            .enqueue(object : Callback<QuestionResponseModel> {

                override fun onResponse(
                    call: Call<QuestionResponseModel>,
                    response: Response<QuestionResponseModel>
                ) {
                    if (response.isSuccessful) {
                        try {
                            binding.todayQuestion = response.body()!![0].contents
                        } catch (e : IndexOutOfBoundsException) {
                            binding.todayQuestion = "아직 오늘의 질문이 등록되지 않았어요."
                        }
                    }
                }

                override fun onFailure(call: Call<QuestionResponseModel>, t: Throwable) {
                    t.printStackTrace()
                }

            })
    }

    private fun setLastQuestion() {
        Log.e("xx",AppUtil.getLastDate())
        ServerClient.getApiService().getQuestion(AppUtil.getLastDate())
            .enqueue(object : Callback<QuestionResponseModel> {

                override fun onResponse(
                    call: Call<QuestionResponseModel>,
                    response: Response<QuestionResponseModel>
                ) {
                    if (response.isSuccessful) {

                        try {
                            binding.lastQuestion = response.body()!![0].contents
                        } catch (e : IndexOutOfBoundsException) {
                            binding.lastQuestion = "어제의 질문이 등록되지 않았어요."
                        }
                    }
                }

                override fun onFailure(call: Call<QuestionResponseModel>, t: Throwable) {
                    t.printStackTrace()
                }

            })
    }

    private fun setNowDate() {
        ServerClient.getApiService().getMoodCount(AppUtil.getNowDate())
            .enqueue(object : Callback<MoodCountModel> {

                override fun onResponse(
                    call: Call<MoodCountModel>,
                    response: Response<MoodCountModel>
                ) {
                    var todayHappyCount = 0
                    var todayMadCount = 0
                    var todaySadCount = 0
                    if (response.isSuccessful) {
                        val moodCountList : ArrayList<MoodCountModelItem> = response.body()!!
                        Log.e("nowdate", moodCountList.toString())

                        if (moodCountList.size > 0 && moodCountList[0].mood != null) {
                            for (i in 0..2) {
                                when(moodCountList[i].mood) {
                                    "happy" -> {
                                        todayHappyCount = moodCountList[i].count
                                    }
                                    "mad" -> {
                                        todayMadCount = moodCountList[i].count
                                    }
                                    "sad" -> {
                                        todaySadCount = moodCountList[i].count
                                    }
                                }
                            }
                        }

                        binding.happySum = todayHappyCount.toString()
                        binding.madSum = todayMadCount.toString()
                        binding.sadSum = todaySadCount.toString()
                    }
                    else {
                        Toast.makeText(requireContext(), "error : "+response.code(), Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<MoodCountModel>, t: Throwable) {
                    t.printStackTrace()
                }

            })
    }

    private fun setLastDate() {
        ServerClient.getApiService().getMoodCount(AppUtil.getLastDate())
            .enqueue(object : Callback<MoodCountModel> {

                override fun onResponse(
                    call: Call<MoodCountModel>,
                    response: Response<MoodCountModel>
                ) {
                    var lastHappyCount = 0
                    var lastMadCount = 0
                    var lastSadCount = 0
                    if (response.isSuccessful) {
                        val moodCountList : ArrayList<MoodCountModelItem> = response.body()!!
                        if (moodCountList.size > 0 && moodCountList[0].mood != null) {
                            for (i in 0..2) {
                                when (moodCountList[i].mood) {
                                    "happy" -> {
                                        lastHappyCount = moodCountList[i].count
                                    }
                                    "mad" -> {
                                        lastMadCount = moodCountList[i].count
                                    }
                                    "sad" -> {
                                        lastSadCount = moodCountList[i].count
                                    }
                                }
                            }

                            CoroutineScope(Dispatchers.Main).launch {
                                val counts = setOf(lastHappyCount, lastMadCount, lastSadCount)
                                when (counts.maxOrNull()) {
                                    lastHappyCount -> {
                                        binding.mainStatsLastHappyIv.layoutParams =
                                            LinearLayout.LayoutParams(getSize(), getSize())
                                        binding.lastStatsText1 = getString(R.string.happy_topic1)
                                        binding.lastStatsText2 = getRandomTopic(R.array.happy_topic)
                                    }
                                    lastMadCount -> {
                                        binding.mainStatsLastMadIv.layoutParams =
                                            LinearLayout.LayoutParams(getSize(), getSize())
                                        binding.lastStatsText1 = getString(R.string.mad_topic1)
                                        binding.lastStatsText2 = getRandomTopic(R.array.mad_topic)
                                    }
                                    lastSadCount -> {
                                        binding.mainStatsLastSadIv.layoutParams =
                                            LinearLayout.LayoutParams(getSize(), getSize())
                                        binding.lastStatsText1 = getString(R.string.sad_topic1)
                                        binding.lastStatsText2 = getRandomTopic(R.array.sad_topic)
                                    }
                                }
                            }
                        } else {
                            binding.lastStatsText1 = "어제는 아무 게시물이 올라오지 않았어요."
                            binding.lastStatsText2 = ""
                        }

                        binding.lastHappySum = lastHappyCount.toString()
                        binding.lastMadSum = lastMadCount.toString()
                        binding.lastSadSum = lastSadCount.toString()
                    }
                    else {
                        Toast.makeText(requireContext(), "error : "+response.code(), Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<MoodCountModel>, t: Throwable) {
                    t.printStackTrace()
                }

            })
    }

    fun getRandomTopic(arrayId : Int) : String {
        val random = java.util.Random()
        var array = resources.getStringArray(arrayId)

        return array[random.nextInt(4)]
    }

    fun getSize() : Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 61.0f, resources.displayMetrics).toInt()
    }

    private fun setAnimation() : Unit {
        binding.mainStatsTv1.postDelayed({
            binding.mainStatsTv1.isVisible = true
        }, 50L)
        binding.mainStatsTodayQuestionTv.postDelayed({
            binding.mainStatsTodayQuestionTv.isVisible = true
        }, 200L)
        binding.mainStatsTodayLayout.postDelayed({
            binding.mainStatsTodayLayout.isVisible = true
        }, 250L)
        binding.mainStatsTv2.postDelayed({
            binding.mainStatsTv2.isVisible = true
        }, 300L)
        binding.mainStatsLastQuestionTv.postDelayed({
            binding.mainStatsLastQuestionTv.isVisible = true
        }, 350L)
        binding.mainStatsLastLayout.postDelayed({
            binding.mainStatsLastLayout.isVisible = true
        }, 400L)
        binding.mainStatsLastStatsTv1.postDelayed({
            binding.mainStatsLastStatsTv1.isVisible = true
        }, 450L)
        binding.mainStatsLastStatsTv2.postDelayed({
            binding.mainStatsLastStatsTv2.isVisible = true
        }, 500L)
    }


}