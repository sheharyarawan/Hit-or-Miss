package com.example.hitormiss.data.model

data class PlayerInfo(
    val battingStyle: String,
    val bowlingStyle: String,
    val country: String,
    val dateOfBirth: String,
    val id: String,
    val name: String,
    val placeOfBirth: String,
    val playerImg: String,
    val role: String,
    val stats: List<Stat>
)