package com.example.hitormiss.data.api

import com.example.hitormiss.data.model.PlayerInfo
import retrofit2.http.GET
import retrofit2.http.Path

interface PlayerApi {

    @GET("player/{id}")
    suspend fun getPlayer(
        @Path("id") id: String
    ): PlayerInfo
}