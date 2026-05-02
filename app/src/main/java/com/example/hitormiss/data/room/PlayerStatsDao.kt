package com.example.hitormiss.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.hitormiss.data.entity.PlayerStatsSummary

@Dao
interface PlayerStatsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(stats: List<PlayerStatsSummary>)

    @Query("SELECT * FROM player_stats_summary")
    suspend fun getAllStats(): List<PlayerStatsSummary>

    @Query("SELECT * FROM player_stats_summary WHERE playerId = :id")
    suspend fun getStatsByPlayer(id: String): PlayerStatsSummary?
}