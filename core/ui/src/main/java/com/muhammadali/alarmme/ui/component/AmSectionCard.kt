package com.muhammadali.alarmme.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.muhammadali.alarmme.ui.theme.AlarmMeThemeProvider
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeColors
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeShapes
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeTypography

@Composable
fun AmSectionCard(
    modifier: Modifier,
    shape: Shape = LocalAlarmMeShapes.current.small,
    content: @Composable BoxScope.() -> Unit
) {
    val backgroundColor = LocalAlarmMeColors.current.surfaceContainer

    Box(
        modifier = modifier.clip(shape).background(backgroundColor),
        content = content
    )
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(
    name = "Dark Mode",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmSectionCardPreview() {
    AlarmMeThemeProvider {
        AmSectionCard(modifier = Modifier.padding(10.dp)) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = "Section Card",
                color = LocalAlarmMeColors.current.onSurface,
                style = LocalAlarmMeTypography.current.bodyMedium
            )
        }
    }
}
