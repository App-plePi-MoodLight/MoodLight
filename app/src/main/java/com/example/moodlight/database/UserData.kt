package com.example.moodlight.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
class UserData {

    @PrimaryKey(autoGenerate = true)
    public var loginID : Int = 0

    @ColumnInfo(name = "id")
    public var id : String

    @ColumnInfo(name = "password")
    public var password : String

    var likeAlarm : Boolean = true
    var commentAlarm : Boolean = true

    constructor(id : String, password : String) {
        this.id = id
        this.password = password
    }

    @Ignore
    constructor(loginID : Int,id : String, password : String, postLikeAlarm : Boolean, commentAlarm : Boolean) {
        this.loginID = loginID
        this.id = id
        this.password = password
        this.likeAlarm = postLikeAlarm
        this.commentAlarm = commentAlarm
    }


}