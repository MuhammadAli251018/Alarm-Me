package com.muhammadali.alarmme.feature.alarmList.presentation.component

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isFinite
import com.muhammadali.alarmme.feature.alarmList.presentation.alarmsListHorizontalPaddings
import com.muhammadali.alarmme.ui.component.AmFabExtendedButtonDefaults
import com.muhammadali.alarmme.ui.component.AmSwitchToggleDefaults
import com.muhammadali.alarmme.ui.component.AmTextButtonDefaults
import com.muhammadali.alarmme.ui.screenCategory.CommonScreenCategoriesPreview
import com.muhammadali.alarmme.ui.screenCategory.CompactWidthCompactHeightPreview
import com.muhammadali.alarmme.ui.screenCategory.HeightCategory
import com.muhammadali.alarmme.ui.screenCategory.LargeWidthExpandedHeightPreview
import com.muhammadali.alarmme.ui.screenCategory.MediumWidthCompactHeightPreview
import com.muhammadali.alarmme.ui.screenCategory.MediumWidthMediumHeightPreview
import com.muhammadali.alarmme.ui.screenCategory.WidthCategory
import com.muhammadali.alarmme.ui.screenCategory.currentScreenCategory
import com.muhammadali.alarmme.ui.theme.AlarmMeThemeProvider
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeColors
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeDimensions
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeShapes
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeTypography
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlin.math.floor

/*
 * Placeholders for the alarms screen while its data loads.
 *
 * Nothing here invents a size. Every block either reuses the constant the real component lays
 * itself out with (card height, status dot, toggle track, button metrics, grid gutters) or is
 * measured from the very string it stands in for. The only tuned values are the width *fractions*
 * below, which are relative to whatever width the card ends up with, so a skeleton is correct on a
 * 412dp phone and on a 1400dp tablet's two-column grid alike.
 */

private const val SHIMMER_MIN_ALPHA = 0.45f
private const val SHIMMER_MAX_ALPHA = 1f
private const val SHIMMER_DURATION_MILLIS = 900

/**
 * How much of the card's text column each bar fills, cycled by row index so a column of
 * placeholders reads like a list of alarms rather than a printed grid. Fractions, not widths:
 * they hold up at every card width and in both column counts.
 */
private val TimeBarWidthFractions = listOf(0.38f, 0.32f, 0.42f, 0.34f)
private val DescriptionBarWidthFractions = listOf(0.72f, 0.55f, 0.80f, 0.50f)

/**
 * Measured only for its height: an ascender and a descender, so a bar standing in for a line of
 * text is exactly as tall as that line would have been.
 */
private const val LINE_HEIGHT_SAMPLE = "Ag"

/** The widest shape the time text takes, so the bar never sits taller or shorter than the real one. */
private const val TIME_SAMPLE = "00:00"

/**
 * A representative eyebrow, built by the real formatter rather than typed out, so the placeholder
 * tracks the string the top bar actually renders. Only its measured size is used.
 */
private val EyebrowSample: String = LocalDateTime(2026, 1, 1, 13, 26).toTopBarEyebrow(
    now = LocalDateTime(2026, 1, 1, 6, 0),
    timeZone = TimeZone.UTC
)

/** One pulse for the whole screen, so every skeleton on it breathes in step. */
@Composable
internal fun rememberSkeletonShimmerAlpha(): Float {
    val transition = rememberInfiniteTransition(label = "skeleton")
    val alpha by transition.animateFloat(
        initialValue = SHIMMER_MIN_ALPHA,
        targetValue = SHIMMER_MAX_ALPHA,
        animationSpec = infiniteRepeatable(
            animation = tween(SHIMMER_DURATION_MILLIS),
            repeatMode = RepeatMode.Reverse
        ),
        label = "skeletonAlpha"
    )
    return alpha
}

/** The one shape everything in here is drawn with: a neutral filled block. */
@Composable
private fun SkeletonBlock(
    modifier: Modifier,
    shape: Shape = LocalAlarmMeShapes.current.small,
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(LocalAlarmMeColors.current.surfaceContainerHigh.copy(0.5f))
    )
}

/** The size [text] would occupy in [style] — how a placeholder learns its dimensions. */
@Composable
private fun textSize(text: String, style: TextStyle): DpSize {
    val measurer = rememberTextMeasurer()
    val density = LocalDensity.current

    return remember(text, style, density, measurer) {
        val layout = measurer.measure(text = text, style = style)
        with(density) { DpSize(layout.size.width.toDp(), layout.size.height.toDp()) }
    }
}

/** A bar the exact size of [text] rendered in [style]. */
@Composable
private fun SkeletonTextBlock(text: String, style: TextStyle, modifier: Modifier = Modifier) {
    val size = textSize(text, style)
    SkeletonBlock(modifier.size(size))
}

/** The box an [com.muhammadali.alarmme.ui.component.AmDestructiveTextButton] carrying [label] fills. */
@Composable
private fun SkeletonTextButton(label: String, modifier: Modifier = Modifier) {
    val labelWidth = textSize(label, LocalAlarmMeTypography.current.labelLarge).width
    val width = (labelWidth + AmTextButtonDefaults.ContentHorizontalPadding * 2)
        .coerceAtLeast(AmTextButtonDefaults.MinWidth)

    SkeletonBlock(
        modifier = modifier
            .width(width)
            .height(AmTextButtonDefaults.Height),
        shape = LocalAlarmMeShapes.current.full
    )
}

/** The box an [com.muhammadali.alarmme.ui.component.AmFabExtendedButton] carrying [label] fills. */
@Composable
private fun SkeletonFabExtendedButton(
    label: String,
    glyph: String,
    modifier: Modifier = Modifier,
) {
    val typography = LocalAlarmMeTypography.current
    val width = textSize(label, typography.labelLarge).width +
        textSize(glyph, typography.labelLarge).width +
        AmFabExtendedButtonDefaults.IconGap +
        AmFabExtendedButtonDefaults.ContentHorizontalPadding * 2

    SkeletonBlock(
        modifier = modifier
            .width(width)
            .height(AmFabExtendedButtonDefaults.Height),
        shape = AmFabExtendedButtonDefaults.Shape
    )
}

/**
 * Stands in for [AlarmsListTopBar]. It mirrors the same three-way branch on screen category — and
 * the same paddings — so the list below it starts at the same offset once the real bar arrives.
 * Selection mode is not mirrored: nothing can be selected while the screen is still loading.
 */
@Composable
fun TopBarSkeleton(modifier: Modifier = Modifier) {
    val screenCategory = currentScreenCategory()
    val outerModifier = modifier
        .fillMaxWidth()
        .padding(horizontal = alarmsListHorizontalPaddings())
        .alpha(rememberSkeletonShimmerAlpha())

    when {
        screenCategory.height == HeightCategory.Compact ->
            TopBarSkeletonCompactHeight(outerModifier)

        screenCategory.width <= WidthCategory.Medium ->
            TopBarSkeletonCompactAndMediumWidth(outerModifier)

        else -> TopBarSkeletonExpandedAndBiggerWidth(outerModifier)
    }
}

/** Title and eyebrow side by side, with both actions trailing — the short-window arrangement. */
@Composable
private fun TopBarSkeletonCompactHeight(modifier: Modifier) {
    val typography = LocalAlarmMeTypography.current

    Row(
        modifier = modifier.padding(
            horizontal = TopBarContentHorizontalPadding,
            vertical = TopBarContentVerticalPadding
        ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(TopBarItemsSpacing)
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(TopBarItemsSpacing)
        ) {
            SkeletonTextBlock(TOP_BAR_TITLE, typography.headlineSmall)
            SkeletonTextBlock(EyebrowSample, typography.labelSmall)
        }

        SkeletonTextButton(TOP_BAR_SELECT_LABEL)
        SkeletonFabExtendedButton(TOP_BAR_NEW_ALARM_LABEL, TOP_BAR_NEW_ALARM_GLYPH)
    }
}

/** Eyebrow over title, with only "Select" trailing — the screen-level FAB carries the new alarm. */
@Composable
private fun TopBarSkeletonCompactAndMediumWidth(modifier: Modifier) {
    val typography = LocalAlarmMeTypography.current

    Column(
        modifier = modifier.padding(
            horizontal = TopBarContentHorizontalPadding,
            vertical = TopBarContentVerticalPadding
        )
    ) {
        SkeletonTextBlock(EyebrowSample, typography.labelSmall)

        Box(modifier = Modifier.fillMaxWidth()) {
            SkeletonTextBlock(
                text = TOP_BAR_TITLE,
                style = typography.headlineSmall,
                modifier = Modifier.align(Alignment.CenterStart)
            )
            SkeletonTextButton(
                label = TOP_BAR_SELECT_LABEL,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }
    }
}

/** The same column, but wide enough to hold both actions in the bar itself. */
@Composable
private fun TopBarSkeletonExpandedAndBiggerWidth(modifier: Modifier) {
    val typography = LocalAlarmMeTypography.current

    Column(
        modifier = modifier.padding(
            horizontal = TopBarContentHorizontalPadding,
            vertical = TopBarContentVerticalPadding
        )
    ) {
        SkeletonTextBlock(EyebrowSample, typography.labelSmall)

        Box(modifier = Modifier.fillMaxWidth()) {
            SkeletonTextBlock(
                text = TOP_BAR_TITLE,
                style = typography.headlineSmall,
                modifier = Modifier.align(Alignment.CenterStart)
            )

            Row(
                modifier = Modifier.align(Alignment.CenterEnd),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(TopBarItemsSpacing)
            ) {
                SkeletonTextButton(TOP_BAR_SELECT_LABEL)
                SkeletonFabExtendedButton(TOP_BAR_NEW_ALARM_LABEL, TOP_BAR_NEW_ALARM_GLYPH)
            }
        }
    }
}

/**
 * Stands in for one [AlarmItem] card: same height, same corner radius, same container, same dot
 * and toggle. [index] only picks which pair of width fractions the two bars use.
 */
@Composable
fun AlarmItemSkeleton(modifier: Modifier = Modifier, index: Int = 0) {
    val colors = LocalAlarmMeColors.current
    val shapes = LocalAlarmMeShapes.current
    val typography = LocalAlarmMeTypography.current

    val timeBarHeight = textSize(TIME_SAMPLE, typography.alarmRow).height * 0.6f
    val descriptionBarHeight = textSize(LINE_HEIGHT_SAMPLE, typography.bodySmall).height

    Box(
        modifier = modifier
            .height(ALARM_ITEM_HEIGHT.dp)
            .sizeIn(minWidth = ALARM_ITEM_MIN_WIDTH.dp)
            .clip(shapes.large)
            .background(colors.surfaceContainer.copy(0.5f))
    ) {
        Row(
            modifier = Modifier
                .matchParentSize()
                .padding(
                    horizontal = ALARM_ITEM_CONTENT_HORIZONTAL_PADDING.dp,
                    vertical = ALARM_ITEM_CONTENT_VERTICAL_PADDING.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(ALARM_ITEM_ITEMS_PADDINGS.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(LocalAlarmMeDimensions.current.xxs)
            ) {
                SkeletonBlock(
                    Modifier
                        .fillMaxWidth(TimeBarWidthFractions[index % TimeBarWidthFractions.size])
                        .height(timeBarHeight)
                )
                SkeletonBlock(
                    Modifier
                        .fillMaxWidth(
                            DescriptionBarWidthFractions[index % DescriptionBarWidthFractions.size]
                        )
                        .height(descriptionBarHeight)
                )
            }

            SkeletonBlock(
                modifier = Modifier.size(
                    width = AmSwitchToggleDefaults.TrackWidth,
                    height = AmSwitchToggleDefaults.TrackHeight
                ),
                shape = shapes.full
            )
        }
    }
}

/**
 * Stands in for [AlarmItemsList]: same gutters, same gaps, and the same one-or-two columns. The
 * row count is not a tuned number — it is however many cards fit in the height it is given, so a
 * 400dp landscape window draws a few and a tall tablet fills its grid.
 */
@Composable
fun AlarmsListSkeleton(modifier: Modifier = Modifier) {
    val columns = alarmsListColumnsCount()
    val shimmer = rememberSkeletonShimmerAlpha()

    BoxWithConstraints(modifier = modifier) {
        val rows = skeletonRowCount(maxHeight)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = alarmsListHorizontalPaddings(),
                    vertical = AlarmsListVerticalPadding
                )
                .alpha(shimmer),
            verticalArrangement = Arrangement.spacedBy(AlarmsGridVerticalSpacing)
        ) {
            repeat(rows) { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(AlarmsGridHorizontalSpacing)
                ) {
                    repeat(columns) { column ->
                        AlarmItemSkeleton(
                            modifier = Modifier.weight(1f),
                            index = row * columns + column
                        )
                    }
                }
            }
        }
    }
}

/**
 * How many card rows fit in [availableHeight], counting the gap that follows each one. Always at
 * least one row — including when the height is unbounded, where filling would never terminate.
 */
private fun skeletonRowCount(availableHeight: Dp): Int {
    if (!availableHeight.isFinite) return 1

    val contentHeight = availableHeight - AlarmsListVerticalPadding * 2 + AlarmsGridVerticalSpacing
    val rowHeight = ALARM_ITEM_HEIGHT.dp + AlarmsGridVerticalSpacing

    return floor(contentHeight / rowHeight).toInt().coerceAtLeast(1)
}

@Composable
private fun SkeletonPreviewSurface(content: @Composable () -> Unit) {
    AlarmMeThemeProvider {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(LocalAlarmMeColors.current.background)
        ) {
            content()
        }
    }
}

/**
 * The three common buckets cover two of the bar's three branches: compact width and expanded width.
 */
@CommonScreenCategoriesPreview
@Composable
private fun TopBarSkeletonPreview() {
    SkeletonPreviewSurface { TopBarSkeleton() }
}

/** The only branch the common buckets miss: a short window packs everything onto one row. */
@MediumWidthCompactHeightPreview
@Composable
private fun TopBarSkeletonCompactHeightPreview() {
    SkeletonPreviewSurface { TopBarSkeleton() }
}

/** Medium width with room to breathe — the 32dp gutter branch. */
@MediumWidthMediumHeightPreview
@Composable
private fun TopBarSkeletonMediumWidthPreview() {
    SkeletonPreviewSurface { TopBarSkeleton() }
}

/** All four fraction pairs stacked, which is the whole of the row-to-row variation. */
@Preview(name = "Alarm item skeletons - Light", widthDp = 412, showBackground = true)
@Preview(
    name = "Alarm item skeletons - Dark",
    widthDp = 412,
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AlarmItemSkeletonPreview() {
    AlarmMeThemeProvider {
        Column(
            modifier = Modifier
                .background(LocalAlarmMeColors.current.background)
                .padding(LocalAlarmMeDimensions.current.lg),
            verticalArrangement = Arrangement.spacedBy(AlarmsGridVerticalSpacing)
        ) {
            repeat(TimeBarWidthFractions.size) { index ->
                AlarmItemSkeleton(modifier = Modifier.fillMaxWidth(), index = index)
            }
        }
    }
}

/** Gutters and column count across the everyday buckets. */
@CommonScreenCategoriesPreview
@Composable
private fun AlarmsListSkeletonPreview() {
    SkeletonPreviewSurface { AlarmsListSkeleton(Modifier.fillMaxSize()) }
}

/** The shortest window there is — proof the row count shrinks rather than overflowing. */
@CompactWidthCompactHeightPreview
@Composable
private fun AlarmsListSkeletonCompactHeightPreview() {
    SkeletonPreviewSurface { AlarmsListSkeleton(Modifier.fillMaxSize()) }
}

/** And the tallest — two columns, filled to the bottom edge. */
@LargeWidthExpandedHeightPreview
@Composable
private fun AlarmsListSkeletonLargeWidthPreview() {
    SkeletonPreviewSurface { AlarmsListSkeleton(Modifier.fillMaxSize()) }
}
