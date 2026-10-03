package dev.alidroid.alarmme.ui.screenCategory

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.DpSize


fun DpSize.toScreenCategory(): ScreenCategory {
    return ScreenCategory.of(width, height)
}

@Composable
fun currentScreenCategory(): ScreenCategory {
    return LocalWindowInfo.current.containerDpSize.toScreenCategory()
}
