package com.example.hitormiss.utils.engine

import com.example.hitormiss.data.entity.Player
import com.example.hitormiss.data.entity.PlayerStatsSummary
import com.example.hitormiss.data.repository.PlayerRepository
import com.example.hitormiss.utils.getPlayers

class GameEngine(
    private val repository: PlayerRepository
) {
    suspend fun generateQuestion(category: StatCategory): GameQuestion {

        val players = getEligiblePlayers(category)

        var playerA: Player
        var playerB: Player

        var statsA: PlayerStatsSummary?
        var statsB: PlayerStatsSummary?
        repeat(50) {

            val playerA = players.random()
            val playerB = players.random()

            val statsA = repository.getPlayerStats(playerA.id)
            val statsB = repository.getPlayerStats(playerB.id)

            if (
                playerA.id != playerB.id &&
                statsA != null &&
                statsB != null &&
                isValidPair(category, statsA, statsB)
            ) {
                return GameQuestion(
                    category = category,
                    playerA = playerA,
                    playerB = playerB,
                    playerAStats = statsA,
                    playerBStats = statsB,
                    questionText = buildQuestionText(category)
                )
            }
        }

        throw Exception("Unable to generate valid question")

        return GameQuestion(
            category = category,
            playerA = playerA,
            playerB = playerB,
            playerAStats = statsA,
            playerBStats = statsB,
            questionText = buildQuestionText(category)
        )
    }

    private suspend fun getEligiblePlayers(category: StatCategory): List<Player> {

        val batsmen = repository.getBatsmen()
        val bowlers = repository.getBowlers()
        val wk = repository.getWicketKeepers()
        val allRounders = repository.getAllRounders()

        return when (category) {

            StatCategory.RUNS,
            StatCategory.SIXES,
            StatCategory.FOURS,
            StatCategory.STRIKE_RATE -> {
                batsmen + allRounders + wk
            }

            StatCategory.WICKETS,
            StatCategory.ECONOMY -> {
                bowlers + allRounders
            }
        }
    }

    private fun isValidPair(
        category: StatCategory,
        a: PlayerStatsSummary,
        b: PlayerStatsSummary
    ): Boolean {

        return when (category) {

            StatCategory.RUNS ->
                kotlin.math.abs(a.runs - b.runs) <= 120

            StatCategory.SIXES ->
                kotlin.math.abs(a.sixes - b.sixes) <= 20

            StatCategory.FOURS ->
                kotlin.math.abs(a.fours - b.fours) <= 25

            StatCategory.WICKETS ->
                kotlin.math.abs(a.wickets - b.wickets) <= 10

            StatCategory.STRIKE_RATE ->
                kotlin.math.abs(a.strikeRate - b.strikeRate) <= 25

            StatCategory.ECONOMY ->
                kotlin.math.abs(a.economy - b.economy) <= 2.5f
        }
    }
    suspend fun syncData() {
        repository.syncPlayers(getPlayers())
    }

    private fun buildQuestionText(category: StatCategory): String {

        return when (category) {

            StatCategory.RUNS ->
                "Who has scored more international runs?"

            StatCategory.SIXES ->
                "Who has hit more sixes in his career?"

            StatCategory.FOURS ->
                "Who has hit more fours in his career?"

            StatCategory.WICKETS ->
                "Who has taken more wickets?"

            StatCategory.STRIKE_RATE ->
                "Who has better batting strike rate?"

            StatCategory.ECONOMY ->
                "Who has better bowling economy?"
        }
    }
}