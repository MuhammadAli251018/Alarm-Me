package dev.alidroid.alarmme.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.alidroid.alarmme.ui.theme.AlarmMeColorScheme
import dev.alidroid.alarmme.ui.theme.AlarmMeIcons
import dev.alidroid.alarmme.ui.theme.AlarmMeShapes
import dev.alidroid.alarmme.ui.theme.AlarmMeThemeProvider
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeColors
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeShapes
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeTypography

private const val MAX_CHAR_COUNT = 30

@Composable
fun AmHeroTextField(
    value: String,
    label: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    maxCharCount: Int = MAX_CHAR_COUNT,
) {
    val typography = LocalAlarmMeTypography.current
    val colors = LocalAlarmMeColors.current
    val shapes = LocalAlarmMeShapes.current
    val limit = maxCharCount.coerceIn(0 .. MAX_CHAR_COUNT)
    val displayValue = value.take(limit)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Box (Modifier.fillMaxWidth()) {
            Text(
                modifier = Modifier.align(Alignment.CenterStart),
                text = label,
                style = typography.titleMedium,
                color = colors.onSurface,
            )

            Text(
                modifier = Modifier.align(Alignment.CenterEnd),
                text = "${displayValue.length}/$limit",
                style = typography.labelMedium,
                color = colors.onSurfaceVariant,
            )
        }

        val textFieldStyle = typography.titleLarge.copy(
            color = colors.onSurface,
            fontSize = 19.sp
        )

        BasicTextField(
            modifier = Modifier.fillMaxWidth(),
            value = displayValue,
            onValueChange = { onValueChange(it.take(limit)) },
            textStyle = textFieldStyle,
            cursorBrush = SolidColor(colors.primary),
            singleLine = true,
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .drawDecorationBox(shapes, colors)
                        .heightIn(min = 60.dp)
                        .padding(horizontal = 15.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(Modifier.weight(1f)) {
                        if (displayValue.isEmpty()) {
                            Text(
                                text = placeholder,
                                style = textFieldStyle,
                                color = colors.onSurfaceVariant
                            )
                        }

                        innerTextField()
                    }

                    if (displayValue.isNotEmpty()) {
                        AmIcon(
                            modifier = Modifier.clickable(
                                onClick = { onValueChange("") },
                                interactionSource = remember { MutableInteractionSource() }
                            ),
                            painter = AlarmMeIcons.Close,
                            contentDescription = "Clear",
                            tint = colors.onSurfaceVariant
                        )
                    }
                }
            }
        )
    }
}

private fun Modifier.drawDecorationBox(shapes: AlarmMeShapes, colors: AlarmMeColorScheme): Modifier =
    this
        .background(colors.primary, shapes.medium)
        .padding(bottom = 3.dp)
        .background(colors.surfaceContainerHigh, shapes.medium)

@Preview(name = "Light Mode--Empty", showBackground = true)
@Preview(
    name = "Dark Mode--Empty",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmHeroTextFieldEmptyPreview() {
    var text by remember { mutableStateOf("") }

    AlarmMeThemeProvider {
        val colors = LocalAlarmMeColors.current

        AmHeroTextField(
            modifier = Modifier
                .background(colors.surface)
                .padding(10.dp),
            value = text,
            label = "Label",
            placeholder = "What is it for?",
            onValueChange = { text = it },
            maxCharCount = 30
        )
    }
}

@Preview(name = "Light Mode--Filled", showBackground = true)
@Preview(
    name = "Dark Mode--Filled",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmHeroTextFieldFilledPreview() {
    var text by remember { mutableStateOf("Gym") }

    AlarmMeThemeProvider {
        val colors = LocalAlarmMeColors.current

        AmHeroTextField(
            modifier = Modifier
                .background(colors.surface)
                .padding(10.dp),
            value = text,
            label = "Label",
            placeholder = "What is it for?",
            onValueChange = { text = it },
            maxCharCount = 30
        )
    }
}

@Preview(name = "Light Mode--Default Label", showBackground = true)
@Preview(
    name = "Dark Mode--Default Label",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmHeroTextFieldDefaultLabelPreview() {
    var text by remember { mutableStateOf("Alarm") }

    AlarmMeThemeProvider {
        val colors = LocalAlarmMeColors.current

        AmHeroTextField(
            modifier = Modifier
                .background(colors.surface)
                .padding(10.dp),
            value = text,
            label = "Default label",
            placeholder = "What is it for?",
            onValueChange = { text = it },
            maxCharCount = 30
        )
    }
}