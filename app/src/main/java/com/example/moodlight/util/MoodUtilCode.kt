package com.example.moodlight.util

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.example.moodlight.R

class MoodUtilCode {
    companion object {
        fun moodTypeToString(moodType: Int): String {
            return when (moodType) {
                DataType.SAD_MOOD -> "sad"

                DataType.MAD_MOOD -> "angry"

                DataType.HAPPY_MOOD -> "happy"

                else -> "happy"
            }
        }

        fun setButtonMood(context: Context): Drawable? {
            return when (DataType.MOOD) {
                DataType.HAPPY_MOOD -> ContextCompat.getDrawable(
                    context,
                    R.drawable.btn_happy_background
                )

                DataType.MAD_MOOD -> ContextCompat.getDrawable(
                    context,
                    R.drawable.btn_mad_background
                )

                DataType.SAD_MOOD -> ContextCompat.getDrawable(
                    context,
                    R.drawable.btn_sad_background
                )

                else -> ContextCompat.getDrawable(
                    context, R.drawable.btn_happy_background
                )
            }
        }
        fun setLevelText(): String {
            return when (DataType.MOOD){
                DataType.HAPPY_MOOD -> "오늘 나의 행복의 정도는?"
                DataType.SAD_MOOD -> "오늘 나의 슬픔의 정도는?"
                DataType.HAPPY_MOOD -> "오늘 나의 분노의 정도는?"
                else -> "오늘 나의 행복의 정도는?"
            }
        }

    }
}