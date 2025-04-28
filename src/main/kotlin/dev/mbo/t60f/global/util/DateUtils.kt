package dev.mbo.t60f.global.util

import org.springframework.stereotype.Component
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Suppress("unused")
@Component
object DateUtils {
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm'Z'").withZone(ZoneOffset.UTC)

    @JvmStatic
    fun formatUtc(instant: Instant?): String? {
        instant ?: return null
        return formatter.format(instant)
    }
}