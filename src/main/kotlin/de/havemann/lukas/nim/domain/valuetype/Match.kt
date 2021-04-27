package de.havemann.lukas.nim.domain.valuetype

/**
 * Value type representing a Match or set of matches.
 */
class Match(var value: Int) : Comparable<Match> {
    companion object {
        val ZERO = Match(0)
        val ONE = Match(1)
        val TWO = Match(2)
        val THREE = Match(3)

        fun min(a: Match, b: Match): Match {
            return if (a < b) a else b
        }
    }

    init {
        require(value >= 0)
    }

    operator fun minus(otherMatches: Match): Match {
        return Match(value - otherMatches.value)
    }

    operator fun rangeTo(other: Match): ClosedRange<Match> {
        return MatchRange(this, other)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Match

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return value
    }

    override fun toString(): String {
        return "Matches(value=$value)"
    }

    override fun compareTo(other: Match): Int {
        return this.value - other.value
    }
}

class MatchRange(
    override val start: Match,
    override val endInclusive: Match
) : ClosedRange<Match>
