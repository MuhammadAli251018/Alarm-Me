package com.muhammadali.alarmme.feature.alarmRinging.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.muhammadali.alarmme.ui.theme.AlarmMeThemeProvider
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeColors
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeShapes
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeTypography
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

private val MinHeight: Dp = 72.dp
private val MaxWidth: Dp = 480.dp
private const val EDGE_ALPHA = 0.45f

@Composable
fun SnoozeButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    durationLabel: String? = null,
    mode: SnoozeMode = SnoozeMode.Available,
    onClickLabel: String? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val colors = LocalAlarmMeColors.current
    val enabled = mode == SnoozeMode.Available
    val shape = LocalAlarmMeShapes.current.large

    val container = when (mode) {
        SnoozeMode.Available -> colors.primaryContainer
        SnoozeMode.Locked -> colors.surfaceContainerHigh
        SnoozeMode.Exhausted -> Color.Transparent
    }
    val content = when (mode) {
        SnoozeMode.Available -> colors.onPrimaryContainer
        SnoozeMode.Locked, SnoozeMode.Exhausted -> colors.outline
    }
    val edge = when (mode) {
        SnoozeMode.Available -> colors.primary.copy(alpha = EDGE_ALPHA)
        SnoozeMode.Locked, SnoozeMode.Exhausted -> colors.outlineVariant
    }

    Row(
        modifier = modifier
            .defaultMinSize(minHeight = MinHeight, minWidth = MaxWidth)
            .clip(shape)
            .background(container)
            .border(width = 1.dp, color = edge, shape = shape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClickLabel = onClickLabel,
                role = Role.Button,
                onClick = onClick,
            )
            .padding(horizontal = 32.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
    ) {
        Text(
            text = label,
            style = LocalAlarmMeTypography.current.titleMedium,
            color = content,
            textAlign = TextAlign.Center,
        )

        if (durationLabel != null && mode == SnoozeMode.Available) {
            Text(
                text = durationLabel,
                style = LocalAlarmMeTypography.current.meridiem,
                color = colors.primary,
            )
        }
    }
}

enum class SnoozeMode {
    Available,

    /** A dismiss hold is in progress; the design dims this and takes it out of the touch stream. */
    Locked,

    /** The alarm's snooze count has run out. Stated, not hidden. */
    Exhausted,
}

@Preview(name = "Light Mode--Default", showBackground = true)
@Preview(name = "Dark Mode--Default", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SnoozeButtonPreview() {
    PreviewSnoozeButton(label = "Snooze", durationLabel = "9 min")
}

@Preview(name = "Light Mode--Pressed", showBackground = true)
@Preview(name = "Dark Mode--Pressed", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SnoozeButtonPressedPreview() {
    PreviewSnoozeButton(
        label = "Snooze",
        durationLabel = "9 min",
        interactionSource = rememberStuckPressedInteractionSource(),
    )
}

@Preview(name = "Light Mode--Locked", showBackground = true)
@Preview(name = "Dark Mode--Locked", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SnoozeButtonLockedPreview() {
    PreviewSnoozeButton(label = "Snooze", mode = SnoozeMode.Locked)
}

/** Snoozed at least once, so the remaining count is stated. */
@Preview(name = "Light Mode--Again", showBackground = true)
@Preview(name = "Dark Mode--Again", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SnoozeButtonAgainPreview() {
    PreviewSnoozeButton(label = "Snooze again (2 left)")
}

@Preview(name = "Light Mode--Exhausted", showBackground = true)
@Preview(name = "Dark Mode--Exhausted", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SnoozeButtonExhaustedPreview() {
    PreviewSnoozeButton(label = "No snoozes left", mode = SnoozeMode.Exhausted)
}

@Composable
private fun PreviewSnoozeButton(
    label: String,
    durationLabel: String? = null,
    mode: SnoozeMode = SnoozeMode.Available,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    AlarmMeThemeProvider {
        SnoozeButton(
            label = label,
            durationLabel = durationLabel,
            mode = mode,
            interactionSource = interactionSource,
            modifier = Modifier
                .background(LocalAlarmMeColors.current.surfaceContainerLowest)
                .padding(24.dp)
                .fillMaxWidth(),
            onClick = {},
        )
    }
}

/**
 * An interaction source stuck pressed, so a preview can render the state layer that otherwise only
 * exists mid-gesture. `core:ui` has the same helper, but `internal` to that module.
 */
private class StuckInteractionSource(interaction: Interaction) : MutableInteractionSource {
    override val interactions: Flow<Interaction> = flowOf(interaction)
    override suspend fun emit(interaction: Interaction) = Unit
    override fun tryEmit(interaction: Interaction): Boolean = false
}

@Composable
private fun rememberStuckPressedInteractionSource(): MutableInteractionSource =
    remember { StuckInteractionSource(PressInteraction.Press(Offset.Zero)) }
