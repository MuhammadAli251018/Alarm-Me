package dev.alidroid.alarmme.feature.alarmRinging.presentation.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.alidroid.alarmme.ui.component.AmIcon
import dev.alidroid.alarmme.ui.component.AmIconDefaults
import dev.alidroid.alarmme.ui.theme.AlarmMeIcons
import dev.alidroid.alarmme.ui.theme.AlarmMeThemeProvider
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeColors
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeShapes
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeTypography
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.fold

private val MinHeight: Dp = 96.dp
private val MaxWidth: Dp = 480.dp

private const val HOLD_MILLIS = 5000

private const val CANCEL_MILLIS = 180
private const val HALFWAY = 0.5f

@Composable
fun HoldToDismissButton(
    label: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onHoldChange: (Boolean) -> Unit = {},
) {
    val haptics = LocalHapticFeedback.current

    var completed by rememberSaveable { mutableStateOf(false) }
    var held by remember { mutableStateOf(false) }
    val progress = remember { Animatable(if (completed) 1f else 0f) }

    val phase = when {
        completed -> HoldPhase.Dismissed
        held -> HoldPhase.Holding
        else -> HoldPhase.Rest
    }

    LaunchedEffect(held) {
        if (completed) return@LaunchedEffect

        if (held) {
            val remaining = ((1f - progress.value) * HOLD_MILLIS).toInt()
            progress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = remaining, easing = LinearEasing),
            )
            completed = true
        } else {
            progress.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = CANCEL_MILLIS, easing = LinearEasing),
            )
        }
    }

    LaunchedEffect(completed) {
        if (!completed) return@LaunchedEffect
        // TalkBack's double-tap completes it without ever animating, so make the gauge agree with
        // the label before anyone sees "Dismissed" over an empty bar.
        progress.snapTo(1f)
        // Completing re-keys the pointerInput, which cancels the block that would otherwise have
        // cleared this on release. Left true, it keeps snooze locked after the alarm is gone.
        held = false
        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
        onDismiss()
    }

    /* The halfway tick. Only when going forward. */
    LaunchedEffect(Unit) {
        val initial = progress.value
        var ticked = false

        snapshotFlow { progress.value }
            .drop(1)
            .fold(initial) { old, new ->
                val isForwardProgress = new > old
                val isHalfway = new > HALFWAY

                ticked = isHalfway && ticked

                if (ticked) return@fold new
                if (isForwardProgress && isHalfway) {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    ticked = true
                }
                return@fold new
            }
    }

    LaunchedEffect(held) { onHoldChange(held) }

    HoldToDismissSurface(
        progress = { progress.value },
        phase = phase,
        label = label,
        modifier = modifier
            .pointerInput(enabled, completed) {
                if (!enabled || completed) return@pointerInput
                detectTapGestures(
                    onPress = {
                        held = true
                        // Returns on release or cancellation; either way the hold is over.
                        tryAwaitRelease()
                        held = false
                    },
                )
            }
            .semantics(mergeDescendants = true) {
                role = Role.Button
                contentDescription = "Dismiss alarm. Double tap and hold."
                if (phase == HoldPhase.Dismissed) stateDescription = "Dismissed"
                onClick(label = "Dismiss alarm") {
                    if (enabled && !completed) completed = true
                    true
                }
            },
    )
}


@Composable
internal fun HoldToDismissSurface(
    progress: () -> Float,
    phase: HoldPhase,
    label: String,
    modifier: Modifier = Modifier,
) {
    val colors = LocalAlarmMeColors.current
    val typography = LocalAlarmMeTypography.current
    val shape = LocalAlarmMeShapes.current.large

    val container = if (phase == HoldPhase.Holding) colors.primaryContainer else colors.primary
    val content = if (phase == HoldPhase.Holding) colors.onPrimaryContainer else colors.onPrimary
    val fill = colors.primary

    val text = when (phase) {
        HoldPhase.Rest -> label
        HoldPhase.Holding -> "Keep holding…"
        HoldPhase.Dismissed -> "Dismissed"
    }

    Box(
        modifier = modifier
            .defaultMinSize(minHeight = MinHeight, minWidth = MaxWidth)
            .clip(shape)
            .drawBehind {
                drawRect(color = container)
                val swept = size.width * progress().coerceIn(0f, 1f)
                if (swept > 0f) {
                    drawRect(color = fill, size = Size(swept, size.height))
                }
            }
            .padding(horizontal = 32.dp),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = text,
                    style = typography.headlineSmall,
                    color = content,
                    textAlign = TextAlign.Center,
                )

                if (phase == HoldPhase.Rest) {
                    Text(text = "press and hold", style = typography.meridiem, color = content)
                }
            }

            if (phase == HoldPhase.Dismissed) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .border(width = 2.dp, color = content, shape = CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    AmIcon(
                        painter = AlarmMeIcons.Check,
                        contentDescription = null,
                        size = AmIconDefaults.Small,
                        tint = content,
                    )
                }
            }
        }
    }
}

internal enum class HoldPhase { Rest, Holding, Dismissed }

@Preview(name = "Light Mode--Rest", showBackground = true)
@Preview(name = "Dark Mode--Rest", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HoldToDismissRestPreview() {
    PreviewHoldToDismissButton()
}

@Preview(name = "Light Mode--30% held", showBackground = true)
@Preview(name = "Dark Mode--30% held", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HoldToDismissEarlyPreview() {
    PreviewHoldToDismiss(progress = 0.3f, phase = HoldPhase.Holding)
}

@Preview(name = "Light Mode--85% held", showBackground = true)
@Preview(name = "Dark Mode--85% held", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HoldToDismissLatePreview() {
    PreviewHoldToDismiss(progress = 0.85f, phase = HoldPhase.Holding)
}

@Preview(name = "Light Mode--Dismissed", showBackground = true)
@Preview(name = "Dark Mode--Dismissed", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HoldToDismissCompletePreview() {
    PreviewHoldToDismiss(progress = 1f, phase = HoldPhase.Dismissed)
}

@Composable
private fun PreviewHoldToDismiss(progress: Float, phase: HoldPhase) {
    AlarmMeThemeProvider {
        HoldToDismissSurface(
            progress = { progress },
            phase = phase,
            label = "Hold to dismiss",
            modifier = Modifier
                .background(LocalAlarmMeColors.current.surfaceContainerLowest)
                .padding(24.dp)
                .fillMaxWidth(),
        )
    }
}


@Composable
private fun PreviewHoldToDismissButton() {
    AlarmMeThemeProvider {
        HoldToDismissButton(
            modifier = Modifier
                .background(LocalAlarmMeColors.current.surfaceContainerLowest)
                .padding(24.dp)
                .fillMaxWidth(),
            label = "Hold to dismiss",
            onDismiss = {}
        )
    }
}
