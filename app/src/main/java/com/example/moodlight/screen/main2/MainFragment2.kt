package com.example.moodlight.screen.main2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.moodlight.R
import com.example.moodlight.databinding.FragmentMain2Binding
import com.example.moodlight.screen.main2.diaryRecyclerview.data.DateAdapter
import com.example.moodlight.screen.main2.diaryRecyclerview.data.DateClass
import com.example.moodlight.screen.main2.diaryRecyclerview.data.QnAData
import java.util.*

class MainFragment2 : Fragment() {

    private lateinit var binding : FragmentMain2Binding
    var list : ArrayList<DateClass> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main2, container, false)

        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val data : ArrayList<QnAData> = ArrayList()
        data.add(QnAData("오늘 점심은 뭐 먹죠?", "점심을 먹죠 ㅎㅎ"))
        data.add(QnAData("오늘 저녁은 뭐 먹죠?", "저녁을 먹죠 ㅎㅎ"))
        list.add(DateClass("3월 16일", data))
        list.add(DateClass("4월 16일", data))
        binding.recycler.adapter = DateAdapter(requireContext(), list)
        binding.recycler.setHasFixedSize(true)
    }
}