package com.example.hitormiss.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "player_stats_summary")
data class PlayerStatsSummary(

    @PrimaryKey val playerId: String,

    val runs: Int,
    val sixes: Int,
    val fours: Int,
    val strikeRate: Float,
    val average: Float,

    val wickets: Int,
    val economy: Float,

    val matches: Int
)