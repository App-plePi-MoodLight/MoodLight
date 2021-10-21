package com.example.moodlight.screen.mainstatics

import android.animation.LayoutTransition
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.moodlight.R
import com.example.moodlight.api.ServerClient
import com.example.moodlight.databinding.FragmentMainStatisticsBinding
import com.example.moodlight.model.MoodCountModel
import com.example.moodlight.model.MoodCountModelItem
import com.example.moodlight.util.AppUtil
import com.example.moodlight.util.FirebaseUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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


/*        setLastDate()
        setNowDate()*/

        FirebaseUtil.getFireStoreInstance().collection("post")
            .document("information")
            .get()
            .addOnCompleteListener {
                val lastHappyCount = it.result!!.get("lastHappyCount").toString().toInt()
                val lastMadCount = it.result!!.get("lastMadCount").toString().toInt()
                val lastSadCount = it.result!!.get("lastSadCount").toString().toInt()

                CoroutineScope(Dispatchers.Main).launch {
                    val counts = setOf(lastHappyCount, lastMadCount, lastSadCount)
                    when(counts.maxOrNull()) {
                        lastHappyCount -> {
                            binding.mainStatsLastHappyIv.layoutParams = LinearLayout.LayoutParams(getSize(), getSize())
                            binding.lastStatsText1 = getString(R.string.happy_topic1)
                            binding.lastStatsText2 = getRandomTopic(R.array.happy_topic)
                        }
                        lastMadCount -> {
                            binding.mainStatsLastMadIv.layoutParams = LinearLayout.LayoutParams(getSize(), getSize())
                            binding.lastStatsText1 = getString(R.string.mad_topic1)
                            binding.lastStatsText2 = getRandomTopic(R.array.mad_topic)
                        }
                        lastSadCount -> {
                            binding.mainStatsLastSadIv.layoutParams = LinearLayout.LayoutParams(getSize(), getSize())
                            binding.lastStatsText1 = getString(R.string.sad_topic1)
                            binding.lastStatsText2 = getRandomTopic(R.array.sad_topic)
                        }
                    }
                }

                binding.todayQuestion = it.result!!.get("todayQuestion") as String
                binding.lastQuestion = it.result!!.get("lastQuestion") as String
                binding.happySum = it.result!!.get("todayHappyCount").toString()
                binding.madSum = it.result!!.get("todayMadCount").toString()
                binding.sadSum = it.result!!.get("todaySadCount").toString()
                binding.lastHappySum = it.result!!.get("lastHappyCount").toString()
                binding.lastMadSum = it.result!!.get("lastMadCount").toString()
                binding.lastSadSum = it.result!!.get("lastSadCount").toString()

            }

        setAnimation()

        return binding.root
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
                        if (moodCountList[0].mood != null) {
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
                        if (moodCountList[0].mood != null) {
                            for (i in 0..2) {
                                when(moodCountList[i].mood) {
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
                        }

                        CoroutineScope(Dispatchers.Main).launch {
                            val counts = setOf(lastHappyCount, lastMadCount, lastSadCount)
                            when(counts.maxOrNull()) {
                                lastHappyCount -> {
                                    binding.mainStatsLastHappyIv.layoutParams = LinearLayout.LayoutParams(getSize(), getSize())
                                    binding.lastStatsText1 = getString(R.string.happy_topic1)
                                    binding.lastStatsText2 = getRandomTopic(R.array.happy_topic)
                                }
                                lastMadCount -> {
                                    binding.mainStatsLastMadIv.layoutParams = LinearLayout.LayoutParams(getSize(), getSize())
                                    binding.lastStatsText1 = getString(R.string.mad_topic1)
                                    binding.lastStatsText2 = getRandomTopic(R.array.mad_topic)
                                }
                                lastSadCount -> {
                                    binding.mainStatsLastSadIv.layoutParams = LinearLayout.LayoutParams(getSize(), getSize())
                                    binding.lastStatsText1 = getString(R.string.sad_topic1)
                                    binding.lastStatsText2 = getRandomTopic(R.array.sad_topic)
                                }
                            }
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