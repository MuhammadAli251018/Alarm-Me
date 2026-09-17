package com.muhammadali.alarmme.feature.alarmDetails.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.muhammadali.alarmme.ui.component.AmSectionCard
import com.muhammadali.alarmme.ui.theme.AlarmMeThemeProvider
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeColors
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeShapes
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeTypography
import com.muhammadali.alarmme.utility.kotlinDateTime.now
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@Composable
fun AlarmTimeDetails(
    modifier: Modifier,
    time: LocalDateTime
) {
    val colors = LocalAlarmMeColors.current
    val typography = LocalAlarmMeTypography.current
    val shapes = LocalAlarmMeShapes.current

    AmSectionCard(
        modifier = modifier.fillMaxWidth(),
        shape = shapes.medium
    ) {
        Column(
            modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val topBarEyebrow = remember(time) { time.toTopBarEyebrow() }

            Text(
                text = topBarEyebrow,
                style = typography.labelSmall,
                color = colors.primary
            )

            Row(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 16.dp)
                    .padding(bottom = 8.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                val colors = LocalAlarmMeColors.current
                val timeString = remember(time) { time.toFormattedString() }
                val meridiem = remember(time) { if (time.hour < 12) "AM" else "PM" }

                Text(
                    text = timeString,
                    style = LocalAlarmMeTypography.current.alarmRow.copy(
                        fontSize = 56.sp
                    ),
                    color = colors.onSurface
                )

                Text(
                    modifier = Modifier.padding(bottom = 6.dp),
                    text = meridiem,
                    style = LocalAlarmMeTypography.current.meridiem,
                    color = colors.onSurfaceVariant
                )
            }
        }
    }
}

// TODO: Move the formate and date-time helper functions into :core:utility:kotlin-date-time
@OptIn(ExperimentalTime::class)
private fun LocalDateTime?.toTopBarEyebrow(
    now: LocalDateTime = LocalDateTime.now(),
    timeZone: TimeZone = TimeZone.currentSystemDefault()
): String {
    if (this == null) return "NO ALARMS ARMED"

    // Subtract instants, not individual fields. Per-field subtraction goes negative whenever the
    // alarm's minute is below the current minute (6:40 vs 7:50 -> "-1H -10M") and breaks entirely
    // across midnight. Clamped at zero so an alarm that has just fired never counts backwards.
    val remaining = (toInstant(timeZone) - now.toInstant(timeZone)).coerceAtLeast(Duration.ZERO)
    val remainingHours = remaining.inWholeHours
    val remainingMinutes = remaining.inWholeMinutes % 60

    return "RINGS IN · ${remainingHours}H ${remainingMinutes}M"
}

private fun LocalDateTime.toFormattedString(): String {
    val hoursInTwelve = if (hour % 12 == 0) 12 else hour % 12
    val hours = hoursInTwelve.toString().padStart(2, '0')
    val minutes = time.minute.toString().padStart(2, '0')
    return "$hours:$minutes"
}

private val PreviewDate = LocalDate(2026, 9, 17)

@Preview(name = "Light Mode", showBackground = true)
@Preview(
    name = "Dark Mode",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AlarmTimeDetailsPreview() {
    AlarmMeThemeProvider {
        AlarmTimeDetails(
            modifier = Modifier.padding(16.dp),
            time = LocalDateTime(PreviewDate, LocalTime(7, 5))
        )
    }
}

/** Midnight - exercises the `hour % 12 == 0 -> 12` branch on the AM side. */
@Preview(name = "Light Mode--Midnight", showBackground = true)
@Preview(
    name = "Dark Mode--Midnight",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AlarmTimeDetailsMidnightPreview() {
    AlarmMeThemeProvider {
        AlarmTimeDetails(
            modifier = Modifier.padding(16.dp),
            time = LocalDateTime(PreviewDate, LocalTime(0, 40))
        )
    }
}

/** Noon - the `hour % 12 == 0` branch on the PM side, plus a `:00` minute padding check. */
@Preview(name = "Light Mode--Noon", showBackground = true)
@Preview(
    name = "Dark Mode--Noon",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AlarmTimeDetailsNoonPreview() {
    AlarmMeThemeProvider {
        AlarmTimeDetails(
            modifier = Modifier.padding(16.dp),
            time = LocalDateTime(PreviewDate, LocalTime(12, 0))
        )
    }
}
