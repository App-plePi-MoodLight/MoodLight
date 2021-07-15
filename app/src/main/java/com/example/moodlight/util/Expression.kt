package com.example.moodlight.util

import android.util.Patterns
import java.util.regex.Pattern

class Expression {
    companion object {

        fun isValidEmail (text : String? ) : Boolean {
            val pattern = Patterns.EMAIL_ADDRESS
            return pattern.matcher(text).matches()
        }

        fun isValidPw(text: String?): Boolean {
            val pattern = Pattern.compile("(^.*(?=.{6,24})(?=.*[0-9])(?=.*[A-z]).*$)")
            return pattern.matcher(text).matches()
        }
    }
}