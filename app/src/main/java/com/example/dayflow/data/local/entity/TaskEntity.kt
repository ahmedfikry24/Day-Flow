package com.example.dayflow.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo val title: String,
    @ColumnInfo val description: String,
    @ColumnInfo val date: Long? = null,
    @ColumnInfo val time: Long? = null,
    @ColumnInfo val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo val status: Boolean,
)
