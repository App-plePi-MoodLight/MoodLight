package com.example.moodlight.screen.onboarding

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.moodlight.R
import com.example.moodlight.screen.initial.InitialActivity

class OnboardFragment1 : Fragment() {
    var image: Int? = null
    var mainTv: String? = null
    var subTv : String? = null

    private lateinit var maintextView : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            image = it.getInt("image", 0)
            mainTv = it.getString("text", "")
            subTv = it.getString("text1", "")
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_onboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(mainTv.equals("과거의 내 기분은 어땠을까")){
            Log.d(TAG, "onViewCreated: ${view.findViewById<TextView>(R.id.mainTV1).text.toString().equals("과거의 내 기분은 어땠을까")}")
            view.findViewById<Button>(R.id.checkBtn1).visibility = View.VISIBLE
        }
        view.findViewById<ImageView>(R.id.onboarding1IV).setImageResource(image!!)
        view.findViewById<TextView>(R.id.mainTV1).text = mainTv
        view.findViewById<TextView>(R.id.subTv).text = subTv

        view.findViewById<Button>(R.id.checkBtn1).setOnClickListener {
            val preferences = activity?.getSharedPreferences("FirstCheck", AppCompatActivity.MODE_PRIVATE)
            val editor = preferences?.edit()
            editor?.putBoolean("checkFirst", true)
            editor?.apply()
            startActivity(Intent(requireContext(), InitialActivity::class.java))
            activity?.finish()
        }
    }

    companion object{
        fun newInstance(image: Int, text: String, text1:String)=
            OnboardFragment1().apply {
                arguments = Bundle().apply {
                    putInt("image", image)
                    putString("text", text)
                    putString("text1", text1)
                }
            }
    }

}