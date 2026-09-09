package com.muhammadali.alarmme.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.muhammadali.alarmme.ui.theme.AlarmMeThemeProvider
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeColors
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeShapes
import com.muhammadali.alarmme.ui.theme.PlexMono

private val IconButtonTargetSize = 48.dp
private val IconButtonVisualSize = 44.dp
private val IconButtonShape = RoundedCornerShape(14.dp)

/**
 * The family lacks a 400 weight, so the spec'd Plex Mono 400 resolves to the nearest available
 * step rather than being synthesised.
 */
private val IconButtonGlyphStyle = TextStyle(
    fontFamily = PlexMono,
    fontWeight = FontWeight.Medium,
    fontSize = 18.sp,
    lineHeight = 18.sp
)


@Composable
fun AmGhostIconButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    selected: Boolean = false,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    val colors = LocalAlarmMeColors.current
    val pressed by interactionSource.collectIsPressedAsState()

    val containerColor = Color.Transparent
    val contentColor = when {
        !enabled -> colors.onSurface.copy(alpha = DisabledContentAlpha)
        else -> colors.onSurfaceVariant
    }

    Box(
        modifier = modifier
            .size(IconButtonTargetSize)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                role = Role.Button,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .selectedOutline(
                    color = colors.primary,
                    shape = IconButtonShape,
                    isSelected = { selected }
                )
                .size(IconButtonVisualSize)
                .dropShadow(LocalAlarmMeShapes.current.medium) {
                    color = if (pressed) Color.Black.copy(0.15f) else Color.Transparent
                }
                .clip(IconButtonShape)
                .background(containerColor),
            contentAlignment = Alignment.Center
        ) {
            CompositionLocalProvider(LocalContentColor provides contentColor) {
                ProvideTextStyle(IconButtonGlyphStyle) {
                    content()
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
private fun AmGhostIconButtonPreview() {
    AlarmMeThemeProvider {
        AmGhostIconButton(
            modifier = Modifier.padding(10.dp),
            onClick = {}
        ) {
            Text(text = "⋮")
        }
    }
}

@Preview(name = "Light Mode--Pressed", showBackground = true)
@Preview(
    name = "Dark Mode--Pressed",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmGhostIconButtonPressedPreview() {
    AlarmMeThemeProvider {
        AmGhostIconButton(
            modifier = Modifier.padding(10.dp),
            interactionSource = rememberPressedInteractionSource(),
            onClick = {}
        ) {
            Text(text = "⋮")
        }
    }
}

@Preview(name = "Light Mode--Disabled", showBackground = true)
@Preview(
    name = "Dark Mode--Disabled",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmGhostIconButtonDisabledPreview() {
    AlarmMeThemeProvider {
        AmGhostIconButton(
            modifier = Modifier.padding(10.dp),
            enabled = false,
            onClick = {}
        ) {
            Text(text = "⋮")
        }
    }
}
