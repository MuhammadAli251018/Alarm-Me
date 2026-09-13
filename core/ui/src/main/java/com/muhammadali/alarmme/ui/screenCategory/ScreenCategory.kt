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

/** A phone held in landscape: short window that is not wide enough to be a tablet/desktop. */
val ScreenCategory.isLandscapePhone: Boolean
    get() = height == HeightCategory.Compact && width < WidthCategory.Expanded

/** A phone held in portrait: narrow window with room to scroll vertically. */
val ScreenCategory.isPortraitPhone: Boolean
    get() = width == WidthCategory.Compact && height > HeightCategory.Compact

/** Either phone orientation — i.e. not a tablet, foldable or desktop window. */
val ScreenCategory.isPhone: Boolean
    get() = isLandscapePhone || isPortraitPhone

/** Enough width for multi-pane layouts. */
val ScreenCategory.isExpandedWidthOrWider: Boolean
    get() = width >= WidthCategory.Expanded
