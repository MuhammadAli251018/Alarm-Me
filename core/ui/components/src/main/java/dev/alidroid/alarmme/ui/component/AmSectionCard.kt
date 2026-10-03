package dev.alidroid.alarmme.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.alidroid.alarmme.ui.theme.AlarmMeThemeProvider
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeColors
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeShapes
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeTypography
import dev.alidroid.alarmme.utility.compose.modifyIf

private val EMPTY_LAMBDA: () -> Unit = {}

@Composable
fun AmSectionCard(
    modifier: Modifier,
    shape: Shape = LocalAlarmMeShapes.current.small,
    containerColor: Color = LocalAlarmMeColors.current.surfaceContainer,
    onClick: (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .clip(shape).background(containerColor)
            .modifyIf(onClick != null || onLongClick != null) {
                combinedClickable(
                    onClick = onClick ?: EMPTY_LAMBDA,
                    onLongClick = onLongClick
                )
            },
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
