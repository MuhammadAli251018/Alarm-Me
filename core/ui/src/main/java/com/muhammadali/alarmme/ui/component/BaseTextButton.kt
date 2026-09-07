package com.muhammadali.alarmme.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.muhammadali.alarmme.ui.theme.AlarmMeThemeProvider
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeColors
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeShapes
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeTypography

/**
 * Shape, size and interaction behaviour shared by every text button variant.
 *
 * It resolves no colors of its own: [containerColor], [contentColor] and [pressColor] arrive
 * fully resolved, so the calling variant owns the enabled/disabled decision. [enabled] is used
 * only to gate the click, never to pick a color.
 *
 * Decoration drawn *outside* the container — selection outline, border — belongs to the variant
 * and should be applied to the [modifier] it passes down.
 *
 * @param pressColor drawn over the container while pressed; expected to carry its own alpha.
 */
@Composable
fun BaseTextButton(
    text: String,
    containerColor: Color,
    contentColor: Color,
    pressColor: Color,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onClick: () -> Unit
) {
    val indication = rememberTextButtonIndication(pressColor)

    Box(
        modifier = modifier
            .height(48.dp)
            .sizeIn(minWidth = 88.dp)
            .clip(shape = LocalAlarmMeShapes.current.full)
            .background(containerColor)
            .clickable(
                interactionSource = interactionSource,
                indication = indication,
                enabled = enabled,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 24.dp),
            text = text,
            style = LocalAlarmMeTypography.current.labelLarge,
            color = contentColor,
        )
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(
    name = "Dark Mode",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun BaseTextButtonPreview() {
    AlarmMeThemeProvider {
        val colors = LocalAlarmMeColors.current

        BaseTextButton(
            modifier = Modifier.padding(10.dp),
            text = "Save",
            containerColor = colors.secondaryContainer,
            contentColor = colors.onSecondaryContainer,
            pressColor = colors.onSecondaryContainer.copy(alpha = 0.12f),
            onClick = {}
        )
    }
}
