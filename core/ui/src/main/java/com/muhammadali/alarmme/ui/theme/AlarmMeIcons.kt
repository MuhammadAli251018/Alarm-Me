package com.muhammadali.alarmme.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.muhammadali.alarmme.ui.R

/**
 * The design system's icon set, drawn in the brand mark's own language: a 24×24 grid, 2dp round-cap
 * strokes over no fill, solid only where an outline would read as a different mark ([Play], [Pause],
 * [MoreVert]).
 *
 * Accessors are `@Composable` so consumers never touch this module's `R` class. Pair them with
 * [com.muhammadali.alarmme.ui.component.AmIcon], which carries the size and tint.
 */
object AlarmMeIcons {
    val ArrowBack: Painter
        @Composable
        get() = painterResource(R.drawable.ic_arrow_back)

    val Close: Painter
        @Composable
        get() = painterResource(R.drawable.ic_close)

    val ChevronRight: Painter
        @Composable
        get() = painterResource(R.drawable.ic_chevron_right)

    val ChevronLeft: Painter
        @Composable
        get() = painterResource(R.drawable.ic_chevron_left)

    val Add: Painter
        @Composable
        get() = painterResource(R.drawable.ic_add)

    val Check: Painter
        @Composable
        get() = painterResource(R.drawable.ic_check)

    val Play: Painter
        @Composable
        get() = painterResource(R.drawable.ic_play)

    val Pause: Painter
        @Composable
        get() = painterResource(R.drawable.ic_pause)

    val MoreVert: Painter
        @Composable
        get() = painterResource(R.drawable.ic_more_vert)
}
