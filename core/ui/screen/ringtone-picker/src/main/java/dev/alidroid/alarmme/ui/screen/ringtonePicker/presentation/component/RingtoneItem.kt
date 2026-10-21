package dev.alidroid.alarmme.ui.screen.ringtonePicker.presentation.component

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.alidroid.alarmme.ui.screen.ringtonePicker.R
import dev.alidroid.alarmme.ui.screen.ringtonePicker.presentation.model.RingtoneItemState
import dev.alidroid.alarmme.ui.component.AmBadge
import dev.alidroid.alarmme.ui.component.AmIcon
import dev.alidroid.alarmme.ui.component.AmIconDefaults
import dev.alidroid.alarmme.ui.component.AmRadioButton
import dev.alidroid.alarmme.ui.theme.AlarmMeIcons
import dev.alidroid.alarmme.ui.theme.AlarmMeThemeProvider
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeColors
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeShapes
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeTypography
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Composable
fun RingtoneItem(
    modifier: Modifier = Modifier,
    ringtone: RingtoneItemState,
    selected: Boolean,
    playing: Boolean,
    onClick: () -> Unit,
    onSelect: () -> Unit
) = RingtoneItem(
    modifier = modifier,
    title = ringtone.title,
    duration = ringtone.duration,
    selected = selected,
    playing = playing,
    onClick = onClick,
    onSelect = onSelect
)

@Composable
fun RingtoneItem(
    modifier: Modifier = Modifier,
    title: String,
    duration: Duration?,
    selected: Boolean,
    playing: Boolean,
    onClick: () -> Unit,
    onSelect: () -> Unit
) {
    val colors = LocalAlarmMeColors.current
    val typography = LocalAlarmMeTypography.current
    val shapes = LocalAlarmMeShapes.current

    val containerColor = when {
        !selected && !playing -> Color.Transparent
        else -> colors.surfaceContainer
    }

    val outlineColor by animateColorAsState(
        targetValue = if (selected) colors.primary else Color.Transparent,
        animationSpec = tween(durationMillis = 300),
        label = "outlineColor"
    )

    val outlineBorderWidth by animateDpAsState(
        targetValue = if (selected) 0.dp else 1.dp,
        animationSpec = tween(durationMillis = 300),
        label = "outlineBorder"
    )

    Box(
        modifier = modifier
            .height(80.dp)
            .fillMaxWidth()
            .border(outlineBorderWidth, outlineColor, shapes.large)
            .clip(shapes.large)
            .background(containerColor)
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.align(Alignment.CenterStart),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            AmRadioButton(
                selected = selected,
                onClick = onSelect
            )

            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = title,
                    style = typography.labelLarge,
                    color = colors.onSurface
                )

                val meta = getRingtoneMeta(duration)
                if (meta.isNotEmpty()) {
                    Text(
                        text = meta,
                        style = typography.bodySmall,
                        color = colors.onSurfaceVariant
                    )
                }
            }
        }

        Row(
            modifier = Modifier.align(Alignment.CenterEnd),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp, Alignment.End),
        ) {
            if (selected || playing) {
                AmBadge(
                    text = getRingtoneStatusBadge(playing, selected),
                    highlighted = playing
                )
            }

            AmIcon(
                painter = if (playing) AlarmMeIcons.Pause else AlarmMeIcons.Play,
                contentDescription = stringResource(
                    if (playing) R.string.ringtone_item_stop_preview
                    else R.string.ringtone_item_preview_sound
                ),
                size = AmIconDefaults.Small,
                tint = colors.primary
            )
        }
    }
}

@Composable
@ReadOnlyComposable
private fun getRingtoneStatusBadge(isPlaying: Boolean, selected: Boolean): String {
    return when {
        selected -> stringResource(R.string.ringtone_item_badge_selected)
        isPlaying -> stringResource(R.string.ringtone_item_badge_playing)
        else -> ""
    }
}

/** `"00:42"`, or empty when the sound's length isn't known. */
private fun getRingtoneMeta(duration: Duration?): String {
    if (duration == null) return ""

    val minutes = duration.inWholeMinutes.toString().padStart(2, '0')
    val seconds = (duration.inWholeSeconds % 60).toString().padStart(2, '0')
    return "$minutes:$seconds"
}

@Preview(name = "Light Mode--Idle", showBackground = true)
@Preview(
    name = "Dark Mode--Idle",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun RingtoneItemIdlePreview() {
    var isSelected by remember { mutableStateOf(false) }
    var isPlaying by remember { mutableStateOf(false) }

    AlarmMeThemeProvider {
        RingtoneItem(
            modifier = Modifier
                .background(LocalAlarmMeColors.current.surface)
                .padding(10.dp),
            title = "Chimes",
            duration = 45.seconds,
            selected = isSelected,
            playing = isPlaying,
            onClick = { isPlaying = !isPlaying },
            onSelect = { isSelected = !isSelected }
        )
    }
}

@Preview(name = "Light Mode--Playing", showBackground = true)
@Preview(
    name = "Dark Mode--Playing",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun RingtoneItemPlayingPreview() {
    var isSelected by remember { mutableStateOf(false) }
    var isPlaying by remember { mutableStateOf(true) }

    AlarmMeThemeProvider {
        RingtoneItem(
            modifier = Modifier
                .background(LocalAlarmMeColors.current.surface)
                .padding(10.dp),
            title = "Daybreak",
            duration = 32.seconds,
            selected = isSelected,
            playing = isPlaying,
            onClick = { isPlaying = !isPlaying },
            onSelect = { isSelected = !isSelected }
        )
    }
}

@Preview(name = "Light Mode--Selected", showBackground = true)
@Preview(
    name = "Dark Mode--Selected",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun RingtoneItemSelectedPreview() {
    var isSelected by remember { mutableStateOf(true) }
    var isPlaying by remember { mutableStateOf(false) }

    AlarmMeThemeProvider {
        RingtoneItem(
            modifier = Modifier
                .background(LocalAlarmMeColors.current.surface)
                .padding(10.dp),
            title = "Morning Glow",
            duration = 58.seconds,
            selected = isSelected,
            playing = isPlaying,
            onClick = { isPlaying = !isPlaying },
            onSelect = { isSelected = !isSelected }
        )
    }
}

@Preview(name = "Light Mode--Selected Playing", showBackground = true)
@Preview(
    name = "Dark Mode--Selected Playing",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun RingtoneItemSelectedPlayingPreview() {
    var isSelected by remember { mutableStateOf(true) }
    var isPlaying by remember { mutableStateOf(true) }

    AlarmMeThemeProvider {
        RingtoneItem(
            modifier = Modifier
                .background(LocalAlarmMeColors.current.surface)
                .padding(10.dp),
            title = "Kettle Drum",
            duration = 21.seconds,
            selected = isSelected,
            playing = isPlaying,
            onClick = { isPlaying = !isPlaying },
            onSelect = { isSelected = !isSelected }
        )
    }
}

/** No known length — the meta line is omitted rather than rendered empty. */
@Preview(name = "Light Mode--No Duration", showBackground = true)
@Preview(
    name = "Dark Mode--No Duration",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun RingtoneItemNoDurationPreview() {
    AlarmMeThemeProvider {
        RingtoneItem(
            modifier = Modifier
                .background(LocalAlarmMeColors.current.surface)
                .padding(10.dp),
            title = "Soft Piano",
            duration = null,
            selected = false,
            playing = false,
            onClick = {},
            onSelect = {}
        )
    }
}
