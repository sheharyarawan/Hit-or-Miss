package com.example.hitormiss.data.repository

import android.util.Log
import com.example.hitormiss.BuildConfig
import com.example.hitormiss.data.api.PlayerApi
import com.example.hitormiss.data.entity.Player
import com.example.hitormiss.data.entity.PlayerStatsSummary
import com.example.hitormiss.data.model.PlayerSeed
import com.example.hitormiss.data.room.PlayerDao
import com.example.hitormiss.data.room.PlayerStatsDao
import com.example.hitormiss.utils.InternationalStats
class PlayerRepository(
    private val api: PlayerApi,
    private val playerDao: PlayerDao,
    private val statsDao: PlayerStatsDao
) {

    suspend fun syncPlayers(seedList: List<PlayerSeed>) {

        val hasPlayers = playerDao.getPlayerCount() > 0
        val hasNonZeroStats = statsDao.getNonZeroStatsCount() > 0
        if (hasPlayers && hasNonZeroStats) return

        val playersToInsert = mutableListOf<Player>()
        val statsToInsert = mutableListOf<PlayerStatsSummary>()

        for (seed in seedList) {
            Log.d("SYNC", "Seed size = ${seedList.size}")

            try {
                val response = api.getPlayerInfo(
                    BuildConfig.CRICKET_API_KEY,
                    seed.id
                )
                Log.d("SYNC", "Fetching: ${seed.id}")

                val player = Player(
                    id = response.data.id,
                    name = response.data.name,
                    role = response.data.role,
                    country = response.data.country,
                    battingStyle = response.data.battingStyle,
                    bowlingStyle = response.data.bowlingStyle,
                    imageUrl = response.data.playerImg
                )
                Log.d("SYNC", "Players to insert = ${playersToInsert.size}")
                playersToInsert.add(player)
                Log.d("SYNC", "INSERT DONE")




                val summary = convertToSummary(response.data.stats, response.data.id)
                statsToInsert.add(summary)

            } catch (e: Exception) {
                Log.e("SYNC_ERROR", "Player fetch failed for ${seed.id}", e)
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

        val batting = InternationalStats.getInternationalBatting(stats)
        val bowling = InternationalStats.getInternationalBowling(stats)

        return PlayerStatsSummary(
            playerId = playerId,
            runs = batting.runsByFormat.total,
            testRuns = batting.runsByFormat.test,
            odiRuns = batting.runsByFormat.odi,
            t20iRuns = batting.runsByFormat.t20i,
            fours = batting.foursByFormat.total,
            testFours = batting.foursByFormat.test,
            odiFours = batting.foursByFormat.odi,
            t20iFours = batting.foursByFormat.t20i,
            sixes = batting.sixesByFormat.total,
            testSixes = batting.sixesByFormat.test,
            odiSixes = batting.sixesByFormat.odi,
            t20iSixes = batting.sixesByFormat.t20i,
            strikeRate = batting.strikeRate,
            testStrikeRate = batting.strikeRateByFormat.test,
            odiStrikeRate = batting.strikeRateByFormat.odi,
            t20iStrikeRate = batting.strikeRateByFormat.t20i,
            average = batting.average,
            testAverage = batting.averageByFormat.test,
            odiAverage = batting.averageByFormat.odi,
            t20iAverage = batting.averageByFormat.t20i,
            wickets = bowling.wicketsByFormat.total,
            testWickets = bowling.wicketsByFormat.test,
            odiWickets = bowling.wicketsByFormat.odi,
            t20iWickets = bowling.wicketsByFormat.t20i,
            economy = bowling.economy,
            testEconomy = bowling.economyByFormat.test,
            odiEconomy = bowling.economyByFormat.odi,
            t20iEconomy = bowling.economyByFormat.t20i,
            matches = batting.matchesByFormat.total
        )
    }

}