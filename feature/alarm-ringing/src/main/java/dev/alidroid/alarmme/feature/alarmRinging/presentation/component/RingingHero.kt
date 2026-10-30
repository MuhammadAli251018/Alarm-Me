package dev.alidroid.alarmme.feature.alarmRinging.presentation.component

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.alidroid.alarmme.ui.screenCategory.ScreenCategory
import dev.alidroid.alarmme.ui.screenCategory.currentScreenCategory
import dev.alidroid.alarmme.ui.screenCategory.isExpandedWidthOrWider
import dev.alidroid.alarmme.ui.screenCategory.isLandscapePhone
import dev.alidroid.alarmme.ui.theme.AlarmMeColorScheme
import dev.alidroid.alarmme.ui.theme.AlarmMeIcons
import dev.alidroid.alarmme.ui.theme.AlarmMeThemeProvider
import dev.alidroid.alarmme.ui.theme.AlarmMeTypography
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeColors
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeTypography
import dev.alidroid.alarmme.utility.compose.modifyIf
import dev.alidroid.alarmme.utility.kotlinDateTime.toClockLabel
import kotlinx.datetime.LocalTime

private const val TIME_LINE_HEIGHT_RATIO = 1.06f
private const val TIME_TRACKING_EM = -0.045f
private const val EXPANDED_TIME_TRACKING_EM = -0.05f
private const val EYEBROW_TRACKING_EM = 0.26f
private const val EXPANDED_EYEBROW_TRACKING_EM = 0.3f
private const val LABEL_LINE_HEIGHT_RATIO = 1.3f

private val MaxWidth: Dp = 480.dp

/**
 * OpenType tabular figures: every digit is the same width, so the clock and countdown don't jitter
 * as their digits change.
 */
private const val TABULAR_FIGURES = "tnum"

private val ALARM_ME_LOGO_SIZE = 84.dp


@Composable
fun RingingHero(
    time: LocalTime,
    eyebrow: String,
    label: String,
    modifier: Modifier = Modifier,
    countdown: String? = null,
    showMark: Boolean = true,
    eyebrowAboveTime: Boolean = false,
) {
    val colors = LocalAlarmMeColors.current
    val typography = LocalAlarmMeTypography.current
    val timeLabel = remember(time) { time.toClockLabel() }
    val density = LocalDensity.current

    val screenCategory = currentScreenCategory()
    val timeStyle = ringingTimeStyle(typography, screenCategory)
    val eyebrowStyle = ringingEyebrowStyle(typography, screenCategory)
    val labelStyle = ringingLabelStyle(typography, screenCategory)
    val infiniteTransition = rememberInfiniteTransition("ring animation")

    val ringAnimation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Ring animation"
    )

    BoxWithConstraints(modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = MaxWidth)
                .drawBehind {
                    val radius = size.width * 0.4f

                    drawCircle(
                        color = colors.secondary,
                        radius = radius,
                        style = Stroke(1f)
                    )
                }
                .drawBehind {
                    val radius = size.width * 0.4f

                    drawCircle(
                        color = colors.primary,
                        radius = radius + (radius * 0.3f * ringAnimation),
                        style = Stroke(2f * ringAnimation)
                    )
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {

            if (showMark) {
                Image(
                    modifier = Modifier
                        .ringingHalo(
                            colors,
                            radius = with(density) {
                                this@BoxWithConstraints.maxWidth.toPx()
                            }
                        )
                        .padding(bottom = 28.dp)
                        .size(ALARM_ME_LOGO_SIZE),
                    painter = AlarmMeIcons.LogoMark,
                    contentDescription = null
                )
            }

            if (eyebrowAboveTime) {
                Text(
                    text = eyebrow,
                    style = eyebrowStyle,
                    color = colors.onSurfaceVariant
                )
            }

            Text(
                modifier = Modifier.modifyIf(!showMark) {
                    ringingHalo(
                        colors,
                        radius = with(density) {
                            this@BoxWithConstraints.maxWidth.toPx()
                        }
                    )
                },
                text = timeLabel,
                style = timeStyle, color = colors.onSurface
            )

            if (!eyebrowAboveTime) {
                Text(
                    text = eyebrow,
                    style = eyebrowStyle,
                    color = colors.onSurfaceVariant
                )
            }

            Text(
                text = label,
                style = labelStyle,
                color = colors.onSurface,
                textAlign = TextAlign.Center,
            )

            if (countdown != null) {
                Text(
                    modifier = Modifier.padding(top = 6.dp),
                    text = countdown,
                    style = typography.labelLarge.copy(
                        letterSpacing = 1.sp,
                        fontFeatureSettings = TABULAR_FIGURES,
                    ),
                    color = colors.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

private fun ringingTimeStyle(
    typography: AlarmMeTypography,
    screenCategory: ScreenCategory,
): TextStyle = when {
    screenCategory.isExpandedWidthOrWider -> typography.alarmDisplay.copy(
        fontSize = 148.sp,
        lineHeight = 168.sp,
        letterSpacing = 168.sp * EXPANDED_TIME_TRACKING_EM,
        fontFeatureSettings = TABULAR_FIGURES,
    )

    else -> typography.alarmDisplay.copy(
        fontSize = 96.sp,
        lineHeight = 104.sp * TIME_LINE_HEIGHT_RATIO,
        letterSpacing = 104.sp * TIME_TRACKING_EM,
        fontFeatureSettings = TABULAR_FIGURES,
    )
}

private fun ringingEyebrowStyle(
    typography: AlarmMeTypography,
    screenCategory: ScreenCategory,
): TextStyle = when {
    screenCategory.isExpandedWidthOrWider -> typography.labelSmall.copy(
        fontSize = 13.sp,
        lineHeight = 13.sp,
        letterSpacing = 13.sp * EXPANDED_EYEBROW_TRACKING_EM,
    )

    screenCategory.isLandscapePhone -> typography.labelSmall.copy(
        fontSize = 12.sp,
        lineHeight = 12.sp,
        letterSpacing = 12.sp * EYEBROW_TRACKING_EM,
    )

    else -> typography.labelSmall.copy(
        fontSize = 14.sp,
        lineHeight = 14.sp,
        letterSpacing = 14.sp * EYEBROW_TRACKING_EM,
    )
}

/** `titleLarge` resized per layout — landscape never overrides this, so it shares the 20sp default. */
private fun ringingLabelStyle(
    typography: AlarmMeTypography,
    screenCategory: ScreenCategory,
): TextStyle {
    val fontSize = if (screenCategory.isExpandedWidthOrWider) 26.sp else 20.sp
    return typography.titleLarge.copy(
        fontSize = fontSize,
        lineHeight = fontSize * LABEL_LINE_HEIGHT_RATIO,
        letterSpacing = 0.sp,
    )
}

fun Modifier.ringingHalo(colors: AlarmMeColorScheme, radius: Float): Modifier {
    return drawBehind {
        drawCircle(
            brush = Brush.radialGradient(
                0f to colors.primary.copy(0.4f),
                0.15f to colors.primary.copy(alpha = 0.2f),
                1f to Color.Transparent,
                center = center,
                radius = radius * 0.75f,
            ),
            radius = radius * 0.75f
        )
    }
}

internal fun LocalTime.meridiem(): String = if (hour < 12) "AM" else "PM"

@Preview(name = "Light Mode--Ringing", showBackground = true, heightDp = 460)
@Preview(
    name = "Dark Mode--Ringing",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES,
    heightDp = 460,
)
@Composable
private fun RingingHeroPreview() {
    PreviewRingingHero(eyebrow = "AM · THURSDAY", label = "Weekday mornings, gently")
}

@Preview(name = "Light Mode--Snoozed", showBackground = true, heightDp = 460)
@Preview(
    name = "Dark Mode--Snoozed",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES,
    heightDp = 460,
)
@Composable
private fun RingingHeroSnoozedPreview() {
    PreviewRingingHero(
        eyebrow = "AM · SNOOZED",
        label = "Weekday mornings, gently",
        countdown = "Back at 6:49 AM · 08:42 remaining"
    )
}

/** A long label has to wrap rather than shrink the time. */
@Preview(name = "Light Mode--Long label", showBackground = true, heightDp = 460)
@Preview(
    name = "Dark Mode--Long label",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES,
    heightDp = 460,
)
@Composable
private fun RingingHeroLongLabelPreview() {
    PreviewRingingHero(
        eyebrow = "AM · THURSDAY",
        label = "Move the laundry over before it sits too long",
    )
}

/** The wider layouts drop the mark and lift the eyebrow above the digits. */
@Preview(name = "Light Mode--Eyebrow above", showBackground = true, heightDp = 340)
@Preview(
    name = "Dark Mode--Eyebrow above",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES,
    heightDp = 340,
)
@Composable
private fun RingingHeroEyebrowAbovePreview() {
    PreviewRingingHero(
        eyebrow = "AM · THURSDAY 10 SEPTEMBER",
        label = "Weekday mornings, gently",
        showMark = false,
        eyebrowAboveTime = true,
    )
}

@Composable
private fun PreviewRingingHero(
    eyebrow: String,
    label: String,
    countdown: String? = null,
    showMark: Boolean = true,
    eyebrowAboveTime: Boolean = false,
) {
    AlarmMeThemeProvider {
        val colors = LocalAlarmMeColors.current

        RingingHero(
            time = LocalTime(6, 40),
            eyebrow = eyebrow,
            label = label,
            countdown = countdown,
            showMark = showMark,
            eyebrowAboveTime = eyebrowAboveTime,
            modifier = Modifier
                .background(colors.surfaceContainerLowest)
                .padding(horizontal = 20.dp, vertical = 24.dp),
        )
    }
}