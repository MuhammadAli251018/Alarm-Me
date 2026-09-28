package com.muhammadali.alarmme.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.muhammadali.alarmme.ui.theme.AlarmMeIcons
import com.muhammadali.alarmme.ui.theme.AlarmMeThemeProvider
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeColors
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeTypography
import com.muhammadali.alarmme.ui.theme.PlexMono
import com.muhammadali.alarmme.ui.theme.WorkSans
import com.muhammadali.alarmme.utility.compose.modifyIf

/** The row's default height, exposed so placeholders can reserve the same box. */
object AmDisclosureRowDefaults {
    val Height: Dp = 60.dp
    val HorizontalPadding: Dp = 20.dp
}

@Composable
fun AmDisclosureRow(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null,
    trailingContent: @Composable RowScope.() -> Unit,
) {
    val colors = LocalAlarmMeColors.current
    val typography = LocalAlarmMeTypography.current

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(AmDisclosureRowDefaults.Height)
            .modifyIf(onClick != null) {
                clickable(onClick = onClick ?: {}, enabled = enabled)
            }
            .padding(horizontal = AmDisclosureRowDefaults.HorizontalPadding)
    ) {
        val contentColor by animateColorAsState(
            targetValue = if (enabled) colors.onSurface else colors.outline,
            animationSpec = tween(300),
            label = "contentColor"
        )

        Text(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .sizeIn(maxWidth = minWidth * 0.5f),
            text = text,
            style = typography.bodyLarge,
            color = contentColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )


        Row(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(start = 10.dp)
                .widthIn(max = minWidth * 0.4f)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            trailingContent()
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(
    name = "Dark Mode",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmDisclosureRowPreview() {
    AlarmMeThemeProvider {
        val colors = LocalAlarmMeColors.current

        AmDisclosureRow(
            text = "Sound",
            onClick = {}
        ) {

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Sunrise chime",
                    style = LocalAlarmMeTypography.current.bodyMedium,
                    color = colors.primary
                )
                AmIcon(
                    painter = AlarmMeIcons.ChevronRight,
                    contentDescription = null,
                    size = AmIconDefaults.Small,
                    tint = colors.onSurface
                )
            }
        }
    }
}

@Preview(name = "Light Mode--With Chevron", showBackground = true)
@Preview(
    name = "Dark Mode--With Chevron",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmDisclosureRowWithChevronPreview() {
    AlarmMeThemeProvider {
        val colors = LocalAlarmMeColors.current

        AmDisclosureRow(
            text = "Sep 20, 24, 28",
            onClick = {}
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "9 min · 3 times",
                    style = LocalAlarmMeTypography.current.bodyMedium,
                    color = colors.onSurfaceVariant
                )
                AmIcon(
                    painter = AlarmMeIcons.ChevronRight,
                    contentDescription = null,
                    size = AmIconDefaults.Small,
                    tint = colors.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun AmNavigationDisclosureRow(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    value: String? = null,
    valueColor: Color = LocalAlarmMeColors.current.onSurfaceVariant,
) {
    val colors = LocalAlarmMeColors.current
    val typography = LocalAlarmMeTypography.current

    AmDisclosureRow(
        text = text,
        modifier = modifier,
        onClick = onClick
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (value != null) {
                Text(
                    text = value,
                    style = typography.bodyMedium,
                    color = valueColor
                )
            }

            AmIcon(
                painter = AlarmMeIcons.ChevronRight,
                contentDescription = null,
                size = AmIconDefaults.Small,
                tint = colors.onSurfaceVariant
            )
        }
    }
}

@Composable
fun AmToggleDisclosureRow(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    AmDisclosureRow(
        text = text,
        enabled = checked,
        modifier = modifier
    ) {
        AmSwitchToggle(
            checked = checked,
            enabled = enabled,
            onCheckedChange = onCheckedChange
        )
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(
    name = "Dark Mode",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmNavigationDisclosureRowPreview() {
    AlarmMeThemeProvider {
        AmNavigationDisclosureRow(
            text = "Sound",
            value = "Sunrise chime",
            valueColor = LocalAlarmMeColors.current.primary,
            onClick = {}
        )
    }
}

@Preview(name = "Light Mode--No Value", showBackground = true)
@Preview(
    name = "Dark Mode--No Value",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmNavigationDisclosureRowNoValuePreview() {
    AlarmMeThemeProvider {
        AmNavigationDisclosureRow(
            text = "Label",
            onClick = {}
        )
    }
}

@Preview(name = "Light Mode--Checked", showBackground = true)
@Preview(
    name = "Dark Mode--Checked",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmToggleDisclosureRowCheckedPreview() {
    var isChecked by remember { mutableStateOf(true) }

    AlarmMeThemeProvider {
        AmToggleDisclosureRow(
            text = "Gradual volume",
            checked = isChecked,
            onCheckedChange = { isChecked = it }
        )
    }
}

@Preview(name = "Light Mode--Unchecked", showBackground = true)
@Preview(
    name = "Dark Mode--Unchecked",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmToggleDisclosureRowUncheckedPreview() {
    var isChecked by remember { mutableStateOf(false) }

    AlarmMeThemeProvider {
        AmToggleDisclosureRow(
            text = "Vibrate",
            checked = isChecked,
            onCheckedChange = { isChecked = it }
        )
    }
}

/** The two-line row's minimum height, exposed so placeholders can reserve the same box. */
object AmTwoLineDisclosureRowDefaults {
    val MinHeight: Dp = 68.dp
}

/**
 * A two-line variant of [AmDisclosureRow], for rows whose label needs a second line of context
 * (a flag's key, a danger action's consequence) rather than a single-line title.
 */
@Composable
fun AmTwoLineDisclosureRow(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    subtitleColor: Color = LocalAlarmMeColors.current.onSurfaceVariant,
    subtitleFontFamily: FontFamily = WorkSans,
    trailingContent: @Composable RowScope.() -> Unit,
) {
    val colors = LocalAlarmMeColors.current
    val typography = LocalAlarmMeTypography.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = AmTwoLineDisclosureRowDefaults.MinHeight)
            .padding(horizontal = AmDisclosureRowDefaults.HorizontalPadding, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val contentColor by animateColorAsState(
            targetValue = if (enabled) colors.onSurface else colors.outline,
            animationSpec = tween(300),
            label = "contentColor"
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = title,
                style = typography.bodyLarge,
                color = contentColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = subtitle,
                style = typography.bodySmall.copy(fontFamily = subtitleFontFamily),
                color = subtitleColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(Modifier.width(12.dp))

        trailingContent()
    }
}

@Composable
fun AmFlagDisclosureRow(
    text: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    AmTwoLineDisclosureRow(
        title = text,
        subtitle = description,
        modifier = modifier,
        enabled = checked,
        subtitleColor = LocalAlarmMeColors.current.outline,
        subtitleFontFamily = PlexMono,
    ) {
        AmSwitchToggle(
            checked = checked,
            enabled = enabled,
            onCheckedChange = onCheckedChange
        )
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(
    name = "Dark Mode",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmTwoLineDisclosureRowPreview() {
    AlarmMeThemeProvider {
        AmTwoLineDisclosureRow(
            title = "Flush alarm database",
            subtitle = "Deletes every alarm",
        ) {
            AmDestructiveTextButton(text = "Flush", onClick = {})
        }
    }
}

@Preview(name = "Light Mode--Checked", showBackground = true)
@Preview(
    name = "Dark Mode--Checked",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmFlagDisclosureRowCheckedPreview() {
    var isChecked by remember { mutableStateOf(true) }

    AlarmMeThemeProvider {
        AmFlagDisclosureRow(
            text = "Custom-date schedule",
            description = "flag.custom_dates",
            checked = isChecked,
            onCheckedChange = { isChecked = it }
        )
    }
}

@Preview(name = "Light Mode--Unchecked", showBackground = true)
@Preview(
    name = "Dark Mode--Unchecked",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmFlagDisclosureRowUncheckedPreview() {
    var isChecked by remember { mutableStateOf(false) }

    AlarmMeThemeProvider {
        AmFlagDisclosureRow(
            text = "New ringtone picker",
            description = "flag.picker_v2",
            checked = isChecked,
            onCheckedChange = { isChecked = it }
        )
    }
}
