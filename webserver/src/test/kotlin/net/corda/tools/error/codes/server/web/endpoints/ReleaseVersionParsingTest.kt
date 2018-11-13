package net.corda.tools.error.codes.server.web.endpoints

import net.corda.tools.error.codes.server.domain.ReleaseVersion
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ReleaseVersionParsingTest {

    @Test
    fun major_minor_patch() {
        val major = 4
        val minor = 3
        val patch = 2
        val expected = ReleaseVersion(major, minor, patch)

        // Do not derive this, otherwise a change in the domain will change the test.
        val raw = "$major.$minor.$patch"

        val actual = releaseVersion(raw)

        assertThat(actual.isValid)
        assertThat(actual.validValue()).isEqualTo(expected)
    }

    @Test
    fun major_minor_patch_snapshot() {
        val major = 4
        val minor = 3
        val patch = 2
        val expected = ReleaseVersion(major, minor, patch).snapshot()

        // Do not derive this, otherwise a change in the domain will change the test.
        val raw = "$major.$minor.$patch-SNAPSHOT"

        val actual = releaseVersion(raw)

        assertThat(actual.isValid)
        assertThat(actual.validValue()).isEqualTo(expected)
    }

    @Test
    fun major_minor_snapshot() {
        val major = 4
        val minor = 3
        val expected = ReleaseVersion(major, minor).snapshot()

        // Do not derive this, otherwise a change in the domain will change the test.
        val raw = "$major.$minor-SNAPSHOT"

        val actual = releaseVersion(raw)

        assertThat(actual.isValid)
        assertThat(actual.validValue()).isEqualTo(expected)
    }
}