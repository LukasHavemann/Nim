package de.havemann.lukas.nim.domain.valuetype

import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class MatchesTest {

    @Test
    fun `creating negative matches is not allowed`() {
        assertThrows<IllegalArgumentException> {
            Match(-10)
        }
    }

    @Test
    fun `drawing until negative range should be possible`() {
        val softly = SoftAssertions()
        softly.assertThat((Match(13) - Match.THREE).value).isEqualTo(10)
        softly.assertThat((Match(3) - Match.THREE).value).isEqualTo(0)
        softly.assertThatThrownBy { Match.ONE - Match.THREE }
        softly.assertAll()
    }

    @Test
    fun `equals and hashcode test`() {
        val softly = SoftAssertions()
        softly.assertThat(Match.THREE).isEqualTo(Match(3))
        softly.assertThat(Match.THREE.hashCode()).isEqualTo(Match(3).hashCode())
        softly.assertAll()
    }

    @Test
    fun `operator overloading test`() {
        val softly = SoftAssertions()
        softly.assertThat(Match.ONE > Match.THREE).isFalse
        softly.assertThat(Match.ONE > Match.ONE).isFalse
        softly.assertThat(Match.TWO > Match.ONE).isTrue
        softly.assertThat(Match.ONE == Match.ONE).isTrue
        softly.assertThat(Match.ONE == Match(1)).isTrue
        softly.assertAll()
    }

    @Test
    fun `range expression works`() {
        val range = Match.ONE..Match.THREE

        val softly = SoftAssertions()
        softly.assertThat(range.contains(Match.ONE)).isTrue
        softly.assertThat(range.contains(Match(4))).isFalse
        softly.assertAll()
    }

    @Test
    fun `max test`() {
        val softly = SoftAssertions()
        softly.assertThat(Match.min(Match.ONE, Match.THREE)).isEqualTo(Match.ONE)
        softly.assertThat(Match.min(Match.THREE, Match.ONE)).isEqualTo(Match.ONE)
        softly.assertAll()
    }
}