package com.muhammadali.alarmme.feature.ringtonePicker.presentatoin.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.muhammadali.alarmme.feature.ringtonePicker.presentatoin.screenConstraints
import com.muhammadali.alarmme.ui.component.AmIconButton
import com.muhammadali.alarmme.ui.component.AmOutlineButtonWithIcon
import com.muhammadali.alarmme.ui.screenCategory.CommonScreenCategoriesPreview
import com.muhammadali.alarmme.ui.screenCategory.currentScreenCategory
import com.muhammadali.alarmme.ui.screenCategory.isExpandedWidthOrWider
import com.muhammadali.alarmme.ui.theme.AlarmMeThemeProvider
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeColors
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeTypography

@Composable
fun RingtonePickerTopBar(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onCustomSoundClick: () -> Unit
) {
    val typography = LocalAlarmMeTypography.current
    val colors = LocalAlarmMeColors.current

    Row(
        modifier = modifier
            .padding(horizontal = 4.dp, vertical = 8.dp)
            .height(56.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        AmIconButton(onClick = onBackClick) {
            Text(
                text = "←",
                color = colors.onSurfaceVariant,
                style = typography.labelSmall
            ) // TODO: Replace with icon
        }

        Text(
            text = "Ringtones",
            color = colors.onSurface,
            style = typography.titleMedium
        )

        if (currentScreenCategory().isExpandedWidthOrWider) {
            Box(modifier = Modifier.weight(1f)){
                AmOutlineButtonWithIcon(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    text = "Custom Sound",
                    icon = {
                        Text(
                            text = "+",
                            style = typography.labelSmall,
                            color = colors.primary
                        )
                    },
                    onClick = onCustomSoundClick
                )
            }
        }
    }
}


@CommonScreenCategoriesPreview
@Composable
private fun RingtonePickerTopBarPreview() {
    AlarmMeThemeProvider {
        val screenCategory = currentScreenCategory()

        RingtonePickerTopBar(
            modifier = Modifier
                .background(LocalAlarmMeColors.current.background)
                .screenConstraints(screenCategory),
            onBackClick = {},
            onCustomSoundClick = {}
        )
    }
}