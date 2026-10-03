package dev.alidroid.alarmme.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.alidroid.alarmme.ui.theme.AlarmMeIcons
import dev.alidroid.alarmme.ui.theme.AlarmMeThemeProvider
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeColors
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeShapes
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeTypography

private const val CHECK_BOX_SIZE = 24

@Composable
fun AmCheckBox(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    AmCheckBoxBase(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
        content = { checked ->
            if (checked) {
                AmIcon(
                    painter = AlarmMeIcons.Check,
                    contentDescription = null,
                    size = AmIconDefaults.Small
                )
            }
        }
    )
}

@Composable
fun AmCheckBoxBase(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable BoxScope.(checked: Boolean) -> Unit = {

    }
) {
    val focusRequester = remember { FocusRequester() }
    val colors = LocalAlarmMeColors.current
    var selected by remember { mutableStateOf(false) }
    val outlineColor = when {
        checked -> Color.Transparent
        !checked && enabled -> colors.outline
        else -> colors.outlineVariant // Disabled
    }
    val containerColor = when {
        !checked -> Color.Transparent
        checked && enabled -> colors.primary
        else -> colors.outlineVariant
    }
    val contentColor = if (enabled) colors.onPrimary else colors.surface
    val glyphStyle = LocalAlarmMeTypography.current.labelMedium.copy(
        fontWeight  = FontWeight(700)
    )

    Box(
        modifier = modifier
            .minimumInteractiveComponentSize()
            .toggleable(
                value = checked,
                onValueChange = {
                    onCheckedChange?.invoke(it)
                    focusRequester.requestFocus()
                },
                enabled = enabled,
                role = null,
                interactionSource = interactionSource,
                indication = null
            )
            .size(CHECK_BOX_SIZE.dp)
            .onFocusChanged { selected = it.isFocused }
            .focusRequester(focusRequester)
            .focusable()
            .border(2.dp, outlineColor, LocalAlarmMeShapes.current.extraSmall)
            .selectedOutline(
                thickness = 2.dp,
                color = colors.primary,
                shape = LocalAlarmMeShapes.current.extraSmall,
                isSelected = { selected && checked }
            )
            .clip(LocalAlarmMeShapes.current.extraSmall)
            .background(containerColor),
        contentAlignment = Alignment.Center
    ) {
        CompositionLocalProvider(LocalContentColor provides contentColor) {
            ProvideTextStyle(glyphStyle) {
                content(checked)
            }
        }
    }
}

@Preview(name = "Light Mode--Unchecked", showBackground = true)
@Preview(
    name = "Dark Mode--Unchecked",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmCheckBoxUncheckedPreview() {
    var isChecked by remember { mutableStateOf(false) }

    AlarmMeThemeProvider {
        AmCheckBox(
            modifier = Modifier.padding(10.dp),
            checked = isChecked,
            onCheckedChange = { isChecked = it }
        )
    }
}

@Preview(name = "Light Mode--Checked", showBackground = true)
@Preview(
    name = "Dark Mode--Checked",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmCheckBoxCheckedPreview() {
    var isChecked by remember { mutableStateOf(true) }

    AlarmMeThemeProvider {
        AmCheckBox(
            modifier = Modifier.padding(10.dp),
            checked = isChecked,
            onCheckedChange = { isChecked = it }
        )
    }
}

@Preview(name = "Light Mode--Disabled Unchecked", showBackground = true)
@Preview(
    name = "Dark Mode--Disabled Unchecked",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmCheckBoxDisabledUncheckedPreview() {
    var isChecked by remember { mutableStateOf(false) }

    AlarmMeThemeProvider {
        AmCheckBox(
            modifier = Modifier.padding(10.dp),
            checked = isChecked,
            enabled = false,
            onCheckedChange = { isChecked = it }
        )
    }
}

@Preview(name = "Light Mode--Disabled Checked", showBackground = true)
@Preview(
    name = "Dark Mode--Disabled Checked",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmCheckBoxDisabledCheckedPreview() {
    var isChecked by remember { mutableStateOf(true) }

    AlarmMeThemeProvider {
        AmCheckBox(
            modifier = Modifier.padding(10.dp),
            checked = isChecked,
            enabled = false,
            onCheckedChange = { isChecked = it }
        )
    }
}
