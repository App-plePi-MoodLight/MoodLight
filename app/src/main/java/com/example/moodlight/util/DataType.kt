package com.example.moodlight.util

class DataType {
    companion object {
        // day type
        const val LAST_DAY : Int = 1001
        const val CURRENT_DAY : Int = 1002

        // moodType type
        const val NONE_MOOD : Int = 100
        const val HAPPY_MOOD : Int = 101
        const val SAD_MOOD : Int = 102
        const val MAD_MOOD : Int = 103
        var MOOD = NONE_MOOD

        const val NORMAL_VIEW_TYPE = 1
        const val LOADING_VIEW_TYPE = 0
    }
}