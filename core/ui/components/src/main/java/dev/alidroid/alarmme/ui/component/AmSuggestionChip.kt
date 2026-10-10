package dev.alidroid.alarmme.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.alidroid.alarmme.ui.theme.AlarmMeIcons
import dev.alidroid.alarmme.ui.theme.AlarmMeThemeProvider
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeColors
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeShapes
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeTypography
import dev.alidroid.alarmme.utility.compose.modifyIfElse

private val SuggestionChipTouchTarget = 48.dp


@Composable
fun AmSuggestionChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    onLongClick: (() -> Unit)? = null,
) {
    val colors = LocalAlarmMeColors.current
    val typography = LocalAlarmMeTypography.current
    val shape = LocalAlarmMeShapes.current.small

    Row(
        modifier = modifier
            .heightIn(min = SuggestionChipTouchTarget)
            .clip(shape)
            .combinedClickable(onClick = onClick, onLongClick = onLongClick)
            .modifyIfElse(
                condition = selected,
                thenModifier = { background(colors.primary) },
                otherwiseModifier = { border(1.dp, colors.outlineVariant, shape) }
            )
            .padding(
                if (selected) {
                    PaddingValues(start = 10.dp, end = 14.dp)
                } else {
                    PaddingValues(horizontal = 14.dp)
                }
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        if (selected) {
            AmIcon(
                painter = AlarmMeIcons.Check,
                contentDescription = null,
                size = AmIconDefaults.Small,
                tint = colors.onPrimary
            )
        }

        Text(
            text = text,
            style = if (selected) {
                typography.labelLarge
            } else {
                typography.labelLarge.copy(fontWeight = FontWeight.Medium)
            },
            color = if (selected) colors.onPrimary else colors.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }

}

@Preview(name = "Light Mode--Unselected", showBackground = true)
@Preview(
    name = "Dark Mode--Unselected",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmSuggestionChipUnselectedPreview() {
    AlarmMeThemeProvider {
        AmSuggestionChip(
            modifier = Modifier.padding(10.dp),
            text = "Medication",
            selected = false,
            onClick = {}
        )
    }
}

@Preview(name = "Light Mode--Selected", showBackground = true)
@Preview(
    name = "Dark Mode--Selected",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmSuggestionChipSelectedPreview() {
    AlarmMeThemeProvider {
        AmSuggestionChip(
            modifier = Modifier.padding(10.dp),
            text = "Gym",
            selected = true,
            onClick = {}
        )
    }
}

@Preview(name = "Light Mode--Long Label", showBackground = true)
@Preview(
    name = "Dark Mode--Long Label",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmSuggestionChipLongLabelPreview() {
    var selected by remember { mutableStateOf(false) }

    AlarmMeThemeProvider {
        AmSuggestionChip(
            modifier = Modifier.padding(10.dp),
            text = "Weekday mornings routine",
            selected = selected,
            onClick = { selected = !selected }
        )
    }
}