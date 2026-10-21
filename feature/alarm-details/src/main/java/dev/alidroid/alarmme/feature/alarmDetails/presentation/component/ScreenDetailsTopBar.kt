package dev.alidroid.alarmme.feature.alarmDetails.presentation.component

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.alidroid.alarmme.ui.component.AmIcon
import dev.alidroid.alarmme.ui.component.AmIconButton
import dev.alidroid.alarmme.ui.component.AmPlainTextButton
import dev.alidroid.alarmme.ui.component.AmSquaryFilledTextButton
import dev.alidroid.alarmme.ui.components.R as ComponentsR
import dev.alidroid.alarmme.ui.screenCategory.CommonScreenCategoriesPreview
import dev.alidroid.alarmme.ui.screenCategory.currentScreenCategory
import dev.alidroid.alarmme.ui.screenCategory.isExpandedWidthOrWider
import dev.alidroid.alarmme.ui.screenCategory.isLandscapePhone
import dev.alidroid.alarmme.ui.theme.AlarmMeIcons
import dev.alidroid.alarmme.ui.theme.AlarmMeThemeProvider
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeColors
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeTypography
import dev.alidroid.alarmme.utility.compose.modifyIf

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
            AmIcon(
                painter = AlarmMeIcons.ArrowBack,
                contentDescription = stringResource(ComponentsR.string.common_go_back),
                tint = colors.onSurfaceVariant
            )
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

        if (currentScreenCategory.isExpandedWidthOrWider || currentScreenCategory.isLandscapePhone) {

            AmPlainTextButton(
                text = stringResource(ComponentsR.string.common_delete),
                contentColor = colors.error,
                onClick = onDeleteButtonClick
            )

            AmSquaryFilledTextButton(
                text = stringResource(ComponentsR.string.common_save),
                enabled = canSave,
                onClick = onSaveButtonClick
            )
        }
        else {
            AmPlainTextButton(
                text = stringResource(ComponentsR.string.common_save),
                contentColor = colors.primary,
                enabled = canSave,
                onClick = onSaveButtonClick
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