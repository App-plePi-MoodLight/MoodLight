package com.example.moodlight.screen.onboarding

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.moodlight.R
import com.example.moodlight.screen.initial.InitialActivity
import com.example.moodlight.util.FirebaseUtil
import me.relex.circleindicator.CircleIndicator


class OnboardingActivity : AppCompatActivity() {
    private var vpAdapter: FragmentStatePagerAdapter? = null
    public lateinit var button : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        button = findViewById(R.id.checkBtn)

        vpAdapter = CustomPagerAdapter(supportFragmentManager, button)
        findViewById<ViewPager>(R.id.viewpager).adapter = vpAdapter

        findViewById<CircleIndicator>(R.id.circleIndicator).setViewPager(findViewById(R.id.viewpager))

        val preferences = getSharedPreferences("FirstCheck", MODE_PRIVATE)
        val checkFirst = preferences.getBoolean("checkFirst", false)
        if (checkFirst || FirebaseUtil.getAuth().getCurrentUser() != null) {
            startActivity(Intent(this@OnboardingActivity, InitialActivity::class.java))
            finish()
        }



    }

    class CustomPagerAdapter(fm: FragmentManager, btn : Button):FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){
        private val PAGENUMBER = 4
        private val button : Button = btn

        override fun getCount(): Int {
            return 3
        }

        override fun getItem(position: Int): Fragment {
            return when(position){
                0 -> {
                    Log.d("asdf", "getItem: $position")
                    button.visibility = View.INVISIBLE
                    OnboardFragment1.newInstance(R.drawable.ic_onboarding1, "오늘의 기분은", "오늘의 기분에 맞는 공간에서 \n" +
                            "질문에 답변하고 다른 사람들과 공유해보세요")
                }
                1-> {
                    Log.d("asdf", "getItem: $position")
                    button.visibility = View.INVISIBLE
                    OnboardFragment1.newInstance(R.drawable.ic_onboarding2, "너의 기분은 어떤데", "좋아요를 누르고 댓글을 달며\n" +
                            "다른 사람들을 위로해 주세요")
                }
                2->{
                    Log.d("asdf", "getItem: $position")
                    button.visibility = View.INVISIBLE
                    OnboardFragment1.newInstance(R.drawable.ic_onboarding3, "과거의 내 기분은 어땠을까", "나의 감정 일지에서\n" +
                            "이전의 나의 감정 기록들을 찾아보아요")
                }
                else->{
                    button.visibility = View.VISIBLE
                    OnboardFragment1.newInstance(R.drawable.ic_onboarding3, "과거의 내 기분은 어땠을까", "나의 감정 일지에서\n" +
                            "이전의 나의 감정 기록들을 찾아보아요")
                }
            }
        }
    }
}