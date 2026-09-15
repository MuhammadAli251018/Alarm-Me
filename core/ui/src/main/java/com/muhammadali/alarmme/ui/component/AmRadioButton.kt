package com.muhammadali.alarmme.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.muhammadali.alarmme.ui.theme.AlarmMeThemeProvider
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeColors
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeShapes

private val RadioSize = 24.dp
private val RadioDotSize = 6.dp
private val RadioStrokeWidth = 6.dp


@Composable
fun AmRadioButton(
    selected: Boolean,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    val colors = LocalAlarmMeColors.current
    val shape = LocalAlarmMeShapes.current.full
    var focused by remember { mutableStateOf(false) }

    val indicatorColor = when {
        !enabled -> colors.outlineVariant
        selected -> colors.primary
        else -> colors.outline
    }

    val selectableModifier = if (onClick != null) {
        Modifier.selectable(
            selected = selected,
            enabled = enabled,
            role = Role.RadioButton,
            interactionSource = interactionSource,
            indication = null,
            onClick = onClick
        )
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .minimumInteractiveComponentSize()
            .then(selectableModifier)
            .size(RadioSize)
            .onFocusChanged { focused = it.isFocused }
            .selectedOutline(
                color = colors.primary,
                shape = shape,
                isSelected = { focused }
            )
            .border(RadioStrokeWidth, indicatorColor, shape),
        contentAlignment = Alignment.Center
    ) {
        if (selected) {
            Box(
                modifier = Modifier
                    .size(RadioDotSize)
                    .clip(shape)
                    .background(indicatorColor)
            )
        }
    }
}

@Preview(name = "Light Mode--Unselected", showBackground = true)
@Preview(
    name = "Dark Mode--Unselected",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmRadioButtonUnselectedPreview() {
    var isSelected by remember { mutableStateOf(false) }

    AlarmMeThemeProvider {
        AmRadioButton(
            modifier = Modifier.padding(10.dp),
            selected = isSelected,
            onClick = { isSelected = true }
        )
    }
}

@Preview(name = "Light Mode--Selected", showBackground = true)
@Preview(
    name = "Dark Mode--Selected",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmRadioButtonSelectedPreview() {
    var isSelected by remember { mutableStateOf(true) }

    AlarmMeThemeProvider {
        AmRadioButton(
            modifier = Modifier.padding(10.dp),
            selected = isSelected,
            onClick = { isSelected = true }
        )
    }
}

@Preview(name = "Light Mode--Disabled Unselected", showBackground = true)
@Preview(
    name = "Dark Mode--Disabled Unselected",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmRadioButtonDisabledUnselectedPreview() {
    var isSelected by remember { mutableStateOf(false) }

    AlarmMeThemeProvider {
        AmRadioButton(
            modifier = Modifier.padding(10.dp),
            selected = isSelected,
            enabled = false,
            onClick = { isSelected = true }
        )
    }
}

@Preview(name = "Light Mode--Disabled Selected", showBackground = true)
@Preview(
    name = "Dark Mode--Disabled Selected",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmRadioButtonDisabledSelectedPreview() {
    var isSelected by remember { mutableStateOf(true) }

    AlarmMeThemeProvider {
        AmRadioButton(
            modifier = Modifier.padding(10.dp),
            selected = isSelected,
            enabled = false,
            onClick = { isSelected = true }
        )
    }
}
