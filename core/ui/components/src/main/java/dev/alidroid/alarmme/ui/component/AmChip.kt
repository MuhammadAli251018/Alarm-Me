package dev.alidroid.alarmme.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.alidroid.alarmme.ui.theme.AlarmMeThemeProvider
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeColors
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeShapes
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeTypography
import dev.alidroid.alarmme.utility.compose.modifyIfElse

@Composable
fun AmChip(
    text: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onToggle: () -> Unit
) {
    val colors = LocalAlarmMeColors.current
    val typography = LocalAlarmMeTypography.current
    val shapes = LocalAlarmMeShapes.current

    Box(
        modifier = modifier
            .height(48.dp)
            .clip(shapes.medium)
            .modifyIfElse(
                condition = selected,
                thenModifier = {
                    background(colors.primary)
                },
                otherwiseModifier = {
                    border(1.dp, colors.outlineVariant, shapes.medium)
                }
            )
            .toggleable(
                value = selected,
                enabled = enabled,
                onValueChange = { onToggle() }
            ),
        contentAlignment = Alignment.Center
    ) {
        val contentColor = if (selected) colors.onPrimary else colors.onSurfaceVariant

        Text(
            text = text,
            style = typography.titleMedium,
            color = contentColor
        )
    }
}


@Preview(name = "Light Mode--Horizontal", showBackground = true)
@Preview(
    name = "Dark Mode--Horizontal",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmChipHorizontalPreview() {
    val labels = listOf("1", "2", "3", "5", "∞")
    var selected by remember { mutableStateOf(labels[2]) }

    AlarmMeThemeProvider {
        Row(
            modifier = Modifier
                .background(LocalAlarmMeColors.current.background)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            labels.forEach { label ->
                AmChip(
                    modifier = Modifier.weight(1f),
                    text = label,
                    selected = selected == label,
                    enabled = true,
                    onToggle = { selected = label }
                )
            }
        }
    }
}