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
    private var writePostMap: Map<String, *>? = null

    private lateinit var myAnswerList : MyAnswerModel

    private val viewModel: Main2ViewModel by lazy {
        ViewModelProvider(requireActivity()).get(Main2ViewModel::class.java)
    }

    private val calendarViewModel: Main2CalendarViewModel by lazy {
        ViewModelProvider(requireActivity()).get(Main2CalendarViewModel::class.java)
    }

    private var isLoding : Boolean = false
    private var limitPage : Int = 1
    private var startPage : Int = 0

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



//        binding.recycler.addOnScrollListener(object :  RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//
//
//                if(!isLoding){
//                    if((recyclerView.layoutManager as LinearLayoutManager?)!!.findLastVisibleItemPosition() == list.size-1){
//                        limitPage+=3
//                        startPage+=3
//                        dataLoding()
//                        this@MainFragment2.isLoding = true
//                    }
//                }
//            }
//        })
      
/*        CoroutineScope(Dispatchers.IO).launch {
            FirebaseUtil.getFireStoreInstance().collection("users")
                .document(FirebaseUtil.getUid())
                .get()
                .addOnCompleteListener {
                    writePostMap = it.result!!.get("writePostMap") as Map<String, *>?
                    setUi()
                }
        }*/


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
                        myAnswerList = response.body()!!
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


        if(list.isEmpty()){
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
                                if(list.isNotEmpty()){
                                    list.removeAt(list.lastIndex)
                                    processingData(result)
                                    requireActivity().runOnUiThread {
                                        setAdapter()
                                    }
                                }
                                else{
                                    processingData(result)
                                    requireActivity().runOnUiThread {
                                        setAdapter()
                                    }
                                }
                            }
                            else{
                                list.removeAt(list.lastIndex)
                                binding.recycler.adapter!!.notifyDataSetChanged()
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
//        val data: ArrayList<QnAData> = ArrayList()
//        if(list.isEmpty()){
//            data.add(QnAData("오늘 점심은 뭐 먹죠?", "점심을 먹죠 ㅎㅎ 아 점심을 먹는다니 참 감미로운 일이네요."))
//            data.add(QnAData("오늘 저녁은 뭐 먹죠?", "저녁을 먹죠 ㅎㅎ"))
//            list.add(DateClass("3월 16일", data))
//            list.add(DateClass("4월 16일", data))
//            Log.d(TAG, "onActivityCreated: 내 리스트 data$data $list")
//        }
    }

    private fun setAdapter() {
        binding.recycler.adapter = DateAdapter(requireContext(), list){data ->
            startActivity(Intent(requireActivity(), DetailAnswerActivity::class.java)
                .putExtra("data", data))
        }
        binding.recycler.setHasFixedSize(true)
    }

    private fun processingData(result: MyAnswerListModel?){

        for( i in 0 until result!!.size){
            result[i].createdDate = changeCreateDate(result[i].createdDate)
            Log.d(TAG, "processingData: date : ${result[i].createdDate}")
        }

        var i = 0
        while(result.size-1 >i){
            val data: ArrayList<MyAnswerListModelItem> = ArrayList()
            data.add(result[i])
            var plusI = 0
            for(j in i+1 until result.size){
                if(result[j].createdDate == result[i].createdDate){
                    data.add(result[j])
                    plusI++
                    if(j == result.size-1){
                        list.add(DateClass(result[i].createdDate,data))
                    }
                }
                else{
                    list.add(DateClass(result[i].createdDate,data))
                    break
                }
            }
            i += plusI+1
        }
        Log.d(TAG, "processingData: list : ${list}")
        val progressList : ArrayList<MyAnswerListModelItem> = ArrayList()
        //progressList.add(MyAnswerListModelItem(" ", " ", 21, false, Question(true," "," ", " ", " ")))
        //list.add(DateClass(" ", progressList))
    }

    @SuppressLint("SimpleDateFormat")
    private fun changeCreateDate(createdDate: String) : String {
        var createSf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.ss'Z'")
        var changeSf = SimpleDateFormat("MM'월' dd일")
        var date = createSf.parse(createdDate)
        
        return changeSf.format(date)
    }

    private fun setUi() {

        calendarViewModel.today = calendarHelper.getDate()

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
            for (k in 0 until calendarHelper.getEndDay()) {
                var main2CalendarData : Main2CalendarData? = null
                for (l in 0 until targetAnswerList.size) {
                    Log.e("vvvvvvvvvvvvvvv",CalendarHelper.dateTransformationToDay(targetAnswerList[l].createdDate))
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
                                    (k + 1).toString(),
                                    moodType,
                                    DataType.CURRENT_DAY
                                )
                            } catch (e : NullPointerException) {
                                main2CalendarData = Main2CalendarData(
                                    (k + 1).toString(),
                                    DataType.NONE_MOOD,
                                    DataType.CURRENT_DAY
                                )
                            }
                        }
/*                    } catch (e : NullPointerException) {
                        // 사용자가 작성한 게시글이 존재하지만 이번달에 작성한 게시물이 없을시
                        main2CalendarData = Main2CalendarData(
                        (k+1).toString(),
                        DataType.NONE_MOOD,
                        DataType.CURRENT_DAY)
                    }*/

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

    override fun onStop() {
        super.onStop()
        calendarViewModel.dateList.clear()
        AppUtil.setBaseCalendarList(calendarViewModel)
    }
}