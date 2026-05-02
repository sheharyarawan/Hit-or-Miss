package com.example.hitormiss.data.viewmodel
import com.example.hitormiss.utils.engine.GameQuestion
data class GameState(
    val isLoading: Boolean = false,
    val question: GameQuestion? = null,
    val error: String? = null,
    val score: Int = 0
)