package com.example.hitormiss.data.repository

import com.example.hitormiss.data.api.PlayerApi
import com.example.hitormiss.data.entity.Player
import com.example.hitormiss.data.entity.PlayerStatsSummary
import com.example.hitormiss.data.model.PlayerSeed
import com.example.hitormiss.data.room.PlayerDao
import com.example.hitormiss.data.room.PlayerStatsDao
class PlayerRepository(
    private val api: PlayerApi,
    private val playerDao: PlayerDao,
    private val statsDao: PlayerStatsDao
) {

    suspend fun syncPlayers(seedList: List<PlayerSeed>) {

        if (playerDao.getPlayerCount() > 0) return

        val playersToInsert = mutableListOf<Player>()
        val statsToInsert = mutableListOf<PlayerStatsSummary>()

        for (seed in seedList) {

            try {
                val response = api.getPlayer(seed.id)

                val player = Player(
                    id = response.id,
                    name = response.name,
                    role = response.role,
                    country = response.country,
                    battingStyle = response.battingStyle,
                    bowlingStyle = response.bowlingStyle,
                    imageUrl = response.playerImg
                )

                playersToInsert.add(player)

                val summary = convertToSummary(response.stats, response.id)
                statsToInsert.add(summary)

            } catch (e: Exception) {
                continue
            }
        }

        playerDao.insertAll(playersToInsert)
        statsDao.insertAll(statsToInsert)
    }

    suspend fun getBatsmen(): List<Player> {
        return playerDao.getPlayersByRoles(listOf("Batsman")).shuffled()
    }

    suspend fun getBowlers(): List<Player> {
        return playerDao.getPlayersByRoles(listOf("Bowler")).shuffled()
    }

    suspend fun getWicketKeepers(): List<Player> {
        return playerDao.getPlayersByRoles(listOf("WicketKeeper")).shuffled()
    }

    suspend fun getAllRounders(): List<Player> {
        return playerDao.getPlayersByRoles(listOf("Allrounder")).shuffled()
    }

    suspend fun getPlayerStats(playerId: String): PlayerStatsSummary? {
        return statsDao.getStatsByPlayer(playerId)
    }

    suspend fun getAllStats(): List<PlayerStatsSummary> {
        return statsDao.getAllStats()
    }

    private fun convertToSummary(
        stats: List<com.example.hitormiss.data.model.Stat>,
        playerId: String
    ): PlayerStatsSummary {

        var runs = 0
        var sixes = 0
        var fours = 0
        var avg = 0f
        var sr = 0f
        var wickets = 0
        var econ = 0f
        var matches = 0

        stats.forEach { stat ->

            when (stat.stat.lowercase().trim()) {

                "runs" -> runs = stat.value.toIntOrNull() ?: 0
                "6s" -> sixes = stat.value.toIntOrNull() ?: 0
                "4s" -> fours = stat.value.toIntOrNull() ?: 0
                "avg" -> avg = stat.value.toFloatOrNull() ?: 0f
                "sr" -> sr = stat.value.toFloatOrNull() ?: 0f
                "wkts" -> wickets = stat.value.toIntOrNull() ?: 0
                "econ" -> econ = stat.value.toFloatOrNull() ?: 0f
                "m" -> matches = stat.value.toIntOrNull() ?: 0
            }
        }

        return PlayerStatsSummary(
            playerId = playerId,
            runs = runs,
            sixes = sixes,
            fours = fours,
            strikeRate = sr,
            average = avg,
            wickets = wickets,
            economy = econ,
            matches = matches
        )
    }
}