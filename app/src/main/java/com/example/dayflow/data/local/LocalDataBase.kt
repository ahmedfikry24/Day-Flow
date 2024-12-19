package com.example.dayflow.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.dayflow.data.local.dao.TaskDao
import com.example.dayflow.data.local.entity.TaskEntity

@Database(entities = [TaskEntity::class], version = 1)
abstract class LocalDataBase : RoomDatabase() {

    abstract fun taskDao(): TaskDao
}
