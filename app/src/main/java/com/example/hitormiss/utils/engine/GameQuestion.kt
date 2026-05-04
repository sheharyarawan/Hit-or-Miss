package com.example.hitormiss.utils.engine

import com.example.hitormiss.data.entity.Player
import com.example.hitormiss.data.entity.PlayerStatsSummary

data class GameQuestion(
    val category: StatCategory,
    val playerA: Player,
    val playerB: Player,
    val playerAStats: PlayerStatsSummary,
    val playerBStats: PlayerStatsSummary,
    val questionText: String
)