package dev.alidroid.alarmme.utility.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection

/**
 * Pins [content] to left-to-right regardless of the surrounding locale.
 *
 * For runs that read the same way in every language — a clock's `6:40 AM`, a `HOUR : MINUTE` field —
 * where mirroring would reorder the parts rather than translate them. This is what Android's own
 * clock and time pickers do. Adds no layout node of its own, so it is free at layout and draw time.
 *
 * Reach for it only around such a run, never around a whole screen: anything inside stops mirroring,
 * including padding and alignment.
 */
@Composable
fun LtrContent(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr, content = content)
}
