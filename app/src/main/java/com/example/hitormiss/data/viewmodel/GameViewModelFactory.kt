package com.example.hitormiss.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hitormiss.utils.engine.GameEngine

class GameViewModelFactory(
    private val gameEngine: GameEngine
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            return GameViewModel(gameEngine) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}