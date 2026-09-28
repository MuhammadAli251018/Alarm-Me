package com.muhammadali.alarmme.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.muhammadali.alarmme.ui.theme.AlarmMeThemeProvider
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeColors

/** An [AmSectionLabel] paired with the [AmSectionedCard] it describes. */
@Composable
fun AmLabeledSectionCard(
    label: String,
    modifier: Modifier = Modifier,
    labelColor: Color = LocalAlarmMeColors.current.outline,
    isDangerSection: Boolean = false,
    content: AmSectionedCardScope.() -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        AmSectionLabel(text = label, color = labelColor)
        AmSectionedCard(isDangerSection = isDangerSection, content = content)
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(
    name = "Dark Mode",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmLabeledSectionCardPreview() {
    AlarmMeThemeProvider {
        var gradualVolumeEnabled by remember { mutableStateOf(true) }
        var vibrateEnabled by remember { mutableStateOf(false) }

        AmLabeledSectionCard(
            modifier = Modifier.padding(16.dp),
            label = "Alarm settings"
        ) {
            sections(count = 4) { index ->
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

                    else -> AmToggleDisclosureRow(
                        text = "Vibrate",
                        checked = vibrateEnabled,
                        onCheckedChange = { vibrateEnabled = it }
                    )
                }
            }
        }
    }
}

@Preview(name = "Light Mode--Danger", showBackground = true)
@Preview(
    name = "Dark Mode--Danger",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmLabeledSectionCardDangerPreview() {
    AlarmMeThemeProvider {
        val colors = LocalAlarmMeColors.current

        AmLabeledSectionCard(
            modifier = Modifier.padding(16.dp),
            label = "Danger zone",
            labelColor = colors.error,
            isDangerSection = true
        ) {
            sections(count = 2) { index ->
                when (index) {
                    0 -> AmTwoLineDisclosureRow(
                        title = "Flush alarm database",
                        subtitle = "Deletes every alarm"
                    ) {
                        AmDestructiveTextButton(text = "Flush", onClick = {})
                    }

                    else -> AmTwoLineDisclosureRow(
                        title = "Reset onboarding",
                        subtitle = "Show first-run flow again"
                    ) {
                        AmDestructiveTextButton(text = "Reset", onClick = {})
                    }
                }
            }
        }
    }
}
