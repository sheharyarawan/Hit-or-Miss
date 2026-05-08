package com.example.hitormiss.utils

enum class MatchFormat {
    TEST,
    ODI,
    T20I
}

fun String?.toMatchFormatOrNull(): MatchFormat? =
    when (this?.trim()?.lowercase()) {
        "test" -> MatchFormat.TEST
        "odi" -> MatchFormat.ODI
        "t20i", "t20" -> MatchFormat.T20I
        else -> null
    }

