package com.example.dayflow.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.dayflow.data.local.entity.TaskEntity

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTask(task: TaskEntity)

    @Query("select * from TaskEntity")
    suspend fun getAllTasks(): List<TaskEntity>

    @Query("delete from taskentity where id =:id")
    suspend fun deleteTask(id: Int)

    @Query("UPDATE TaskEntity SET status = :status WHERE id = :id")
    suspend fun updateTaskStatus(id: Int, status: Boolean)
}
