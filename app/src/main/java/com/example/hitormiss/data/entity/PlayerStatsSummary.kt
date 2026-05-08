package com.example.hitormiss.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "player_stats_summary")
data class PlayerStatsSummary(

    @PrimaryKey val playerId: String,

    val runs: Int,
    val testRuns: Int,
    val odiRuns: Int,
    val t20iRuns: Int,

    val fours: Int,
    val testFours: Int,
    val odiFours: Int,
    val t20iFours: Int,

    val sixes: Int,
    val testSixes: Int,
    val odiSixes: Int,
    val t20iSixes: Int,

    val strikeRate: Float,
    val testStrikeRate: Float,
    val odiStrikeRate: Float,
    val t20iStrikeRate: Float,

    val average: Float,
    val testAverage: Float,
    val odiAverage: Float,
    val t20iAverage: Float,

    val wickets: Int,
    val testWickets: Int,
    val odiWickets: Int,
    val t20iWickets: Int,

    val economy: Float,
    val testEconomy: Float,
    val odiEconomy: Float,
    val t20iEconomy: Float,

    val matches: Int
)