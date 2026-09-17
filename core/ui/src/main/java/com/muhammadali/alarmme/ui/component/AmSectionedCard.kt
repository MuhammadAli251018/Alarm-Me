package com.muhammadali.alarmme.ui.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.muhammadali.alarmme.ui.theme.AlarmMeThemeProvider
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeColors
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeShapes
import com.muhammadali.alarmme.utility.compose.modifyIf

@Composable
fun AmSectionedCard(
    modifier: Modifier = Modifier,
    sectionsCount: Int,
    section: @Composable (index: Int) -> Unit
) {
    val colors = LocalAlarmMeColors.current
    val boarderColor = colors.outline.copy(0.5f)

    AmSectionCard(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, boarderColor, LocalAlarmMeShapes.current.large),
        shape = LocalAlarmMeShapes.current.large
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            (0 until sectionsCount).forEach { index ->
                Box(
                    Modifier
                        .fillMaxWidth()
                        .modifyIf(index > 0) {
                            drawBehind {
                                drawLine(
                                    color = boarderColor,
                                    start = Offset(0f, 0f),
                                    end = Offset(size.width, 0f),
                                    strokeWidth = 1f
                                )
                            }
                        }
                ){
                    section(index)
                }
            }
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(
    name = "Dark Mode",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmSectionedCardPreview() {
    AlarmMeThemeProvider {
        var gradualVolumeEnabled by remember { mutableStateOf(true) }
        var vibrateEnabled by remember { mutableStateOf(false) }

        AmSectionedCard(
            modifier = Modifier.padding(16.dp),
            sectionsCount = 5
        ) { index ->
            when (index) {
                0 -> AmNavigationDisclosureRow(
                    text = "Sound",
                    value = "Sunrise chime",
                    valueColor = LocalAlarmMeColors.current.primary,
                    onClick = {}
                )

                1 -> AmNavigationDisclosureRow(
                    text = "Snooze",
                    value = "9 min · 3 times",
                    onClick = {}
                )

                2 -> AmToggleDisclosureRow(
                    text = "Gradual volume",
                    checked = gradualVolumeEnabled,
                    onCheckedChange = { gradualVolumeEnabled = it }
                )

                3 -> AmToggleDisclosureRow(
                    text = "Vibrate",
                    checked = vibrateEnabled,
                    onCheckedChange = { vibrateEnabled = it }
                )

                else -> AmNavigationDisclosureRow(
                    text = "Label",
                    value = "Weekday mornings",
                    onClick = {}
                )
            }
        }
    }
}