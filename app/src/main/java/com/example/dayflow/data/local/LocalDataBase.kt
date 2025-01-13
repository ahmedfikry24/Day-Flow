package com.example.dayflow.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.dayflow.data.local.dao.BlockAppsDao
import com.example.dayflow.data.local.dao.DailyTaskDao
import com.example.dayflow.data.local.dao.YearlyTaskDao
import com.example.dayflow.data.local.entity.BlockAppInfoEntity
import com.example.dayflow.data.local.entity.DailyTaskEntity
import com.example.dayflow.data.local.entity.YearlyTaskEntity

@Database(
    entities = [DailyTaskEntity::class, YearlyTaskEntity::class, BlockAppInfoEntity::class],
    version = 1
)
@TypeConverters(RoomConverters::class)
abstract class LocalDataBase : RoomDatabase() {

    abstract fun dailyTaskDao(): DailyTaskDao

    abstract fun yearlyTaskDao(): YearlyTaskDao

    abstract fun blockAppsDao(): BlockAppsDao
}
