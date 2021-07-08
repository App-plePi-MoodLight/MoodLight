package com.example.moodlight.database

import androidx.room.*

@Dao
interface UserDao {
    @Insert
    fun insert (userData: UserData)

    @Update
    fun update (userData: UserData)

    @Delete
    fun delete (userData: UserData)

    @Update
    fun updateLoginTable(userData: UserData)

    @Query("SELECT id FROM userLoginTable ")
    fun getIdFromUserLoginTable() : String?

    @Query("SELECT * FROM userLoginTable")
    fun getuserLoginTable() : List<UserData>?

}