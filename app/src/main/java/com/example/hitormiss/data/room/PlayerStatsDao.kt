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

    @Query("SELECT COUNT(*) FROM player_stats_summary")
    suspend fun getStatsCount(): Int

    @Query(
        "SELECT COUNT(*) FROM player_stats_summary " +
            "WHERE runs > 0 OR sixes > 0 OR fours > 0 OR wickets > 0 OR matches > 0"
    )
    suspend fun getNonZeroStatsCount(): Int
}