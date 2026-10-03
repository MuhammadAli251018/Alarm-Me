package dev.alidroid.alarmme.ui.component

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.alidroid.alarmme.ui.theme.AlarmMeIcons
import dev.alidroid.alarmme.ui.theme.AlarmMeThemeProvider
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeColors
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeTypography

internal val IconButtonTargetSize = 48.dp
internal val IconButtonVisualSize = 44.dp
internal val IconButtonShape = RoundedCornerShape(14.dp)

@Composable
internal fun BaseIconButton(
    colors: AmButtonColors,
    glyphStyle: TextStyle,
    modifier: Modifier = Modifier,
    shape: Shape = IconButtonShape,
    enabled: Boolean = true,
    selected: Boolean = false,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    val alarmColors = LocalAlarmMeColors.current
    val containerColor = if (enabled) colors.containerColor else colors.disabledContainerColor
    val contentColor = if (enabled) colors.contentColor else colors.disabledContentColor
    val pressed by interactionSource.collectIsPressedAsState()

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
                    color = alarmColors.primary,
                    shape = shape,
                    isSelected = { selected }
                )
                .size(IconButtonVisualSize)
                .dropShadow(shape) {
                    color = if (pressed) Color.Black.copy(0.15f) else Color.Transparent
                }
                .clip(shape)
                .background(containerColor),
            contentAlignment = Alignment.Center
        ) {
            CompositionLocalProvider(LocalContentColor provides contentColor) {
                ProvideTextStyle(glyphStyle) {
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
private fun BaseIconButtonPreview() {
    AlarmMeThemeProvider {
        val colors = LocalAlarmMeColors.current

        BaseIconButton(
            modifier = Modifier.padding(10.dp),
            colors = AmButtonColors.solid(colors.surface, colors.onSurfaceVariant),
            glyphStyle = LocalAlarmMeTypography.current.bodyLarge,
            onClick = {}
        ) {
            AmIcon(AlarmMeIcons.MoreVert, contentDescription = null)
        }
    }
}
