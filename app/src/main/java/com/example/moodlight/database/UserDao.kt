    package com.example.moodlight.database

    import androidx.room.*

    @Dao
    interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert (userData: UserData)

    @Query("DELETE from UserData")
    fun deleteUserLoginTable()

    @Query("SELECT id FROM UserData ")
    fun getId() : String

    @Query("SELECT password FROM UserData")
    fun getPassword() : String

    @Query("SELECT * FROM UserData")
    fun getUserFromUserLoginTable() : List<UserData>

    }
