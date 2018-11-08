package com.eaglesakura.armyknife.runtime.time

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

/**
 * Time Duration.
 */
@RunWith(AndroidJUnit4::class)
class DurationTest {

    @Test
    fun toSeconds() {
        assertThat(Duration.from(1, TimeUnit.SECONDS).toSeconds()).apply {
            isEqualTo(1.0)
        }

        assertThat(Duration.from(500, TimeUnit.MILLISECONDS).toSeconds()).apply {
            isEqualTo(0.5)
        }
    }

    @Test
    fun toMinutes() {
        assertThat(Duration.from(1, TimeUnit.MINUTES).toMinutes()).apply {
            isEqualTo(1.0)
        }
    }

    @Test
    fun toHours() {
        assertThat(Duration.from(1, TimeUnit.HOURS).toHours()).apply {
            isEqualTo(1.0)
        }
    }

    @Test
    fun toDays() {
        assertThat(Duration.from(1, TimeUnit.DAYS).toDays()).apply {
            isEqualTo(1.0)
        }
    }
}