package com.muhammadali.alarmme.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.muhammadali.alarmme.ui.theme.AlarmMeThemeProvider
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeColors
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeTypography
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

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(AmDisclosureRowDefaults.Height)
            .modifyIf(onClick != null) {
                clickable(onClick = onClick ?: {}, enabled = enabled)
            }
            .padding(horizontal = AmDisclosureRowDefaults.HorizontalPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val contentColor by animateColorAsState(
            targetValue = if (enabled) colors.onSurface else colors.outline,
            animationSpec = tween(300),
            label = "contentColor"
        )

        Text(
            modifier = Modifier.sizeIn(maxWidth = 160.dp),
            text = text,
            style = typography.bodyLarge,
            color = contentColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(Modifier.weight(1f))

        trailingContent()
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
                Text(
                    text = "›",
                    style = LocalAlarmMeTypography.current.bodyLarge,
                    color = colors.onSurface
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
                Text(
                    text = "›",
                    style = LocalAlarmMeTypography.current.bodyLarge,
                    color = colors.onSurfaceVariant
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

            Text(
                text = "›",
                style = typography.bodyLarge,
                color = colors.onSurfaceVariant
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
