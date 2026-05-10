package com.example.hitormiss.data.viewmodel
import com.example.hitormiss.utils.engine.GameQuestion
data class GameState(
    val isLoading: Boolean = false,
    val question: GameQuestion? = null,
    val error: String? = null,
    val score: Int = 0,
    val totalAnswered: Int = 0,
    val correctAnswered: Int = 0,
    val xp: Int = 0,
    val wrongCount: Int = 0
)