package com.muhammadali.alarmme.feature.alarmRinging.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.muhammadali.alarmme.ui.theme.AlarmMeThemeProvider
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeColors
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeTypography

@Composable
fun RingingActionStack(
    snoozeLabel: String,
    dismissLabel: String,
    hint: String,
    onSnoozeClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    snoozeDurationLabel: String? = null,
    snoozeOnClickLabel: String? = null,
    snoozeExhausted: Boolean = false,
) {
    val colors = LocalAlarmMeColors.current
    val typography = LocalAlarmMeTypography.current
    var dismissHeld by remember { mutableStateOf(false) }

    val snoozeMode = when {
        snoozeExhausted -> SnoozeMode.Exhausted
        dismissHeld -> SnoozeMode.Locked
        else -> SnoozeMode.Available
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SnoozeButton(
            label = snoozeLabel,
            durationLabel = snoozeDurationLabel,
            mode = snoozeMode,
            onClickLabel = snoozeOnClickLabel,
            modifier = Modifier.fillMaxWidth(),
            onClick = onSnoozeClick,
        )

        HoldToDismissButton(
            label = dismissLabel,
            onHoldChange = { dismissHeld = it },
            modifier = Modifier.fillMaxWidth(),
            onDismiss = onDismiss,
        )

        Text(
            modifier = Modifier
                .padding(top = 2.dp)
                .fillMaxWidth(),
            text = hint,
            style = typography.labelSmall,
            color = colors.outline,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview(name = "Light Mode--Ringing", showBackground = true, heightDp = 320)
@Preview(
    name = "Dark Mode--Ringing",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES,
    heightDp = 320,
)
@Composable
private fun RingingActionStackPreview() {
    PreviewRingingActionStack(
        snoozeLabel = "Snooze",
        snoozeDurationLabel = "9 min",
        dismissLabel = "Hold to dismiss",
        hint = "Snooze = one tap · dismiss = press and hold",
    )
}

@Preview(name = "Light Mode--Snoozed", showBackground = true, heightDp = 320)
@Preview(
    name = "Dark Mode--Snoozed",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES,
    heightDp = 320,
)
@Composable
private fun RingingActionStackSnoozedPreview() {
    PreviewRingingActionStack(
        snoozeLabel = "Snooze again (2 left)",
        snoozeDurationLabel = null,
        dismissLabel = "Hold to dismiss for good",
        hint = "The countdown keeps ticking while the screen is dim",
    )
}

@Preview(name = "Light Mode--Exhausted", showBackground = true, heightDp = 320)
@Preview(
    name = "Dark Mode--Exhausted",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES,
    heightDp = 320,
)
@Composable
private fun RingingActionStackExhaustedPreview() {
    PreviewRingingActionStack(
        snoozeLabel = "No snoozes left",
        snoozeDurationLabel = null,
        dismissLabel = "Hold to dismiss",
        hint = "Snooze = one tap · dismiss = press and hold",
        snoozeExhausted = true,
    )
}

/** The narrow column landscape puts the pair in. */
@Preview(name = "Light Mode--Narrow", showBackground = true, widthDp = 300, heightDp = 320)
@Preview(
    name = "Dark Mode--Narrow",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES,
    widthDp = 300,
    heightDp = 320,
)
@Composable
private fun RingingActionStackNarrowPreview() {
    PreviewRingingActionStack(
        snoozeLabel = "Snooze",
        snoozeDurationLabel = "9 min",
        dismissLabel = "Hold to dismiss",
        hint = "Snooze = one tap · dismiss = press and hold",
    )
}

@Composable
private fun PreviewRingingActionStack(
    snoozeLabel: String,
    snoozeDurationLabel: String?,
    dismissLabel: String,
    hint: String,
    snoozeExhausted: Boolean = false,
) {
    AlarmMeThemeProvider {
        RingingActionStack(
            snoozeLabel = snoozeLabel,
            snoozeDurationLabel = snoozeDurationLabel,
            snoozeExhausted = snoozeExhausted,
            dismissLabel = dismissLabel,
            hint = hint,
            modifier = Modifier
                .background(LocalAlarmMeColors.current.surfaceContainerLowest)
                .padding(horizontal = 20.dp, vertical = 24.dp),
            onSnoozeClick = {},
            onDismiss = {},
        )
    }
}
