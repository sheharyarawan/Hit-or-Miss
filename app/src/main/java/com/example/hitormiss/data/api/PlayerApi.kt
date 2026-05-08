package com.example.hitormiss.data.api

import com.example.hitormiss.data.model.PlayerInfo
import com.example.hitormiss.data.model.PlayerResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PlayerApi {

    @GET("players_info")
    suspend fun getPlayerInfo(
        @Query("apikey") apiKey:String ,
        @Query("id") playerId: String
    ): PlayerResponse
}