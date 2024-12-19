package com.example.dayflow.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.dayflow.data.local.dao.DailyTaskDao
import com.example.dayflow.data.local.dao.YearlyTaskDao
import com.example.dayflow.data.local.entity.DailyTaskEntity
import com.example.dayflow.data.local.entity.YearlyTaskEntity

@Database(entities = [DailyTaskEntity::class, YearlyTaskEntity::class], version = 1)
abstract class LocalDataBase : RoomDatabase() {

    abstract fun dailyTaskDao(): DailyTaskDao

    abstract fun yearlyTaskDao(): YearlyTaskDao
}
