package com.example.hitormiss.utils.engine

import com.example.hitormiss.data.entity.Player

data class GameQuestion(
    val category: StatCategory,
    val playerA: Player,
    val playerB: Player,
    val questionText: String
)