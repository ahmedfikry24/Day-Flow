package com.example.dayflow.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.dayflow.data.local.entity.BlockAppInfoEntity

@Dao
interface BlockAppsDao {

    @Query("select * from BlockAppInfoEntity")
    suspend fun getAllBlockedApps(): List<BlockAppInfoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBlockedApp(info: BlockAppInfoEntity)

    @Query("delete from BlockAppInfoEntity where id =:id")
    suspend fun removeBlockedApp(id: Int)
}