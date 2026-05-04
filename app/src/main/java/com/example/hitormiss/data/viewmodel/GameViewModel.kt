package com.example.hitormiss.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hitormiss.utils.engine.GameEngine
import com.example.hitormiss.utils.engine.GameQuestion
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

        viewModelScope.launch {
            try {
                gameEngine.syncData()   // 👈 we’ll add this
                loadQuestion(category)
            } catch (e: Exception) {
                _state.value = _state.value?.copy(
                    error = e.message
                )
            }
        }
    }

    fun nextQuestion() {
        currentCategory?.let {
            loadQuestion(it)
        }
    }

    fun checkAnswer(question: GameQuestion, selected: String): Boolean {

        val a = question.playerAStats
        val b = question.playerBStats

        val correct = when (question.category) {

            StatCategory.RUNS -> a.runs > b.runs
            StatCategory.SIXES -> a.sixes > b.sixes
            StatCategory.FOURS -> a.fours > b.fours
            StatCategory.WICKETS -> a.wickets > b.wickets
            StatCategory.STRIKE_RATE -> a.strikeRate > b.strikeRate
            StatCategory.ECONOMY -> a.economy < b.economy
        }

        return if (selected == "A") correct else !correct
    }
}