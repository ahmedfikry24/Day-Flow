package com.example.dayflow.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.dayflow.data.local.entity.DailyTaskEntity

@Dao
interface DailyTaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTask(task: DailyTaskEntity)

    @Query("select * from DailyTaskEntity")
    suspend fun getAllTasks(): List<DailyTaskEntity>

    @Query("delete from DailyTaskEntity where id =:id")
    suspend fun deleteTask(id: Int)

    @Query("UPDATE DailyTaskEntity SET status = :status WHERE id = :id")
    suspend fun updateTaskStatus(id: Int, status: Boolean)
}
