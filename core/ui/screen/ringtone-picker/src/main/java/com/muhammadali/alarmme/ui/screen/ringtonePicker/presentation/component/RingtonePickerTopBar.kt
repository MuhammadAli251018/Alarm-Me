package com.muhammadali.alarmme.ui.screen.ringtonePicker.presentation.component

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
import com.muhammadali.alarmme.ui.screen.ringtonePicker.presentation.hasCustomSoundInTopBar
import com.muhammadali.alarmme.ui.screen.ringtonePicker.presentation.screenConstraints
import com.muhammadali.alarmme.ui.component.AmIcon
import com.muhammadali.alarmme.ui.component.AmIconButton
import com.muhammadali.alarmme.ui.component.AmOutlineButtonWithIcon
import com.muhammadali.alarmme.ui.component.AmPlainTextButton
import com.muhammadali.alarmme.ui.screenCategory.CommonScreenCategoriesPreview
import com.muhammadali.alarmme.ui.screenCategory.currentScreenCategory
import com.muhammadali.alarmme.ui.theme.AlarmMeIcons
import com.muhammadali.alarmme.ui.theme.AlarmMeThemeProvider
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeColors
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeTypography

@Composable
fun RingtonePickerTopBar(
    modifier: Modifier = Modifier,
    canSave: Boolean,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
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
            AmIcon(
                painter = AlarmMeIcons.ArrowBack,
                contentDescription = "Go back",
                tint = colors.onSurfaceVariant
            )
        }

        Text(
            text = "Ringtones",
            color = colors.onSurface,
            style = typography.titleMedium
        )

        Box(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.align(Alignment.CenterEnd),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                AmPlainTextButton(
                    text = "Save",
                    contentColor = colors.primary,
                    enabled = canSave,
                    onClick = onSaveClick
                )

                if (currentScreenCategory().hasCustomSoundInTopBar()) {
                    AmOutlineButtonWithIcon(
                        text = "Custom Sound",
                        icon = {
                            AmIcon(
                                painter = AlarmMeIcons.Add,
                                contentDescription = null,
                                tint = colors.primary
                            )
                        },
                        onClick = onCustomSoundClick
                    )
                }
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
            canSave = true,
            onBackClick = {},
            onSaveClick = {},
            onCustomSoundClick = {}
        )
    }
}