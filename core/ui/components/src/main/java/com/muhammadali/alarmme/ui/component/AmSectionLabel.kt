package com.muhammadali.alarmme.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.muhammadali.alarmme.ui.theme.AlarmMeThemeProvider
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeColors
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeTypography

/** A mono, uppercase group heading above a section's card ("FEATURE FLAGS", "TRIGGERS", …). */
@Composable
fun AmSectionLabel(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = LocalAlarmMeColors.current.outline,
) {
    Text(
        modifier = modifier.padding(horizontal = 4.dp, vertical = 8.dp),
        text = text.uppercase(),
        style = LocalAlarmMeTypography.current.labelSmall,
        color = color
    )
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(
    name = "Dark Mode",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmSectionLabelPreview() {
    AlarmMeThemeProvider {
        AmSectionLabel(text = "Feature flags")
    }
}
