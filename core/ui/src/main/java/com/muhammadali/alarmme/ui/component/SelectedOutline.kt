package com.muhammadali.alarmme.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.muhammadali.alarmme.ui.theme.AlarmMeThemeProvider
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeColors
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeShapes

private const val OutlineAlpha = 0.22f

fun Modifier.selectedOutline(
    color: Color,
    shape: Shape,
    thickness: Dp = 4.dp,
    isSelected: () -> Boolean
): Modifier = drawWithCache {
    val stroke = thickness.toPx()
    val half = stroke / 2f
    val outline = shape.createOutline(
        Size(size.width + stroke, size.height + stroke),
        layoutDirection,
        this
    )
    val style = Stroke(width = stroke)
    val outlineColor = color.copy(alpha = OutlineAlpha)

    onDrawBehind {
        if (isSelected()) {
            translate(-half, -half) {
                drawOutline(outline, color = outlineColor, style = style)
            }
        }
    }
}

@Preview(name = "Light Mode")
@Preview(
    name = "Dark Mode",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun SelectedOutlinePreview() {
    AlarmMeThemeProvider {
        Column(
            modifier = Modifier
                .background(LocalAlarmMeColors.current.background)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AmFilledTextButton(
                modifier = Modifier.selectedOutline(
                    shape = LocalAlarmMeShapes.current.full,
                    color = LocalAlarmMeColors.current.primary,
                    isSelected = { true }
                ),
                text = "Selected",
                onClick = {}
            )

            AmFilledTextButton(
                modifier = Modifier.selectedOutline(
                    shape = LocalAlarmMeShapes.current.full,
                    color = LocalAlarmMeColors.current.primary,
                    isSelected = { false }
                ),
                text = "Unselected",
                onClick = {}
            )
        }
    }
}
