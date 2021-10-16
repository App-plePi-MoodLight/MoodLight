package com.example.moodlight.database

import androidx.room.*

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert (userData: UserData)

    @Update
    fun update (userData: UserData)

    @Delete
    fun delete (userData: UserData)

    @Update
    fun updateLoginTable(userData: UserData)

    @Query("DELETE from UserData")
    fun deleteUserLoginTable()

    @Query("SELECT id FROM UserData ")
    fun getIdFromUserLoginTable() : String

    @Query("SELECT * FROM UserData")
    fun getUserFromUserLoginTable() : List<UserData>

}