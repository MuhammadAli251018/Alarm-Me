package dev.alidroid.alarmme.feature.alarmDetails.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.alidroid.alarmme.feature.alarmDetails.R
import dev.alidroid.alarmme.ui.component.AmSectionCard
import dev.alidroid.alarmme.ui.theme.AlarmMeThemeProvider
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeColors
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeShapes
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeTypography
import dev.alidroid.alarmme.utility.compose.LtrContent
import dev.alidroid.alarmme.utility.kotlinDateTime.now
import dev.alidroid.alarmme.utility.kotlinDateTime.toClockLabel
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@Composable
fun AlarmTimeDetails(
    modifier: Modifier,
    time: LocalTime,
    nextOccurrence: LocalDateTime?,
    onClick: () -> Unit
) {
    val colors = LocalAlarmMeColors.current
    val typography = LocalAlarmMeTypography.current
    val shapes = LocalAlarmMeShapes.current

    AmSectionCard(
        modifier = modifier.fillMaxWidth(),
        shape = shapes.medium,
        onClick = onClick
    ) {
        Column(
            Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val eyebrow = nextOccurrence.toRingsInLabel()

            Text(
                text = eyebrow,
                style = typography.labelSmall,
                color = colors.primary
            )

            // The time and its meridiem read in the same order in every locale.
            LtrContent {
                // Centred via the arrangement rather than ColumnScope.align, which is not in
                // scope inside LtrContent; the parent Column already fills the width.
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .padding(bottom = 8.dp),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally)
                ) {
                    val timeString = remember(time) { time.toClockLabel() }
                    val meridiem = remember(time) { if (time.hour < 12) "AM" else "PM" }

                    Text(
                        text = timeString,
                        style = typography.alarmRow.copy(fontSize = 56.sp),
                        color = colors.onSurface
                    )

                    Text(
                        modifier = Modifier.padding(bottom = 6.dp),
                        text = meridiem,
                        style = typography.meridiem,
                        color = colors.onSurfaceVariant
                    )
                }
            }
        }
    }
}

/**
 * How long until the alarm actually rings. Takes the already-computed next occurrence rather than a
 * bare time of day, because the wait depends on the schedule — "Wed only" at 6:40 is up to a week
 * out, not the same evening.
 */
@OptIn(ExperimentalTime::class)
@Composable
@ReadOnlyComposable
private fun LocalDateTime?.toRingsInLabel(
    now: LocalDateTime = LocalDateTime.now(),
    timeZone: TimeZone = TimeZone.currentSystemDefault()
): String {
    if (this == null) return stringResource(R.string.alarm_details_not_scheduled)

    // Subtract instants, not individual fields: per-field subtraction goes negative whenever the
    // alarm's minute is below the current minute, and breaks entirely across midnight. Clamped at
    // zero so an alarm that has just fired never counts backwards.
    val remaining = (toInstant(timeZone) - now.toInstant(timeZone)).coerceAtLeast(Duration.ZERO)

    return stringResource(
        R.string.alarm_details_rings_in,
        remaining.inWholeHours,
        remaining.inWholeMinutes % 60
    )
}

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
            time = LocalTime(7, 5),
            nextOccurrence = LocalDateTime.now(),
            onClick = {}
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
            time = LocalTime(0, 40),
            nextOccurrence = LocalDateTime.now(),
            onClick = {}
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
            time = LocalTime(12, 0),
            nextOccurrence = LocalDateTime.now(),
            onClick = {}
        )
    }
}

/** Nothing scheduled — no repeat day and no custom date, so there is no countdown to show. */
@Preview(name = "Light Mode--Not Scheduled", showBackground = true)
@Preview(
    name = "Dark Mode--Not Scheduled",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AlarmTimeDetailsNotScheduledPreview() {
    AlarmMeThemeProvider {
        AlarmTimeDetails(
            modifier = Modifier.padding(16.dp),
            time = LocalTime(6, 40),
            nextOccurrence = null,
            onClick = {}
        )
    }
}
