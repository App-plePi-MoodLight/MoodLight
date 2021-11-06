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

    //@Query("DELETE from userLoginTable")

    @Query("DELETE from UserData")
    fun deleteUserLoginTable()

    @Query("SELECT id FROM userLoginTable ")
    fun getId() : String

    @Query("SELECT password FROM userLoginTable")
    fun getPassword() : String

    @Query("SELECT * FROM userLoginTable")
    fun getUserFromUserLoginTable() : List<UserData>

    }
