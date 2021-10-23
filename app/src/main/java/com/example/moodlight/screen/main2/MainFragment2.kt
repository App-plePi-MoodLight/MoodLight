package com.example.moodlight.screen.main2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.moodlight.R
import com.example.moodlight.api.ServerClient
import com.example.moodlight.databinding.FragmentMain2Binding
import com.example.moodlight.model.my_answer.MyAnswerModel
import com.example.moodlight.model.my_answer.MyAnswerModelItem
import com.example.moodlight.screen.main2.calendar.CalendarHelper
import com.example.moodlight.screen.main2.calendar.Main2CalendarAdapter
import com.example.moodlight.screen.main2.calendar.Main2CalendarData
import com.example.moodlight.screen.main2.calendar.Main2CalendarViewModel
import com.example.moodlight.screen.main2.diaryRecyclerview.data.DateAdapter
import com.example.moodlight.screen.main2.diaryRecyclerview.data.DateClass
import com.example.moodlight.screen.main2.diaryRecyclerview.data.QnAData
import com.example.moodlight.util.AppUtil
import com.example.moodlight.util.DataType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainFragment2 : Fragment() {

    private val calendarHelper by lazy { CalendarHelper() }
    private var writePostMap: Map<String, *>? = null

    private lateinit var myAnswerList : MyAnswerModel

    private val viewModel: Main2ViewModel by lazy {
        ViewModelProvider(this).get(Main2ViewModel::class.java)
    }

    private val calendarViewModel: Main2CalendarViewModel by lazy {
        ViewModelProvider(this).get(Main2CalendarViewModel::class.java)
    }

    private lateinit var binding: FragmentMain2Binding
    var list: ArrayList<DateClass> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main2, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.fragment = this

/*        CoroutineScope(Dispatchers.IO).launch {
            FirebaseUtil.getFireStoreInstance().collection("users")
                .document(FirebaseUtil.getUid())
                .get()
                .addOnCompleteListener {
                    writePostMap = it.result!!.get("writePostMap") as Map<String, *>?
                    setUi()
                }
        }*/


        ServerClient.getApiService().getMyAnswer()
            .enqueue(object : Callback<MyAnswerModel> {

                override fun onResponse(
                    call: Call<MyAnswerModel>,
                    response: Response<MyAnswerModel>
                ) {
                    if (response.isSuccessful) {
                        myAnswerList = response.body()!!
                        setUi()
                    }
                }

                override fun onFailure(call: Call<MyAnswerModel>, t: Throwable) {
                    t.printStackTrace()
                }

            })


        dataLoding()



        return binding.root
    }

    private fun dataLoding() {
        val data: ArrayList<QnAData> = ArrayList()
        if(list.isEmpty()){
            data.add(QnAData("오늘 점심은 뭐 먹죠?", "점심을 먹죠 ㅎㅎ"))
            data.add(QnAData("오늘 저녁은 뭐 먹죠?", "저녁을 먹죠 ㅎㅎ"))
            list.add(DateClass("3월 16일", data))
            list.add(DateClass("4월 16일", data))

        }
        binding.recycler.adapter = DateAdapter(requireContext(), list)
        binding.recycler.setHasFixedSize(true)
    }

    private fun setUi() {

        val adapter: Main2CalendarAdapter = Main2CalendarAdapter(calendarViewModel)
        binding.main2CalendarRecyclerView.adapter = adapter
        calendarViewModel.today = calendarHelper.getDate()

        setCalendar()
    }

    private fun setCalendar() {

        val lastEndDay: Int
        when (calendarHelper.getMonth() + 1) {
            1 -> {
                viewModel.month.value = "January"
                lastEndDay = 31
            }
            2 -> {
                viewModel.month.value = "February"
                lastEndDay = 31
            }
            3 -> {
                viewModel.month.value = "March"
                if (calendarHelper.getYear() % 4 == 0 && calendarHelper.getYear() % 100 != 0
                    || calendarHelper.getYear() % 400 == 0
                ) {
                    lastEndDay = 29
                } else
                    lastEndDay = 28
            }
            4 -> {
                viewModel.month.value = "April"
                lastEndDay = 31
            }
            5 -> {
                viewModel.month.value = "May"
                lastEndDay = 30
            }
            6 -> {
                viewModel.month.value = "June"
                lastEndDay = 31
            }
            7 -> {
                viewModel.month.value = "July"
                lastEndDay = 30
            }
            8 -> {
                viewModel.month.value = "August"
                lastEndDay = 31
            }
            9 -> {
                viewModel.month.value = "September"
                lastEndDay = 31
            }
            10 -> {
                viewModel.month.value = "October"
                lastEndDay = 30
            }
            11 -> {
                viewModel.month.value = "November"
                lastEndDay = 31
            }
            12 -> {
                viewModel.month.value = "December"
                lastEndDay = 30
            }
            else -> {
                viewModel.month.value = "error"
                lastEndDay = 30
            }
        }

        for (i in calendarHelper.getStartDayOfWeek() - 2 downTo 0) {
            calendarViewModel.dateList.add(
                Main2CalendarData(
                    (lastEndDay - i).toString(),
                    DataType.NONE_MOOD,
                    DataType.LAST_DAY
                )
            )
        }

        val targetAnswerList = ArrayList<MyAnswerModelItem>()

        for (j in 0 until myAnswerList.size) {
            if (CalendarHelper.dateTransformationToMonth(myAnswerList[j].createdDate).toInt()
            == calendarHelper.getMonth()+1 &&
                CalendarHelper.dateTransformationToYear(myAnswerList[j].createdDate).toInt()
            == calendarHelper.getYear()) {
                targetAnswerList.add(myAnswerList[j])
            } else {
                continue
            }

        }

        if (targetAnswerList.size <= 0) {
            for (k in 0 until calendarHelper.getEndDay()) {
                val main2CalendarData : Main2CalendarData = Main2CalendarData(
                    (k+1).toString(),
                    DataType.NONE_MOOD,
                    DataType.CURRENT_DAY)
                calendarViewModel.dateList.add(main2CalendarData)
            }
        }
        else {
            for (k in 0 until calendarHelper.getEndDay()) {
                var main2CalendarData : Main2CalendarData? = null
                for (l in 0 until targetAnswerList.size) {

                    if (CalendarHelper.dateTransformationToDay(targetAnswerList[l].createdDate)
                            .toInt() == k) {
                                var moodType : Int = 0
                                when(targetAnswerList[l].question.mood) {
                                    "happy" -> moodType = DataType.HAPPY_MOOD
                                    "sad" -> moodType = DataType.SAD_MOOD
                                    "mad" -> moodType = DataType.MAD_MOOD
                                }
                         main2CalendarData = Main2CalendarData(
                            (k + 1).toString(),
                            moodType,
                            DataType.CURRENT_DAY)
                    }
                }

                if (main2CalendarData == null) {
                    main2CalendarData = Main2CalendarData(
                        (k+1).toString(),
                        DataType.NONE_MOOD,
                        DataType.CURRENT_DAY
                    )
                }
                calendarViewModel.dateList.add(main2CalendarData)
                main2CalendarData = null
            }
        }



        for (m in 1..7 - calendarHelper.getEndDayOfWeek()) {
            calendarViewModel.dateList.add(
                Main2CalendarData(m.toString(), DataType.NONE_MOOD, DataType.LAST_DAY)
            )
        }

        binding.main2CalendarRecyclerView.adapter!!.notifyDataSetChanged()
    }

    public fun plusMonth(view: View) {
        if (calendarHelper.getYear() == AppUtil.getNowYear()
            && calendarHelper.getMonth()+1 == AppUtil.getNowMonth()) {

        } else {
            calendarHelper.plusMonth()
            if (calendarHelper.getYear().toString() != binding.year)
                binding.year = calendarHelper.getYear().toString()

            calendarViewModel.dateList.clear()

            setCalendar()
        }
    }

    public fun minusMonth(view: View) {
        calendarHelper.minusMonth()

        calendarViewModel.dateList.clear()
        setCalendar()
    }


}