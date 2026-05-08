package com.example.hitormiss.utils

import android.content.Context
import com.example.hitormiss.data.api.PlayerApi
import com.example.hitormiss.data.api.RetrofitInstance
import com.example.hitormiss.data.repository.PlayerRepository
import com.example.hitormiss.data.room.AppDatabase
import com.example.hitormiss.domain.usecase.GenerateQuestionUseCase

object AppDependencies {

    private lateinit var database: AppDatabase
    private lateinit var api: PlayerApi

    fun init(context: Context) {
        database = AppDatabase.invoke(context)
        api = RetrofitInstance().api
    }

    private val playerDao by lazy {
        database.playerDao()
    }

    private val statsDao by lazy {
        database.playerStatsDao()
    }

    val repository by lazy {
        PlayerRepository(api, playerDao, statsDao)
    }

    val generateQuestionUseCase by lazy {
        GenerateQuestionUseCase(repository)
    }
}