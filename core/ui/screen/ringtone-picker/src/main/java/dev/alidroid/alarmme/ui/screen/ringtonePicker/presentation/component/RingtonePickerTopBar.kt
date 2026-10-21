package dev.alidroid.alarmme.ui.screen.ringtonePicker.presentation.component

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.alidroid.alarmme.ui.components.R as ComponentsR
import dev.alidroid.alarmme.ui.screen.ringtonePicker.R
import dev.alidroid.alarmme.ui.screen.ringtonePicker.presentation.hasCustomSoundInTopBar
import dev.alidroid.alarmme.ui.screen.ringtonePicker.presentation.screenConstraints
import dev.alidroid.alarmme.ui.component.AmIcon
import dev.alidroid.alarmme.ui.component.AmIconButton
import dev.alidroid.alarmme.ui.component.AmOutlineButtonWithIcon
import dev.alidroid.alarmme.ui.component.AmPlainTextButton
import dev.alidroid.alarmme.ui.screenCategory.CommonScreenCategoriesPreview
import dev.alidroid.alarmme.ui.screenCategory.currentScreenCategory
import dev.alidroid.alarmme.ui.theme.AlarmMeIcons
import dev.alidroid.alarmme.ui.theme.AlarmMeThemeProvider
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeColors
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeTypography

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
                contentDescription = stringResource(ComponentsR.string.common_go_back),
                tint = colors.onSurfaceVariant
            )
        }

        Text(
            text = stringResource(R.string.sound_screen_title),
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
                    text = stringResource(ComponentsR.string.common_save),
                    contentColor = colors.primary,
                    enabled = canSave,
                    onClick = onSaveClick
                )

                if (currentScreenCategory().hasCustomSoundInTopBar()) {
                    AmOutlineButtonWithIcon(
                        text = stringResource(R.string.ringtone_picker_custom_sound_title),
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