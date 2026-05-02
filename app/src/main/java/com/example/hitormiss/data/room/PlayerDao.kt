package com.example.hitormiss.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.hitormiss.data.entity.Player

@Dao
interface PlayerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(players: List<Player>)

    @Query("SELECT * FROM players")
    suspend fun getAllPlayers(): List<Player>

    @Query("SELECT COUNT(*) FROM players")
    suspend fun getPlayerCount(): Int

    @Query("SELECT * FROM players WHERE role IN (:roles)")
    suspend fun getPlayersByRoles(roles: List<String>): List<Player>
}