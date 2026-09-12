package com.muhammadali.alarmme.ui.screenCategory

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

/** Compact width x compact height - 412x400dp. */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@Preview(
    name = "Compact W x Compact H - Light",
    group = "Compact width",
    device = "spec:width=412dp,height=400dp,dpi=420",
    showSystemUi = true,
)
@Preview(
    name = "Compact W x Compact H - Dark",
    group = "Compact width",
    device = "spec:width=412dp,height=400dp,dpi=420",
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
annotation class CompactWidthCompactHeightPreview

/** Compact width x medium height - 412x700dp. */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@Preview(
    name = "Compact W x Medium H - Light",
    group = "Compact width",
    device = "spec:width=412dp,height=700dp,dpi=420",
    showSystemUi = true,
)
@Preview(
    name = "Compact W x Medium H - Dark",
    group = "Compact width",
    device = "spec:width=412dp,height=700dp,dpi=420",
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
annotation class CompactWidthMediumHeightPreview

/** Compact width x expanded height - 412x1000dp. */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@Preview(
    name = "Compact W x Expanded H - Light",
    group = "Compact width",
    device = "spec:width=412dp,height=1000dp,dpi=420",
    showSystemUi = true,
)
@Preview(
    name = "Compact W x Expanded H - Dark",
    group = "Compact width",
    device = "spec:width=412dp,height=1000dp,dpi=420",
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
annotation class CompactWidthExpandedHeightPreview

/** Medium width x compact height - 700x400dp. */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@Preview(
    name = "Medium W x Compact H - Light",
    group = "Medium width",
    device = "spec:width=700dp,height=400dp,dpi=240",
    showSystemUi = true,
)
@Preview(
    name = "Medium W x Compact H - Dark",
    group = "Medium width",
    device = "spec:width=700dp,height=400dp,dpi=240",
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
annotation class MediumWidthCompactHeightPreview

/** Medium width x medium height - 700x700dp. */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@Preview(
    name = "Medium W x Medium H - Light",
    group = "Medium width",
    device = "spec:width=700dp,height=700dp,dpi=240",
    showSystemUi = true,
)
@Preview(
    name = "Medium W x Medium H - Dark",
    group = "Medium width",
    device = "spec:width=700dp,height=700dp,dpi=240",
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
annotation class MediumWidthMediumHeightPreview

/** Medium width x expanded height - 700x1000dp. */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@Preview(
    name = "Medium W x Expanded H - Light",
    group = "Medium width",
    device = "spec:width=700dp,height=1000dp,dpi=240",
    showSystemUi = true,
)
@Preview(
    name = "Medium W x Expanded H - Dark",
    group = "Medium width",
    device = "spec:width=700dp,height=1000dp,dpi=240",
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
annotation class MediumWidthExpandedHeightPreview

/** Expanded width x compact height - 1000x400dp. */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@Preview(
    name = "Expanded W x Compact H - Light",
    group = "Expanded width",
    device = "spec:width=1000dp,height=400dp,dpi=240",
    showSystemUi = true,
)
@Preview(
    name = "Expanded W x Compact H - Dark",
    group = "Expanded width",
    device = "spec:width=1000dp,height=400dp,dpi=240",
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
annotation class ExpandedWidthCompactHeightPreview

/** Expanded width x medium height - 1000x700dp. */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@Preview(
    name = "Expanded W x Medium H - Light",
    group = "Expanded width",
    device = "spec:width=1000dp,height=700dp,dpi=240",
    showSystemUi = true,
)
@Preview(
    name = "Expanded W x Medium H - Dark",
    group = "Expanded width",
    device = "spec:width=1000dp,height=700dp,dpi=240",
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
annotation class ExpandedWidthMediumHeightPreview

/** Expanded width x expanded height - 1000x1000dp. */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@Preview(
    name = "Expanded W x Expanded H - Light",
    group = "Expanded width",
    device = "spec:width=1000dp,height=1000dp,dpi=240",
    showSystemUi = true,
)
@Preview(
    name = "Expanded W x Expanded H - Dark",
    group = "Expanded width",
    device = "spec:width=1000dp,height=1000dp,dpi=240",
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
annotation class ExpandedWidthExpandedHeightPreview

/** Large width x compact height - 1400x400dp. */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@Preview(
    name = "Large W x Compact H - Light",
    group = "Large width",
    device = "spec:width=1400dp,height=400dp,dpi=160",
    showSystemUi = true,
)
@Preview(
    name = "Large W x Compact H - Dark",
    group = "Large width",
    device = "spec:width=1400dp,height=400dp,dpi=160",
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
annotation class LargeWidthCompactHeightPreview

/** Large width x medium height - 1400x700dp. */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@Preview(
    name = "Large W x Medium H - Light",
    group = "Large width",
    device = "spec:width=1400dp,height=700dp,dpi=160",
    showSystemUi = true,
)
@Preview(
    name = "Large W x Medium H - Dark",
    group = "Large width",
    device = "spec:width=1400dp,height=700dp,dpi=160",
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
annotation class LargeWidthMediumHeightPreview

/** Large width x expanded height - 1400x1000dp. */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@Preview(
    name = "Large W x Expanded H - Light",
    group = "Large width",
    device = "spec:width=1400dp,height=1000dp,dpi=160",
    showSystemUi = true,
)
@Preview(
    name = "Large W x Expanded H - Dark",
    group = "Large width",
    device = "spec:width=1400dp,height=1000dp,dpi=160",
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
annotation class LargeWidthExpandedHeightPreview

/** Extra-large width x compact height - 1800x400dp. */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@Preview(
    name = "Extra-large W x Compact H - Light",
    group = "Extra-large width",
    device = "spec:width=1800dp,height=400dp,dpi=160",
    showSystemUi = true,
)
@Preview(
    name = "Extra-large W x Compact H - Dark",
    group = "Extra-large width",
    device = "spec:width=1800dp,height=400dp,dpi=160",
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
annotation class ExtraLargeWidthCompactHeightPreview

/** Extra-large width x medium height - 1800x700dp. */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@Preview(
    name = "Extra-large W x Medium H - Light",
    group = "Extra-large width",
    device = "spec:width=1800dp,height=700dp,dpi=160",
    showSystemUi = true,
)
@Preview(
    name = "Extra-large W x Medium H - Dark",
    group = "Extra-large width",
    device = "spec:width=1800dp,height=700dp,dpi=160",
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
annotation class ExtraLargeWidthMediumHeightPreview

/** Extra-large width x expanded height - 1800x1000dp. */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@Preview(
    name = "Extra-large W x Expanded H - Light",
    group = "Extra-large width",
    device = "spec:width=1800dp,height=1000dp,dpi=160",
    showSystemUi = true,
)
@Preview(
    name = "Extra-large W x Expanded H - Dark",
    group = "Extra-large width",
    device = "spec:width=1800dp,height=1000dp,dpi=160",
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
annotation class ExtraLargeWidthExpandedHeightPreview

/**
 * Every screen category in both themes - 15 buckets x 2 themes = **30 renders** per composable,
 * which is slow in Android Studio. Prefer [CommonScreenCategoriesPreview] day to day, or use the
 * preview panel's group filter (groups are named per width bucket) to show one column at a time.
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@CompactWidthCompactHeightPreview
@CompactWidthMediumHeightPreview
@CompactWidthExpandedHeightPreview
@MediumWidthCompactHeightPreview
@MediumWidthMediumHeightPreview
@MediumWidthExpandedHeightPreview
@ExpandedWidthCompactHeightPreview
@ExpandedWidthMediumHeightPreview
@ExpandedWidthExpandedHeightPreview
@LargeWidthCompactHeightPreview
@LargeWidthMediumHeightPreview
@LargeWidthExpandedHeightPreview
@ExtraLargeWidthCompactHeightPreview
@ExtraLargeWidthMediumHeightPreview
@ExtraLargeWidthExpandedHeightPreview
annotation class AllScreenCategoriesPreview

/**
 * The three buckets most real devices land in, in both themes - 6 renders. The everyday default;
 * reach for [AllScreenCategoriesPreview] when you specifically need full coverage.
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@CompactWidthMediumHeightPreview
@MediumWidthCompactHeightPreview
@ExpandedWidthMediumHeightPreview
annotation class CommonScreenCategoriesPreview
