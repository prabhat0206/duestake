package com.example.duestake.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.duestake.data.user.UserData

@Database(entities = [UserData::class], version = 1, exportSchema = false)
abstract class MyDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}