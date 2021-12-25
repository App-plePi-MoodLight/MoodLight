package com.example.moodlight.screen.main3.qna

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import com.example.moodlight.R
import com.example.moodlight.databinding.ActivityHelpAcitvityBinding
import com.example.moodlight.model.qna.QnAModel
import com.example.moodlight.model.qna.QnAModelItem
import org.json.JSONArray

class HelpAcitvity : AppCompatActivity() {
    private lateinit var binding : ActivityHelpAcitvityBinding
    private var qnaList : QnAModel = QnAModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help_acitvity)
        getData()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_help_acitvity)
        binding.recycler.adapter = QnaAdapter(qnaList)

        setSupportActionBar(binding.main2Toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.left_btn)
    }

    private fun getData() {
        var jsonArray = JSONArray(
            assets.open("qna.json").reader().readText()
        )

        for(i in 0 until jsonArray.length()){
            val jsonObject = jsonArray.getJSONObject(i)
            when(i){
                0->qnaList.add(QnAModelItem(jsonObject.getString("answer"), jsonObject.getBoolean("isExpand"),jsonObject.getString("question"), R.drawable.ic_group))
                1->qnaList.add(QnAModelItem(jsonObject.getString("answer"), jsonObject.getBoolean("isExpand"),jsonObject.getString("question"), R.drawable.ic_group))
                else->{
                    qnaList.add(QnAModelItem(jsonObject.getString("answer"), jsonObject.getBoolean("isExpand"),jsonObject.getString("question"), R.drawable.img_danger))
                }
            }
        }
        Log.d(TAG, "getData: qna : ${qnaList}")
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}