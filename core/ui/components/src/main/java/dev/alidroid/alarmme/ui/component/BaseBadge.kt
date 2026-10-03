package dev.alidroid.alarmme.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.alidroid.alarmme.ui.theme.AlarmMeThemeProvider
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeColors
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeShapes
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeTypography

private val BadgeHeight = 28.dp
private val BadgeHorizontalPadding = 8.dp
private val BadgeBorderWidth = 1.dp

@Composable
internal fun BaseBadge(
    text: String,
    colors: AmBadgeColors,
    modifier: Modifier = Modifier
) {
    val shape = LocalAlarmMeShapes.current.extraSmall

    Box(
        modifier = modifier
            .height(BadgeHeight)
            .border(BadgeBorderWidth, colors.borderColor, shape)
            .clip(shape)
            .background(colors.containerColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(horizontal = BadgeHorizontalPadding),
            text = text,
            style = LocalAlarmMeTypography.current.labelSmall,
            color = colors.contentColor,
        )
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(
    name = "Dark Mode",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun BaseBadgePreview() {
    AlarmMeThemeProvider {
        val colors = LocalAlarmMeColors.current

        BaseBadge(
            modifier = Modifier.padding(10.dp),
            text = "OFF",
            colors = AmBadgeColors(
                containerColor = Color.Transparent,
                contentColor = colors.onSurfaceVariant,
                borderColor = colors.outlineVariant
            )
        )
    }
}
