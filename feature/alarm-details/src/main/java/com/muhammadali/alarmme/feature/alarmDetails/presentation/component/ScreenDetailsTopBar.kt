package com.muhammadali.alarmme.feature.alarmDetails.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.muhammadali.alarmme.ui.component.AmIconButton
import com.muhammadali.alarmme.ui.component.AmPlainTextButton
import com.muhammadali.alarmme.ui.component.AmSquaryFilledTextButton
import com.muhammadali.alarmme.ui.screenCategory.CommonScreenCategoriesPreview
import com.muhammadali.alarmme.ui.screenCategory.currentScreenCategory
import com.muhammadali.alarmme.ui.screenCategory.isExpandedWidthOrWider
import com.muhammadali.alarmme.ui.theme.AlarmMeThemeProvider
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeColors
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeTypography
import com.muhammadali.alarmme.utility.compose.modifyIf

@Composable
fun ScreenDetailsTopBar(
    modifier: Modifier = Modifier,
    alarmTitle: String,
    canSave: Boolean,
    onBackClick: () -> Unit,
    onSaveButtonClick: () -> Unit,
    onDeleteButtonClick: () -> Unit
) {
    val typography = LocalAlarmMeTypography.current
    val colors = LocalAlarmMeColors.current
    val currentScreenCategory = currentScreenCategory()

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

        Box(
            modifier = Modifier
                .weight(1f)
                .modifyIf(!currentScreenCategory.isExpandedWidthOrWider) {
                    scrollable(state = rememberScrollState(), orientation = Orientation.Horizontal)
                }
        ) {
            Text(
                modifier = Modifier.align(Alignment.CenterStart),
                text = alarmTitle,
                color = colors.onSurface,
                style = typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        if (currentScreenCategory.isExpandedWidthOrWider) {

            AmPlainTextButton(
                text = "Delete",
                contentColor = colors.error,
                onClick = onDeleteButtonClick
            )

            AmSquaryFilledTextButton(
                text = "Save",
                enabled = canSave,
                onClick = onSaveButtonClick
            )
        }
        else {
            AmPlainTextButton(
                text = "Save",
                contentColor = colors.primary,
                enabled = canSave,
                onClick = onDeleteButtonClick
            )
        }
    }
}

@CommonScreenCategoriesPreview
@Composable
private fun ScreenDetailsTopBarPreview() {
    AlarmMeThemeProvider {
        ScreenDetailsTopBar(
            modifier = Modifier
                .fillMaxWidth()
                .background(LocalAlarmMeColors.current.background),
            alarmTitle = "Morning workout",
            canSave = true,
            onBackClick = {},
            onSaveButtonClick = {},
            onDeleteButtonClick = {}
        )
    }
}

@CommonScreenCategoriesPreview
@Composable
private fun ScreenDetailsTopBarSaveDisabledPreview() {
    AlarmMeThemeProvider {
        ScreenDetailsTopBar(
            modifier = Modifier
                .fillMaxWidth()
                .background(LocalAlarmMeColors.current.background),
            alarmTitle = "Morning workout",
            canSave = false,
            onBackClick = {},
            onSaveButtonClick = {},
            onDeleteButtonClick = {}
        )
    }
}