package com.example.moodlight.util

import android.content.Context
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import android.widget.TextView
import androidx.databinding.BindingAdapter
import kotlin.coroutines.coroutineContext

object QnaBindingAdapter {
    @JvmStatic
    @BindingAdapter("text_edit", "image", "context")
    fun setText(view : TextView, text : String, image : Int, context : Context){
        if(text.indexOf("/")!=-1){
            val textArray = text.split("/")
            val ssb = SpannableStringBuilder()
            ssb.append(textArray[0])
            ssb.setSpan(ImageSpan(context, image)
            ,ssb.length-1, ssb.length, 0)
            ssb.append(textArray[1])
            view.text = ssb
        }
        else{
            view.text = text
        }
    }
}