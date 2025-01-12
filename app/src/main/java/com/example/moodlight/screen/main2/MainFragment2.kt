package com.example.moodlight.screen.main2

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.moodlight.R
import com.example.moodlight.api.ServerClient
import com.example.moodlight.databinding.FragmentMain2Binding
import com.example.moodlight.model.my_answer.MyAnswerModel
import com.example.moodlight.model.my_answer.MyAnswerModelItem
import com.example.moodlight.model.myanswermodel.MyAnswerListModel
import com.example.moodlight.model.myanswermodel.MyAnswerListModelItem
import com.example.moodlight.screen.main2.calendar.CalendarHelper
import com.example.moodlight.screen.main2.calendar.Main2CalendarAdapter
import com.example.moodlight.screen.main2.calendar.Main2CalendarData
import com.example.moodlight.screen.main2.calendar.Main2CalendarViewModel
import com.example.moodlight.screen.main2.diaryRecyclerview.data.DateAdapter
import com.example.moodlight.screen.main2.diaryRecyclerview.data.DateClass
import com.example.moodlight.util.AppUtil
import com.example.moodlight.util.DataType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat

class MainFragment2 : Fragment() {

    private val calendarHelper by lazy { CalendarHelper() }

    private lateinit var myAnswerList : MyAnswerModel

    private val viewModel: Main2ViewModel by lazy {
        ViewModelProvider(requireActivity()).get(Main2ViewModel::class.java)
    }

    private val calendarViewModel: Main2CalendarViewModel by lazy {
        ViewModelProvider(requireActivity()).get(Main2CalendarViewModel::class.java)
    }
    private lateinit var binding: FragmentMain2Binding
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



        setAdapter()

        val adapter: Main2CalendarAdapter = Main2CalendarAdapter(calendarViewModel)
        binding.main2CalendarRecyclerView.adapter = adapter
        binding.main2CalendarRecyclerView.adapter!!.notifyDataSetChanged()

        ServerClient.getApiService().getMyAnswerAll()
            .enqueue(object : Callback<MyAnswerModel> {

                override fun onResponse(
                    call: Call<MyAnswerModel>,
                    response: Response<MyAnswerModel>
                ) {
                    if (response.isSuccessful) {
                        if (response.body() == null) {
                            myAnswerList = MyAnswerModel()
                        }
                        else {
                            myAnswerList = response.body()!!
                        }
                        setUi()
                    }
                    else {
                        Log.e("cc",response.code().toString())
                    }
                }

                override fun onFailure(call: Call<MyAnswerModel>, t: Throwable) {
                    t.printStackTrace()
                }

            })


        if(viewModel.list.value!!.isEmpty()){
            dataLoding()
        }
        else{
            setAdapter()
        }



        return binding.root
    }

    private fun dataLoding() {
        CoroutineScope(Dispatchers.IO).launch { 
            ServerClient.getApiService().getMyAllAnswer()
                .enqueue(object : Callback<MyAnswerListModel> {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onResponse(
                        call: Call<MyAnswerListModel>,
                        response: Response<MyAnswerListModel>
                    ) {
                        val result = response.body()
                        Log.d(TAG, "onResponse: $response")
                        if(response.code() == 200){
                            Log.d(TAG, "onResponse: data : $result")
                            if(result!!.size != 0){
                                if(viewModel.list.value!!.isNotEmpty()){
                                    viewModel.list.value!!.removeAt(viewModel.list.value!!.lastIndex)
                                }
                                processingData(result)
                                requireActivity().runOnUiThread {
                                    binding.recycler.adapter!!.notifyDataSetChanged()
                                }
                            }
                        }
                        else{
                            Toast.makeText(requireContext(), "내 답변 리스트를 불러오는데에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<MyAnswerListModel>, t: Throwable) {
                        Toast.makeText(requireContext(), "내 답변 리스트를 불러오는데에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                    }

                })
        }
    }

    private fun setAdapter() {
        binding.recycler.adapter = DateAdapter(requireContext(),
            viewModel.list.value!!
        ){data ->
            startActivity(Intent(requireActivity(), DetailAnswerActivity::class.java)
                .putExtra("data", data))
        }
        binding.recycler.setHasFixedSize(true)
    }

    private fun processingData(result: MyAnswerListModel?){

        for( i in 0 until result!!.size){
            result[i].createdDate = changeCreateDate(result[i].question.activatedDate)
            Log.d(TAG, "processingData: date : ${result[i].createdDate}")
        }

        var i = 0
        while(result.size >i){
            val data: ArrayList<MyAnswerListModelItem> = ArrayList()
            var plusI = 0
            for(j in i until result.size){
                if(result[j].createdDate == result[i].createdDate){
                    data.add(result[j])
                    plusI++
                    if(j == result.size-1){
                        viewModel.list.value!!.add(DateClass(result[i].createdDate,data))
                    }
                }
                else{
                    viewModel.list.value!!.add(DateClass(result[i].createdDate,data))
                    break
                }
            }
            i += plusI
        }
        Log.d(TAG, "processingData: list : ${viewModel.list.value}")
    }

    @SuppressLint("SimpleDateFormat")
    private fun changeCreateDate(createdDate: String) : String {
        var createSf = SimpleDateFormat("yyyy-MM-dd")
        var changeSf = SimpleDateFormat("MM'월' dd일")
        var date = createSf.parse(createdDate)
        
        return changeSf.format(date)
    }

    private fun setUi() {

        val adapter: Main2CalendarAdapter = Main2CalendarAdapter(calendarViewModel)
        binding.main2CalendarRecyclerView.adapter = adapter
        calendarViewModel.today = calendarHelper.getDate()
        binding.year = calendarHelper.getYear().toString()

        setCalendar()
    }

    private fun setCalendar() {

        calendarViewModel.dateList.clear()

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
                lastEndDay = if (AppUtil.isLeapYear(calendarHelper.getYear())) {
                    29
                } else
                    28
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
            // 만약 사용자가 작성한 게시글이 없을 시
            for (k in 0 until calendarHelper.getEndDay()) {
                val main2CalendarData : Main2CalendarData = Main2CalendarData(
                    (k+1).toString(),
                    DataType.NONE_MOOD,
                    DataType.CURRENT_DAY)
                calendarViewModel.dateList.add(main2CalendarData)
            }
        }
        else {

            // 사용자가 작성한 게시글이 존재할 시
            for (k in 1 .. calendarHelper.getEndDay()) {
                var main2CalendarData : Main2CalendarData? = null

                for (l in 0 until targetAnswerList.size) {

/*                    try {*/
                        if (CalendarHelper.dateTransformationToDay(targetAnswerList[l].createdDate)
                                .toInt() == k
                        ) {
                            Log.e("zbs",targetAnswerList[l].createdDate)
                            var moodType: Int = 0
                            try {
                                when (targetAnswerList[l].question.mood) {
                                    "happy" -> moodType = DataType.HAPPY_MOOD
                                    "sad" -> moodType = DataType.SAD_MOOD
                                    "mad" -> moodType = DataType.MAD_MOOD
                                }

                                main2CalendarData = Main2CalendarData(
                                    (k).toString(),
                                    moodType,
                                    DataType.CURRENT_DAY)



                            } catch (e : NullPointerException) {
                                main2CalendarData = Main2CalendarData(
                                    (k).toString(),
                                    DataType.NONE_MOOD,
                                    DataType.CURRENT_DAY)

                            }

                        }

                }

                if (main2CalendarData == null) {
                    main2CalendarData = Main2CalendarData(
                        (k).toString(),
                        DataType.NONE_MOOD,
                        DataType.CURRENT_DAY
                    )
                }
                calendarViewModel.dateList.add(main2CalendarData)
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

        binding.year = calendarHelper.getYear().toString()
    }

    public fun minusMonth(view: View) {
        calendarHelper.minusMonth()

        calendarViewModel.dateList.clear()
        setCalendar()
        binding.year = calendarHelper.getYear().toString()
    }

    override fun onStop() {
        super.onStop()
        calendarViewModel.dateList.clear()
        AppUtil.setBaseCalendarList(calendarViewModel)
    }
}