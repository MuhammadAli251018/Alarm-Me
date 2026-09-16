package com.muhammadali.alarmme.feature.ringtonePicker.presentatoin

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.muhammadali.alarmme.ui.screenCategory.ScreenCategory
import com.muhammadali.alarmme.ui.screenCategory.isExpandedWidthOrWider
import com.muhammadali.alarmme.ui.screenCategory.isMediumWidth
import com.muhammadali.alarmme.ui.screenCategory.isPhone
import com.muhammadali.alarmme.utility.compose.modifyIf

private const val PHONES_SCREEN_HORIZONTAL_PADDINGS = 20
private const val MEDIUM_SCREEN_MAX_WIDTH = 480
private const val EXPANDED_SCREEN_MAX_WIDTH = 640

internal fun Modifier.screenConstraints(screenCategory: ScreenCategory): Modifier =
    this.modifyIf(screenCategory.isPhone) { padding(horizontal = PHONES_SCREEN_HORIZONTAL_PADDINGS.dp) }
        .modifyIf(screenCategory.isMediumWidth) { sizeIn(maxWidth = MEDIUM_SCREEN_MAX_WIDTH.dp) }
        .modifyIf(screenCategory.isExpandedWidthOrWider) { sizeIn(maxWidth = EXPANDED_SCREEN_MAX_WIDTH.dp) }
