package com.muhammadali.alarmme.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.muhammadali.alarmme.ui.theme.AlarmMeThemeProvider
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeColors


@Composable
fun BaseFabButton(
    containerColor: Color,
    contentColor: Color,
    shape: Shape,
    glyphStyle: TextStyle,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    selected: Boolean = false,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    val pressed by interactionSource.collectIsPressedAsState()

    Box(
        modifier = modifier
            .selectedOutline(
                color = LocalAlarmMeColors.current.primary,
                shape = shape,
                isSelected = { selected }
            )
            .clip(shape)
            .background(containerColor)
            .dropShadow(shape) {
                color = if (pressed) Color.Black.copy(0.5f) else Color.Transparent
            }
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        CompositionLocalProvider(LocalContentColor provides contentColor) {
            ProvideTextStyle(glyphStyle) {
                content()
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
private fun BaseFabButtonPreview() {
    AlarmMeThemeProvider {
        val colors = LocalAlarmMeColors.current

        BaseFabButton(
            modifier = Modifier.size(64.dp),
            containerColor = colors.primary,
            contentColor = colors.onPrimary,
            shape = FabShape,
            glyphStyle = FabGlyphStyle,
            onClick = {}
        ) {
            Text(text = "＋")
        }
    }
}

@Preview(name = "Light Mode--Pressed", showBackground = true)
@Preview(
    name = "Dark Mode--Pressed",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun BaseFabButtonPressedPreview() {
    AlarmMeThemeProvider {
        val colors = LocalAlarmMeColors.current

        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // no pressColor: pressed shows as a drop shadow
            BaseFabButton(
                modifier = Modifier.size(64.dp),
                containerColor = colors.primary,
                contentColor = colors.onPrimary,
                shape = FabShape,
                glyphStyle = FabGlyphStyle,
                interactionSource = rememberPressedInteractionSource(),
                onClick = {}
            ) {
                Text(text = "＋")
            }
            // explicit pressColor: pressed shows as an overlay tint
            BaseFabButton(
                modifier = Modifier.size(64.dp),
                containerColor = colors.primary,
                contentColor = colors.onPrimary,
                shape = FabShape,
                glyphStyle = FabGlyphStyle,
                interactionSource = rememberPressedInteractionSource(),
                onClick = {}
            ) {
                Text(text = "＋")
            }
        }
    }
}
