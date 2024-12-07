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
    @ColumnInfo val status: String,
    @ColumnInfo val priority: String,
)
