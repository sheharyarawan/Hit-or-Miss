package com.example.hitormiss.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "players")
data class Player(

    @PrimaryKey val id: String,

    val name: String,
    val country: String,
    val role: String,

    val battingStyle: String?,
    val bowlingStyle: String?,

    val imageUrl: String?,

    val dateOfBirth: String?,
    val placeOfBirth: String?
)