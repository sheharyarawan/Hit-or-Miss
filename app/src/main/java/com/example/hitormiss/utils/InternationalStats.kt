package com.example.hitormiss.utils

import com.example.hitormiss.data.model.Stat

object InternationalStats {

    private val INTERNATIONAL_MATCH_TYPES = setOf("test", "odi", "t20i")

    data class ByFormatInt(val test: Int, val odi: Int, val t20i: Int) {
        val total: Int get() = test + odi + t20i
    }

    data class ByFormatFloat(val test: Float, val odi: Float, val t20i: Float)


    data class InternationalBatting(
        val runsByFormat: ByFormatInt,
        val foursByFormat: ByFormatInt,
        val sixesByFormat: ByFormatInt,
        val matchesByFormat: ByFormatInt,
        val innsByFormat: ByFormatInt,
        val notOutByFormat: ByFormatInt,
        val ballsFacedByFormat: ByFormatInt,
        val average: Float,
        val strikeRate: Float,
        val averageByFormat: ByFormatFloat,
        val strikeRateByFormat: ByFormatFloat
    )


    data class InternationalBowling(
        val wicketsByFormat: ByFormatInt,
        val ballsByFormat: ByFormatInt,
        val runsConcededByFormat: ByFormatInt,
        val economy: Float,
        val economyByFormat: ByFormatFloat
    )

    fun getInternationalRuns(stats: List<Stat>): Int =
        getInternationalBatting(stats).runsByFormat.total

    fun getInternationalRunsByFormat(stats: List<Stat>): ByFormatInt =
        getInternationalBatting(stats).runsByFormat

    fun getInternationalBatting(stats: List<Stat>): InternationalBatting {
        val latest = latestInternational(stats)

        fun v(matchType: String, stat: String): String? =
            latest["batting|$matchType|$stat"]?.value

        fun i(matchType: String, stat: String): Int =
            v(matchType, stat)?.toSafeInt() ?: 0

        val runs = ByFormatInt(
            test = i("test", "runs"),
            odi = i("odi", "runs"),
            t20i = i("t20i", "runs")
        )
        val fours = ByFormatInt(
            test = i("test", "4s"),
            odi = i("odi", "4s"),
            t20i = i("t20i", "4s")
        )
        val sixes = ByFormatInt(
            test = i("test", "6s"),
            odi = i("odi", "6s"),
            t20i = i("t20i", "6s")
        )
        val matches = ByFormatInt(
            test = i("test", "m"),
            odi = i("odi", "m"),
            t20i = i("t20i", "m")
        )
        val inns = ByFormatInt(
            test = i("test", "inn"),
            odi = i("odi", "inn"),
            t20i = i("t20i", "inn")
        )
        val notOut = ByFormatInt(
            test = i("test", "no"),
            odi = i("odi", "no"),
            t20i = i("t20i", "no")
        )
        val ballsFaced = ByFormatInt(
            test = i("test", "bf"),
            odi = i("odi", "bf"),
            t20i = i("t20i", "bf")
        )

        fun f(matchType: String, stat: String): Float =
            v(matchType, stat)?.toSafeFloat() ?: 0f

        val avgByFormat = ByFormatFloat(
            test = round2(f("test", "avg")),
            odi = round2(f("odi", "avg")),
            t20i = round2(f("t20i", "avg"))
        )
        val srByFormat = ByFormatFloat(
            test = round2(f("test", "sr")),
            odi = round2(f("odi", "sr")),
            t20i = round2(f("t20i", "sr"))
        )

        val totalInns = inns.total
        val totalNotOut = notOut.total
        val outs = (totalInns - totalNotOut).coerceAtLeast(0)
        val avg = if (outs > 0) runs.total.toFloat() / outs else 0f

        val totalBf = ballsFaced.total
        val sr = if (totalBf > 0) (runs.total.toFloat() / totalBf) * 100f else 0f

        return InternationalBatting(
            runsByFormat = runs,
            foursByFormat = fours,
            sixesByFormat = sixes,
            matchesByFormat = matches,
            innsByFormat = inns,
            notOutByFormat = notOut,
            ballsFacedByFormat = ballsFaced,
            average = round2(avg),
            strikeRate = round2(sr),
            averageByFormat = avgByFormat,
            strikeRateByFormat = srByFormat
        )
    }

    fun getInternationalBowling(stats: List<Stat>): InternationalBowling {
        val latest = latestInternational(stats)

        fun v(matchType: String, stat: String): String? =
            latest["bowling|$matchType|$stat"]?.value

        fun i(matchType: String, stat: String): Int =
            v(matchType, stat)?.toSafeInt() ?: 0

        val wickets = ByFormatInt(
            test = i("test", "wkts"),
            odi = i("odi", "wkts"),
            t20i = i("t20i", "wkts")
        )
        val balls = ByFormatInt(
            test = i("test", "b"),
            odi = i("odi", "b"),
            t20i = i("t20i", "b")
        )
        val runsConceded = ByFormatInt(
            test = i("test", "runs"),
            odi = i("odi", "runs"),
            t20i = i("t20i", "runs")
        )

        val overs = balls.total / 6f
        val econ = if (overs > 0f) runsConceded.total / overs else 0f

        fun econFormat(matchType: String): Float {
            val oversF = i(matchType, "b") / 6f
            val runsF = i(matchType, "runs")
            return if (oversF > 0f) runsF / oversF else 0f
        }
        val econByFormat = ByFormatFloat(
            test = round2(econFormat("test")),
            odi = round2(econFormat("odi")),
            t20i = round2(econFormat("t20i"))
        )

        return InternationalBowling(
            wicketsByFormat = wickets,
            ballsByFormat = balls,
            runsConcededByFormat = runsConceded,
            economy = round2(econ),
            economyByFormat = econByFormat
        )
    }

    private fun latestInternational(stats: List<Stat>): MutableMap<String, Stat> {
        val latest: MutableMap<String, Stat> = linkedMapOf()
        for (s in stats) {
            val fn = s.fn.normalizeFn()
            val matchType = s.matchtype.normalizeMatchType()
            val statKey = s.stat.normalizeStatKey()
            if (matchType !in INTERNATIONAL_MATCH_TYPES) continue
            val k = "$fn|$matchType|$statKey"
            latest[k] = s
        }
        return latest
    }

    private fun String.normalizeMatchType(): String =
        lowercase().trim().let {
            // CricAPI sometimes returns "t20" even for international T20Is (and duplicates entries).
            if (it == "t20") "t20i" else it
        }

    private fun String.normalizeStatKey(): String =
        lowercase().trim()

    private fun String.normalizeFn(): String =
        lowercase().trim()

    private fun String.toSafeInt(): Int =
        trim()
            .replace(",", "")
            .replace(Regex("[^0-9-]"), "")
            .toIntOrNull() ?: 0

    private fun String.toSafeFloat(): Float =
        trim()
            .replace(",", "")
            .replace(Regex("[^0-9.-]"), "")
            .toFloatOrNull() ?: 0f

    private fun round2(v: Float): Float =
        kotlin.math.round(v * 100f) / 100f
}

