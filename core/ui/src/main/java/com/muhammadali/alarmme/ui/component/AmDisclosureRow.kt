package com.muhammadali.alarmme.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.muhammadali.alarmme.ui.theme.AlarmMeThemeProvider
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeColors
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeTypography
import com.muhammadali.alarmme.utility.compose.modifyIf

/** The row's default height, exposed so placeholders can reserve the same box. */
object AmDisclosureRowDefaults {
    val Height: Dp = 60.dp
    val HorizontalPadding: Dp = 20.dp
}

@Composable
fun AmDisclosureRow(
    text: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    trailingContent: @Composable RowScope.() -> Unit,
) {
    val colors = LocalAlarmMeColors.current
    val typography = LocalAlarmMeTypography.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(AmDisclosureRowDefaults.Height)
            .modifyIf(onClick != null) {
                clickable(onClick = onClick ?: {})
            }
            .padding(horizontal = AmDisclosureRowDefaults.HorizontalPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = typography.bodyLarge,
            color = colors.onSurface
        )

        Spacer(Modifier.weight(1f))

        trailingContent()
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(
    name = "Dark Mode",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmDisclosureRowPreview() {
    AlarmMeThemeProvider {
        val colors = LocalAlarmMeColors.current

        AmDisclosureRow(
            text = "Sound",
            onClick = {}
        ) {

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Sunrise chime",
                    style = LocalAlarmMeTypography.current.bodyMedium,
                    color = colors.primary
                )
                Text(
                    text = "›",
                    style = LocalAlarmMeTypography.current.bodyLarge,
                    color = colors.onSurface
                )
            }
        }
    }
}

@Preview(name = "Light Mode--With Chevron", showBackground = true)
@Preview(
    name = "Dark Mode--With Chevron",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmDisclosureRowWithChevronPreview() {
    AlarmMeThemeProvider {
        val colors = LocalAlarmMeColors.current

        AmDisclosureRow(
            text = "Sep 20, 24, 28",
            onClick = {}
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "9 min · 3 times",
                    style = LocalAlarmMeTypography.current.bodyMedium,
                    color = colors.onSurfaceVariant
                )
                Text(
                    text = "›",
                    style = LocalAlarmMeTypography.current.bodyLarge,
                    color = colors.onSurfaceVariant
                )
            }
        }
    }
}
