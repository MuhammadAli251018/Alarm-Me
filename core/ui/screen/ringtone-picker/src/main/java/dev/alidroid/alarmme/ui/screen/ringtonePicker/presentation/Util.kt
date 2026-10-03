package dev.alidroid.alarmme.ui.screen.ringtonePicker.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.alidroid.alarmme.ui.screenCategory.HeightCategory
import dev.alidroid.alarmme.ui.screenCategory.ScreenCategory
import dev.alidroid.alarmme.ui.screenCategory.isExpandedWidthOrWider
import dev.alidroid.alarmme.ui.screenCategory.isLandscapePhone
import dev.alidroid.alarmme.ui.screenCategory.isMediumWidth
import dev.alidroid.alarmme.ui.screenCategory.isPhone

private const val PHONES_SCREEN_HORIZONTAL_PADDINGS = 20
private const val MEDIUM_SCREEN_HORIZONTAL_PADDINGS = 32
private const val EXPANDED_SCREEN_HORIZONTAL_PADDINGS = 48

internal fun Modifier.screenConstraints(screenCategory: ScreenCategory): Modifier = when {
    screenCategory.isPhone -> padding(horizontal = PHONES_SCREEN_HORIZONTAL_PADDINGS.dp)
    screenCategory.isExpandedWidthOrWider -> padding(horizontal = MEDIUM_SCREEN_HORIZONTAL_PADDINGS.dp)
    screenCategory.isMediumWidth -> padding(horizontal = EXPANDED_SCREEN_HORIZONTAL_PADDINGS.dp)
    else -> this
}

internal fun ScreenCategory.hasCustomSoundInTopBar(): Boolean =
    isExpandedWidthOrWider || isLandscapePhone
