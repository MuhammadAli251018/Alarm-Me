package dev.alidroid.alarmme.feature.alarmRinging.presentation

import android.os.Parcelable
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import dev.alidroid.alarmme.alarms.domain.AlarmId
import dev.alidroid.alarmme.feature.alarmRinging.presentation.component.RingingActionStack
import dev.alidroid.alarmme.feature.alarmRinging.presentation.component.RingingHero
import dev.alidroid.alarmme.feature.alarmRinging.presentation.component.meridiem
import dev.alidroid.alarmme.feature.alarmRinging.presentation.component.ringingHalo
import dev.alidroid.alarmme.feature.alarmRinging.presentation.model.AlarmRingingEvent
import dev.alidroid.alarmme.feature.alarmRinging.presentation.model.AlarmRingingState
import dev.alidroid.alarmme.feature.alarmRinging.presentation.model.AlarmRingingUiState
import dev.alidroid.alarmme.navigation.AlarmRingingParams
import dev.alidroid.alarmme.ui.component.ErrorScreenContent
import dev.alidroid.alarmme.ui.screenCategory.CommonScreenCategoriesPreview
import dev.alidroid.alarmme.ui.screenCategory.ScreenCategory
import dev.alidroid.alarmme.ui.screenCategory.WidthCategory
import dev.alidroid.alarmme.ui.screenCategory.currentScreenCategory
import dev.alidroid.alarmme.ui.screenCategory.isExpandedWidthOrWider
import dev.alidroid.alarmme.ui.screenCategory.isLandscapePhone
import dev.alidroid.alarmme.ui.screenCategory.isPhone
import dev.alidroid.alarmme.ui.theme.AlarmMeThemeProvider
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeColors
import dev.alidroid.alarmme.utility.kotlinDateTime.toClockLabel
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.parcelize.Parcelize
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import org.koin.core.parameter.parametersOf

private const val SNOOZED_EYEBROW = "SNOOZED"
private const val RINGING_HINT = "Snooze = one tap · dismiss = press and hold"
private const val SNOOZED_HINT = "Snooze again, or hold to dismiss for good"

private const val RINGING_HALO_ALPHA = 0.15f
private const val SNOOZED_HALO_ALPHA = 0.09f

private val PortraitHeroTopSpacing: Dp = 46.dp
private val PortraitActionsBottomSpacing: Dp = 34.dp
private val LandscapeVerticalPadding: Dp = 26.dp
private val LandscapeActionsWidth: Dp = 300.dp
private val ExpandedActionsMaxWidth: Dp = 480.dp

private const val ERROR_HEADING = "Couldn't load the alarm"
private const val ERROR_DESCRIPTION = "Something went wrong reading the alarm that's ringing."

@Parcelize
class AlarmRingingScreen(val params: AlarmRingingParams) : Screen, Parcelable {

    @Composable
    override fun Content() {
        val alarmId = params.alarmId
        val screenModel: AlarmRingingScreenModel =
            koinScreenModel(parameters = { parametersOf(alarmId) })
        val state by screenModel.state.collectAsState()
        val dispatch = screenModel::handleEvent


        AlarmRingingScreenContent(
            // Painted full-bleed first, so the colour runs under the system bars the content insets past.
            modifier = Modifier
                .fillMaxSize()
                .background(LocalAlarmMeColors.current.surfaceContainerLowest)
                .systemBarsPadding(),
            state = state,
            onSnoozeClick = { dispatch(AlarmRingingEvent.OnSnoozeClick) },
            onDismiss = { dispatch(AlarmRingingEvent.OnDismiss) },
            onRetry = { dispatch(AlarmRingingEvent.OnRetry) }
        )
    }
}

@Composable
fun AlarmRingingScreenContent(
    state: AlarmRingingState,
    onSnoozeClick: () -> Unit,
    onDismiss: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (state) {
        // The background is already painted by the caller; there is nothing else to show yet.
        AlarmRingingState.Loading -> Box(modifier = modifier.fillMaxSize())

        AlarmRingingState.Error -> ErrorScreenContent(
            modifier = modifier.fillMaxSize(),
            errorHeading = ERROR_HEADING,
            errorDescription = ERROR_DESCRIPTION,
            onRetry = onRetry,
            onDismiss = onDismiss
        )

        is AlarmRingingState.Loaded -> AlarmRingingScreenContent(
            state = state.alarm,
            onSnoozeClick = onSnoozeClick,
            onDismiss = onDismiss,
            modifier = modifier.fillMaxSize()
        )
    }
}

@Composable
fun AlarmRingingScreenContent(
    state: AlarmRingingUiState,
    onSnoozeClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalAlarmMeColors.current
    val screenCategory = currentScreenCategory()
    val snoozed = state.phase as? AlarmRingingUiState.Phase.Snoozed

    val haloColor by animateColorAsState(
        targetValue = if (snoozed != null) {
            colors.tertiary.copy(alpha = SNOOZED_HALO_ALPHA)
        } else colors.primary.copy(alpha = RINGING_HALO_ALPHA),
        label = "Ringing halo",
    )

    val rootModifier = modifier.semantics { isTraversalGroup = true }

    when {
        screenCategory.isLandscapePhone -> RingingLandscapeContent(
            state = state,
            onSnoozeClick = onSnoozeClick,
            onDismiss = onDismiss,
            modifier = rootModifier.ringingHalo {
                color = haloColor
                diameter = 460.dp
                centerX = 110.dp
            },
        )

        screenCategory.isExpandedWidthOrWider -> RingingExpandedContent(
            state = state,
            onSnoozeClick = onSnoozeClick,
            onDismiss = onDismiss,
            modifier = rootModifier.ringingHalo {
                color = haloColor
                diameter = 760.dp
                centerY = 120.dp
            },
        )

        else -> RingingPortraitContent(
            state = state,
            onSnoozeClick = onSnoozeClick,
            onDismiss = onDismiss,
            modifier = rootModifier.ringingHalo {
                color = haloColor
                diameter = 520.dp
                centerY = (-30).dp
            },
        )
    }
}

/** The mark sits above the time, the actions sit in the thumb arc, and the middle gives way. */
@Composable
private fun RingingPortraitContent(
    state: AlarmRingingUiState,
    onSnoozeClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val screenCategory = currentScreenCategory()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = ringingGutter(screenCategory)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(PortraitHeroTopSpacing))

        RingingHero(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            time = state.time,
            eyebrow = state.eyebrow(dated = false),
            label = state.label,
            countdown = state.countdownLabel(),
        )

        Spacer(Modifier.height(PortraitHeroTopSpacing))

        RingingActionStack(
            snoozeLabel = state.snoozeLabel(),
            snoozeDurationLabel = state.snoozeDurationLabel(),
            snoozeOnClickLabel = state.snoozeOnClickLabel(),
            snoozeExhausted = state.isSnoozeExhausted,
            dismissLabel = state.dismissLabel(),
            hint = state.hint(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = PortraitActionsBottomSpacing),
            onSnoozeClick = onSnoozeClick,
            onDismiss = onDismiss,
        )
    }
}

@Composable
private fun RingingLandscapeContent(
    state: AlarmRingingUiState,
    onSnoozeClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val screenCategory = currentScreenCategory()
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = ringingGutter(screenCategory), vertical = LandscapeVerticalPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(26.dp),
    ) {
        RingingHero(
            time = state.time,
            eyebrow = state.eyebrow(dated = false),
            label = state.label,
            countdown = state.countdownLabel(),
            showMark = false,
            eyebrowAboveTime = true,
            modifier = Modifier.weight(1f),
        )

        RingingActionStack(
            snoozeLabel = state.snoozeLabel(),
            snoozeDurationLabel = state.snoozeDurationLabel(),
            snoozeOnClickLabel = state.snoozeOnClickLabel(),
            snoozeExhausted = state.isSnoozeExhausted,
            dismissLabel = state.dismissLabel(),
            hint = state.hint(),
            modifier = Modifier.width(LandscapeActionsWidth),
            onSnoozeClick = onSnoozeClick,
            onDismiss = onDismiss,
        )
    }
}

@Composable
private fun RingingExpandedContent(
    state: AlarmRingingUiState,
    onSnoozeClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val screenCategory = currentScreenCategory()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = ringingGutter(screenCategory)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        RingingHero(
            time = state.time,
            eyebrow = state.eyebrow(dated = true),
            label = state.label,
            countdown = state.countdownLabel(),
            showMark = false,
            eyebrowAboveTime = true,
        )

        Spacer(Modifier.height(24.dp))

        RingingActionStack(
            snoozeLabel = state.snoozeLabel(),
            snoozeDurationLabel = state.snoozeDurationLabel(),
            snoozeOnClickLabel = state.snoozeOnClickLabel(),
            snoozeExhausted = state.isSnoozeExhausted,
            dismissLabel = state.dismissLabel(),
            hint = state.hint(),
            modifier = Modifier.widthIn(max = ExpandedActionsMaxWidth),
            onSnoozeClick = onSnoozeClick,
            onDismiss = onDismiss,
        )
    }
}


private fun ringingGutter(screenCategory: ScreenCategory): Dp {
    return when {
        screenCategory.isPhone -> 20.dp
        screenCategory.width == WidthCategory.Medium -> 32.dp
        screenCategory.isExpandedWidthOrWider -> 48.dp
        else -> 20.dp
    }
}

/** `"AM · THURSDAY"`, `"AM · THURSDAY 10 SEPTEMBER"` on expanded, `"AM · SNOOZED"` once snoozed. */
private fun AlarmRingingUiState.eyebrow(dated: Boolean): String {
    val tail = when {
        phase is AlarmRingingUiState.Phase.Snoozed -> SNOOZED_EYEBROW
        dated -> "${date.dayOfWeek.name} ${date.day} ${date.month.name}"
        else -> date.dayOfWeek.name
    }
    return "${time.meridiem()} · $tail"
}

/** `"Back at 6:49 AM · 08:42 remaining"`, or null while the alarm is actually ringing. */
private fun AlarmRingingUiState.countdownLabel(): String? {
    val snoozed = phase as? AlarmRingingUiState.Phase.Snoozed ?: return null
    return "Back at ${snoozed.nextRingAt.toClockLabel()} ${snoozed.nextRingAt.meridiem()} · " +
        "${snoozed.remaining.toRemainingLabel()} remaining"
}

/** `mm:ss`, zero-padded so tabular figures keep the line still as it ticks. */
private fun Duration.toRemainingLabel(): String {
    val totalSeconds = inWholeSeconds.coerceAtLeast(0)
    val minutes = (totalSeconds / 60).toString().padStart(2, '0')
    val seconds = (totalSeconds % 60).toString().padStart(2, '0')
    return "$minutes:$seconds"
}

private val AlarmRingingUiState.isSnoozeExhausted: Boolean
    get() = snooze is AlarmRingingUiState.Snooze.Exhausted

/** Snooze is finite, and once it has been used the count is stated rather than implied. */
private fun AlarmRingingUiState.snoozeLabel(): String = when (val snooze = snooze) {
    AlarmRingingUiState.Snooze.Exhausted -> "No snoozes left"
    is AlarmRingingUiState.Snooze.Available ->
        if (phase is AlarmRingingUiState.Phase.Snoozed) {
            "Snooze again (${snooze.remainingCount} left)"
        } else {
            "Snooze"
        }
}

/** The duration meta answers "how long" on the first tap; the design drops it once snoozed. */
private fun AlarmRingingUiState.snoozeDurationLabel(): String? {
    if (phase is AlarmRingingUiState.Phase.Snoozed) return null
    val available = snooze as? AlarmRingingUiState.Snooze.Available ?: return null
    return "${available.duration.inWholeMinutes} min"
}

private fun AlarmRingingUiState.snoozeOnClickLabel(): String? {
    val available = snooze as? AlarmRingingUiState.Snooze.Available ?: return null
    return "Snooze ${available.duration.inWholeMinutes} minutes"
}

private fun AlarmRingingUiState.dismissLabel(): String =
    if (phase is AlarmRingingUiState.Phase.Snoozed) "Hold to dismiss for good" else "Hold to dismiss"

private fun AlarmRingingUiState.hint(): String =
    if (phase is AlarmRingingUiState.Phase.Snoozed) SNOOZED_HINT else RINGING_HINT


// Previews

private fun previewRingingAlarm(
    phase: AlarmRingingUiState.Phase = AlarmRingingUiState.Phase.Ringing,
    snooze: AlarmRingingUiState.Snooze = AlarmRingingUiState.Snooze.Available(9.minutes, 3),
): AlarmRingingUiState = AlarmRingingUiState(
    id = AlarmId("weekday"),
    time = LocalTime(6, 40),
    label = "Weekday mornings, gently",
    date = LocalDate(2026, 9, 10),
    phase = phase,
    snooze = snooze,
)

@CommonScreenCategoriesPreview
@Composable
private fun AlarmRingingScreenContentPreview() {
    PreviewAlarmRinging(previewRingingAlarm())
}

@CommonScreenCategoriesPreview
@Composable
private fun AlarmRingingScreenContentSnoozedPreview() {
    PreviewAlarmRinging(
        previewRingingAlarm(
            phase = AlarmRingingUiState.Phase.Snoozed(
                nextRingAt = LocalTime(6, 49),
                remaining = 8.minutes + 42.seconds,
            ),
            snooze = AlarmRingingUiState.Snooze.Available(9.minutes, 2),
        ),
    )
}

@CommonScreenCategoriesPreview
@Composable
private fun AlarmRingingScreenContentExhaustedPreview() {
    PreviewAlarmRinging(
        previewRingingAlarm(
            phase = AlarmRingingUiState.Phase.Snoozed(
                nextRingAt = LocalTime(6, 49),
                remaining = 1.minutes + 5.seconds,
            ),
            snooze = AlarmRingingUiState.Snooze.Exhausted,
        ),
    )
}

/** Daytime alarm, light theme, a shorter label — the design's own light frame. */
@CommonScreenCategoriesPreview
@Composable
private fun AlarmRingingScreenContentDaytimePreview() {
    PreviewAlarmRinging(
        previewRingingAlarm().copy(
            time = LocalTime(13, 5),
            label = "Take the bread out",
        ),
    )
}

@Composable
private fun PreviewAlarmRinging(state: AlarmRingingUiState) {
    AlarmMeThemeProvider {
        AlarmRingingScreenContent(
            state = state,
            modifier = Modifier
                .fillMaxSize()
                .background(LocalAlarmMeColors.current.surfaceContainerLowest),
            onSnoozeClick = {},
            onDismiss = {},
        )
    }
}
