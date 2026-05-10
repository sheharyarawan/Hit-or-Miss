package com.example.hitormiss.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hitormiss.domain.usecase.GenerateQuestionUseCase
import com.example.hitormiss.utils.engine.GameQuestion
import com.example.hitormiss.utils.engine.StatCategory
import com.example.hitormiss.utils.MatchFormat
import kotlinx.coroutines.launch

class GameViewModel(
    private val generateQuestionUseCase: GenerateQuestionUseCase
) : ViewModel() {

    private val _state = MutableLiveData(GameState())
    val state: LiveData<GameState> = _state

    private var currentCategory: StatCategory? = null
    private var currentFormat: MatchFormat = MatchFormat.TEST

    fun startGame(category: StatCategory, format: MatchFormat) {
        currentCategory = category
        currentFormat = format
        loadQuestion()
    }

    fun loadQuestion() {

        val category = currentCategory ?: return

        viewModelScope.launch {

            _state.value = _state.value?.copy(
                isLoading = true,
                error = null
            )

            try {

                val question = generateQuestionUseCase.execute(category)

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

    fun nextQuestion() {
        loadQuestion()
    }

    fun updateScore(isCorrect: Boolean) {

        val current = _state.value ?: return

        val newScore = if (isCorrect) current.score + 1 else current.score
        val newTotal = current.totalAnswered + 1
        val newCorrect = if (isCorrect) current.correctAnswered + 1 else current.correctAnswered
        val newXp = if (isCorrect) current.xp + 10 else current.xp
        val newWrong = if (isCorrect) current.wrongCount else current.wrongCount + 1

        _state.value = current.copy(
            score = newScore,
            totalAnswered = newTotal,
            correctAnswered = newCorrect,
            xp = newXp,
            wrongCount = newWrong
        )
    }

    fun resetRun() {
        val current = _state.value ?: GameState()
        _state.value = current.copy(
            score = 0,
            totalAnswered = 0,
            correctAnswered = 0,
            xp = 0,
            wrongCount = 0
        )
    }


    fun checkAnswer(question: GameQuestion, selected: String): Boolean {

        val a = question.playerAStats
        val b = question.playerBStats

        val correct = when (question.category) {
            StatCategory.RUNS -> getRuns(a) > getRuns(b)
            StatCategory.SIXES -> getSixes(a) > getSixes(b)
            StatCategory.FOURS -> getFours(a) > getFours(b)
            StatCategory.WICKETS -> getWickets(a) > getWickets(b)
            StatCategory.STRIKE_RATE -> getStrikeRate(a) > getStrikeRate(b)
            StatCategory.ECONOMY -> getEconomy(a) < getEconomy(b)
        }

        return if (selected == "A") correct else !correct
    }

    private fun getRuns(s: com.example.hitormiss.data.entity.PlayerStatsSummary): Int =
        when (currentFormat) {
            MatchFormat.TEST -> s.testRuns
            MatchFormat.ODI -> s.odiRuns
            MatchFormat.T20I -> s.t20iRuns
        }

    private fun getFours(s: com.example.hitormiss.data.entity.PlayerStatsSummary): Int =
        when (currentFormat) {
            MatchFormat.TEST -> s.testFours
            MatchFormat.ODI -> s.odiFours
            MatchFormat.T20I -> s.t20iFours
        }

    private fun getSixes(s: com.example.hitormiss.data.entity.PlayerStatsSummary): Int =
        when (currentFormat) {
            MatchFormat.TEST -> s.testSixes
            MatchFormat.ODI -> s.odiSixes
            MatchFormat.T20I -> s.t20iSixes
        }

    private fun getWickets(s: com.example.hitormiss.data.entity.PlayerStatsSummary): Int =
        when (currentFormat) {
            MatchFormat.TEST -> s.testWickets
            MatchFormat.ODI -> s.odiWickets
            MatchFormat.T20I -> s.t20iWickets
        }

    private fun getStrikeRate(s: com.example.hitormiss.data.entity.PlayerStatsSummary): Float =
        when (currentFormat) {
            MatchFormat.TEST -> s.testStrikeRate
            MatchFormat.ODI -> s.odiStrikeRate
            MatchFormat.T20I -> s.t20iStrikeRate
        }

    private fun getEconomy(s: com.example.hitormiss.data.entity.PlayerStatsSummary): Float =
        when (currentFormat) {
            MatchFormat.TEST -> s.testEconomy
            MatchFormat.ODI -> s.odiEconomy
            MatchFormat.T20I -> s.t20iEconomy
        }
}