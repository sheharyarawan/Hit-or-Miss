package com.example.hitormiss.domain.usecase

import com.example.hitormiss.data.repository.PlayerRepository
import com.example.hitormiss.utils.engine.GameEngine
import com.example.hitormiss.utils.engine.GameQuestion
import com.example.hitormiss.utils.engine.StatCategory
import com.example.hitormiss.utils.getPlayers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext

class GenerateQuestionUseCase(
    private val repository: PlayerRepository
) {

    private val engine = GameEngine()

    suspend fun execute(category: StatCategory): GameQuestion {

        withContext(Dispatchers.IO + NonCancellable) {
            repository.syncPlayers(getPlayers())
        }

        val players = getEligiblePlayers(category)

        val statsMap = repository.getAllStats()
            .associateBy { it.playerId }

        return engine.generateQuestion(
            category,
            players,
            statsMap
        )
    }

    private suspend fun getEligiblePlayers(category: StatCategory) =
        when (category) {

            StatCategory.RUNS,
            StatCategory.SIXES,
            StatCategory.FOURS,
            StatCategory.STRIKE_RATE -> {
                repository.getBatsmen() +
                        repository.getAllRounders() +
                        repository.getWicketKeepers()
            }

            StatCategory.WICKETS,
            StatCategory.ECONOMY -> {
                repository.getBowlers() +
                        repository.getAllRounders()
            }
        }
}