package com.example.hitormiss.utils.engine

import com.example.hitormiss.data.entity.Player
import com.example.hitormiss.data.entity.PlayerStatsSummary
import kotlin.math.abs

class GameEngine {

    fun generateQuestion(
        category: StatCategory,
        players: List<Player>,
        statsMap: Map<String, PlayerStatsSummary>
    ): GameQuestion {

        repeat(50) {

            val playerA = players.random()
            val playerB = players.random()

            val statsA = statsMap[playerA.id]
            val statsB = statsMap[playerB.id]

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

        val playersWithStats = players.filter { statsMap[it.id] != null }
        if (playersWithStats.size < 2) {
            throw Exception("Not enough player data to create a question")
        }

        var playerA = playersWithStats.random()
        var playerB = playersWithStats.random()
        while (playerA.id == playerB.id) {
            playerB = playersWithStats.random()
        }

        val statsA = statsMap[playerA.id]!!
        val statsB = statsMap[playerB.id]!!

        return GameQuestion(
            category = category,
            playerA = playerA,
            playerB = playerB,
            playerAStats = statsA,
            playerBStats = statsB,
            questionText = buildQuestionText(category)
        )
    }

    private fun isValidPair(
        category: StatCategory,
        a: PlayerStatsSummary,
        b: PlayerStatsSummary
    ): Boolean {

        return when (category) {

            StatCategory.RUNS ->
                abs(a.runs - b.runs) <= 120

            StatCategory.SIXES ->
                abs(a.sixes - b.sixes) <= 20

            StatCategory.FOURS ->
                abs(a.fours - b.fours) <= 25

            StatCategory.WICKETS ->
                abs(a.wickets - b.wickets) <= 10

            StatCategory.STRIKE_RATE ->
                abs(a.strikeRate - b.strikeRate) <= 25

            StatCategory.ECONOMY ->
                abs(a.economy - b.economy) <= 2.5f
        }
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