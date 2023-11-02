package com.example.duestake.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.duestake.data.user.UserData

@Dao
interface UserDao {
    @Query("SELECT * from user_table")
    fun getAll(): LiveData<UserData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(userData: UserData)

    @Query("SELECT * FROM user_table WHERE _id = :id")
    fun getUserByID(id: Int): UserData
}