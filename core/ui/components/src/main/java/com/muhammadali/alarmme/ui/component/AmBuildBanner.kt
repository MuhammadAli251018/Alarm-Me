package com.muhammadali.alarmme.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.muhammadali.alarmme.ui.theme.AlarmMeThemeProvider
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeColors
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeShapes
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeTypography
import com.muhammadali.alarmme.ui.theme.PlexMono

/** A debug/internal-build indicator strip — pinned to the top of debug-only screens. */
@Composable
fun AmBuildBanner(
    details: String,
    modifier: Modifier = Modifier,
    eyebrow: String = "DEBUG BUILD",
) {
    val colors = LocalAlarmMeColors.current
    val typography = LocalAlarmMeTypography.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(LocalAlarmMeShapes.current.medium)
            .background(colors.surfaceContainer)
            .border(1.dp, colors.primaryContainer, LocalAlarmMeShapes.current.medium)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(colors.primary)
        )

        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Text(
                text = eyebrow,
                style = typography.labelSmall,
                color = colors.primary
            )
            Text(
                text = details,
                style = typography.labelMedium.copy(fontFamily = PlexMono),
                color = colors.onSurfaceVariant
            )
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(
    name = "Dark Mode",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmBuildBannerPreview() {
    AlarmMeThemeProvider {
        AmBuildBanner(
            modifier = Modifier.padding(10.dp),
            details = "v1.0.0-debug · build 142 · a3f9c21"
        )
    }
}
