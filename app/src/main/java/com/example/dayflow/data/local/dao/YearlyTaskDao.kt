package com.example.dayflow.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.dayflow.data.local.entity.YearlyTaskEntity

@Dao
interface YearlyTaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTask(task: YearlyTaskEntity)

    @Query("select * from YearlyTaskEntity")
    suspend fun getAllTasks(): List<YearlyTaskEntity>

    @Query("delete from YearlyTaskEntity where id =:id")
    suspend fun deleteTask(id: Int)
}
