package dev.alidroid.alarmme.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.alidroid.alarmme.ui.theme.AlarmMeIcons
import dev.alidroid.alarmme.ui.theme.AlarmMeThemeProvider
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeColors
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeTypography

/**
 * The three icon sizes the design's per-slot glyph sizes collapse onto. There is no icon-size token
 * in the theme because the design carries none — each slot names its own glyph size instead.
 */
object AmIconDefaults {
    /** Row chevrons, chip affordances, the checkbox mark, the ringtone preview control. */
    val Small: Dp = 16.dp

    /** Icon buttons (a 44dp well in a 48dp target), month-nav wells, the extended FAB's leading slot. */
    val Medium: Dp = 20.dp

    /** The bare 64dp FAB and the large top bar's back arrow. */
    val Large: Dp = 24.dp
}


@Composable
fun AmIcon(
    painter: Painter,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    size: Dp = AmIconDefaults.Medium,
    tint: Color = LocalContentColor.current,
) {
    Icon(
        painter = painter,
        contentDescription = contentDescription,
        modifier = modifier.size(size),
        tint = tint
    )
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(
    name = "Dark Mode",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmIconCatalogPreview() {
    AlarmMeThemeProvider {
        val colors = LocalAlarmMeColors.current

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val icons: List<Pair<String, Painter>> = listOf(
                "back" to AlarmMeIcons.ArrowBack,
                "close" to AlarmMeIcons.Close,
                "chevron right" to AlarmMeIcons.ChevronRight,
                "chevron left" to AlarmMeIcons.ChevronLeft,
                "add" to AlarmMeIcons.Add,
                "check" to AlarmMeIcons.Check,
                "play" to AlarmMeIcons.Play,
                "pause" to AlarmMeIcons.Pause,
                "overflow" to AlarmMeIcons.MoreVert
            )

            icons.forEach { (name, painter) ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        modifier = Modifier.size(width = 76.dp, height = 20.dp),
                        text = name,
                        style = LocalAlarmMeTypography.current.labelMedium,
                        color = colors.onSurfaceVariant
                    )
                    AmIcon(painter, null, size = AmIconDefaults.Small, tint = colors.onSurface)
                    AmIcon(painter, null, size = AmIconDefaults.Medium, tint = colors.onSurface)
                    AmIcon(painter, null, size = AmIconDefaults.Large, tint = colors.onSurface)
                    AmIcon(painter, null, size = AmIconDefaults.Large, tint = colors.primary)
                }
            }
        }
    }
}
