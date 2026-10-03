package dev.alidroid.alarmme.feature.settings.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.alidroid.alarmme.ui.component.AmIcon
import dev.alidroid.alarmme.ui.component.AmIconButton
import dev.alidroid.alarmme.ui.screenCategory.CommonScreenCategoriesPreview
import dev.alidroid.alarmme.ui.theme.AlarmMeIcons
import dev.alidroid.alarmme.ui.theme.AlarmMeThemeProvider
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeColors
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeTypography

@Composable
fun SettingsTopBar(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
) {
    val colors = LocalAlarmMeColors.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        AmIconButton(onClick = onBackClick) {
            AmIcon(
                painter = AlarmMeIcons.ArrowBack,
                contentDescription = "Go back",
                tint = colors.onSurfaceVariant
            )
        }

        Text(
            text = "Settings",
            color = colors.onSurface,
            style = LocalAlarmMeTypography.current.titleMedium
        )
    }
}

@CommonScreenCategoriesPreview
@Composable
private fun SettingsTopBarPreview() {
    AlarmMeThemeProvider {
        SettingsTopBar(
            modifier = Modifier.background(LocalAlarmMeColors.current.background),
            onBackClick = {}
        )
    }
}
