package dev.mbo.t60f.global

import org.springframework.stereotype.Component
import java.time.Instant
import java.time.ZoneId
import java.util.Date

@Suppress("unused")
@Component
object DateUtils {
    @JvmStatic
    fun toLocal(instant: Instant?): Date? {
        return instant?.atZone(ZoneId.systemDefault())?.toInstant()?.let { Date.from(it) }
    }
}