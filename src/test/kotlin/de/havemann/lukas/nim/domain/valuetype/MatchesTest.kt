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
        SoftAssertions().apply {
            assertThat((Match(13) - Match.THREE).value).isEqualTo(10)
            assertThat((Match(3) - Match.THREE).value).isEqualTo(0)
            assertThatThrownBy { Match.ONE - Match.THREE }
        }.assertAll()
    }

    @Test
    fun `equals and hashcode test`() {
        SoftAssertions().apply {
            assertThat(Match.THREE).isEqualTo(Match(3))
            assertThat(Match.THREE.hashCode()).isEqualTo(Match(3).hashCode())
        }.assertAll()
    }

    @Test
    fun `operator overloading test`() {
        SoftAssertions().apply {
            assertThat(Match.ONE > Match.THREE).isFalse
            assertThat(Match.ONE > Match.ONE).isFalse
            assertThat(Match.TWO > Match.ONE).isTrue
            assertThat(Match.ONE == Match.ONE).isTrue
            assertThat(Match.ONE == Match(1)).isTrue
        }.assertAll()
    }

    @Test
    fun `range expression works`() {
        val range = Match.ONE..Match.THREE

        SoftAssertions().apply {
            assertThat(range.contains(Match.ONE)).isTrue
            assertThat(range.contains(Match(4))).isFalse
        }.assertAll()
    }

    @Test
    fun `max test`() {
        SoftAssertions().apply {
            assertThat(Match.min(Match.ONE, Match.THREE)).isEqualTo(Match.ONE)
            assertThat(Match.min(Match.THREE, Match.ONE)).isEqualTo(Match.ONE)
        }.assertAll()
    }
}