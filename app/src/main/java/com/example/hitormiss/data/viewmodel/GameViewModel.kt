package com.example.hitormiss.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hitormiss.utils.engine.GameEngine
import com.example.hitormiss.utils.engine.StatCategory
import kotlinx.coroutines.launch

class GameViewModel(
    private val gameEngine: GameEngine
) : ViewModel() {

    private val _state = MutableLiveData(GameState())
    val state: LiveData<GameState> = _state
    private var currentCategory: StatCategory? = null

    fun loadQuestion(category: StatCategory) {

        viewModelScope.launch {

            _state.value = _state.value?.copy(
                isLoading = true,
                error = null
            )

            try {

                val question = gameEngine.generateQuestion(category)

                _state.value = _state.value?.copy(
                    isLoading = false,
                    question = question
                )

            } catch (e: Exception) {

                _state.value = _state.value?.copy(
                    isLoading = false,
                    error = e.message ?: "Something went wrong"
                )
            }
        }
    }
    fun nextQuestion(category: StatCategory) {
        loadQuestion(category)
    }

    fun updateScore(isCorrect: Boolean) {

        val current = _state.value ?: return

        val newScore = if (isCorrect) {
            current.score + 1
        } else {
            current.score
        }

        _state.value = current.copy(
            score = newScore
        )
    }



    fun startGame(category: StatCategory) {
        currentCategory = category
        loadQuestion(category)
    }

    fun nextQuestion() {
        currentCategory?.let {
            loadQuestion(it)
        }
    }
}