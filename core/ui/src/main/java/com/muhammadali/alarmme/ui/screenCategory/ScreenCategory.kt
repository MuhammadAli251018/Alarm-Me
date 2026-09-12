package com.muhammadali.alarmme.ui.screenCategory

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isSpecified
import androidx.window.core.layout.WindowSizeClass

enum class WidthCategory(val minWidth: Dp) {
    Compact(0.dp),
    Medium(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND.dp),
    Expanded(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND.dp),
    Large(WindowSizeClass.WIDTH_DP_LARGE_LOWER_BOUND.dp),
    ExtraLarge(WindowSizeClass.WIDTH_DP_EXTRA_LARGE_LOWER_BOUND.dp);

    companion object {
        fun of(width: Dp): WidthCategory =
            if (width.isSpecified) entries.lastOrNull { width >= it.minWidth } ?: Compact
            else Compact
    }
}

enum class HeightCategory(val minHeight: Dp) {
    Compact(0.dp),
    Medium(WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND.dp),
    Expanded(WindowSizeClass.HEIGHT_DP_EXPANDED_LOWER_BOUND.dp);

    companion object {
        fun of(height: Dp): HeightCategory =
            if (height.isSpecified) entries.lastOrNull { height >= it.minHeight } ?: Compact
            else Compact
    }
}

data class ScreenCategory(
    val width: WidthCategory,
    val height: HeightCategory
) {
    companion object {
        fun of(width: Dp, height: Dp): ScreenCategory =
            ScreenCategory(WidthCategory.of(width), HeightCategory.of(height))
    }
}
