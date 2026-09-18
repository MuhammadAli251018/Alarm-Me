package com.muhammadali.alarmme.feature.alarmDetails.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import com.muhammadali.alarmme.feature.alarmDetails.domain.AlarmDetails
import com.muhammadali.alarmme.feature.alarmDetails.domain.AlarmDetailsId
import com.muhammadali.alarmme.feature.alarmDetails.presentation.component.AlarmSettingsDetails
import com.muhammadali.alarmme.feature.alarmDetails.presentation.component.AlarmTimeDetails
import com.muhammadali.alarmme.feature.alarmDetails.presentation.component.ScheduleDetails
import com.muhammadali.alarmme.feature.alarmDetails.presentation.component.ScreenDetailsTopBar
import com.muhammadali.alarmme.feature.alarmDetails.presentation.model.AlarmDetailsEvent
import com.muhammadali.alarmme.feature.alarmDetails.presentation.model.AlarmDetailsState
import com.muhammadali.alarmme.ui.component.AmPlainTextButton
import com.muhammadali.alarmme.ui.screen.ErrorScreenContent
import com.muhammadali.alarmme.ui.screenCategory.CommonScreenCategoriesPreview
import com.muhammadali.alarmme.ui.screenCategory.ScreenCategory
import com.muhammadali.alarmme.ui.screenCategory.currentScreenCategory
import com.muhammadali.alarmme.ui.screenCategory.isExpandedWidthOrWider
import com.muhammadali.alarmme.ui.screenCategory.isLandscapePhone
import com.muhammadali.alarmme.ui.screenCategory.isMediumWidth
import com.muhammadali.alarmme.ui.screenCategory.isPhone
import com.muhammadali.alarmme.ui.theme.AlarmMeThemeProvider
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeColors
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import org.koin.core.parameter.parametersOf

class AlarmDetailsScreen(val alarmId: AlarmDetailsId) : Screen {

    @Composable
    override fun Content() {
        val screenModel: AlarmDetailsScreenModel =
            koinScreenModel(parameters = { parametersOf(alarmId) })
        val state by screenModel.state.collectAsState()
        val dispatch = screenModel::handleEvent

        AlarmDetailsScreenContent(
            modifier = Modifier
                .imePadding()
                .systemBarsPadding()
                .fillMaxSize()
                .background(LocalAlarmMeColors.current.background),
            state = state,
            onBackClick = {}, // TODO: navigate back once navigation is wired up
            onAlarmTimeClick = { dispatch(AlarmDetailsEvent.OnAlarmTimeClick) },
            onSaveClick = { dispatch(AlarmDetailsEvent.OnSaveClick) },
            onDeleteClick = { dispatch(AlarmDetailsEvent.OnDeleteClick) },
            onDayToggle = { day -> dispatch(AlarmDetailsEvent.OnDayToggle(day)) },
            onCustomDateToggle = { enabled -> dispatch(AlarmDetailsEvent.OnCustomDateToggle(enabled)) },
            onCustomDateRowClick = { dispatch(AlarmDetailsEvent.OnCustomDateRowClick) },
            onSoundClick = { dispatch(AlarmDetailsEvent.OnSoundClick) },
            onSnoozeClick = { dispatch(AlarmDetailsEvent.OnSnoozeClick) },
            onGradualVolumeToggle = { enabled -> dispatch(AlarmDetailsEvent.OnGradualVolumeToggle(enabled)) },
            onVibrateToggle = { enabled -> dispatch(AlarmDetailsEvent.OnVibrateToggle(enabled)) },
            onLabelClick = { dispatch(AlarmDetailsEvent.OnLabelClick) },
            onRetry = { dispatch(AlarmDetailsEvent.OnRetry) },
            onDismiss = { /*TODO*/ }
        )
    }
}
private const val PHONES_SCREEN_HORIZONTAL_PADDINGS = 20
private const val MEDIUM_SCREEN_HORIZONTAL_PADDINGS = 32
private const val EXPANDED_SCREEN_HORIZONTAL_PADDINGS = 48


internal fun Modifier.screenConstraints(screenCategory: ScreenCategory): Modifier = when {
    screenCategory.isPhone -> padding(horizontal = PHONES_SCREEN_HORIZONTAL_PADDINGS.dp)
    screenCategory.isExpandedWidthOrWider -> padding(horizontal = MEDIUM_SCREEN_HORIZONTAL_PADDINGS.dp)
    screenCategory.isMediumWidth -> padding(horizontal = EXPANDED_SCREEN_HORIZONTAL_PADDINGS.dp)
    else -> this
}

/** The single-column layout's content, capped/centered from medium width up, per the design. */
private val VerticalContentMaxWidth = 480.dp

@Composable
fun AlarmDetailsScreenContent(
    modifier: Modifier = Modifier,
    state: AlarmDetailsState,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    onAlarmTimeClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onDayToggle: (DayOfWeek) -> Unit,
    onCustomDateToggle: (Boolean) -> Unit,
    onCustomDateRowClick: () -> Unit,
    onSoundClick: () -> Unit,
    onSnoozeClick: () -> Unit,
    onGradualVolumeToggle: (Boolean) -> Unit,
    onVibrateToggle: (Boolean) -> Unit,
    onLabelClick: () -> Unit,
    onRetry: () -> Unit,
    onDismiss: () -> Unit,
) {
    when (state) {
        AlarmDetailsState.Loading -> LoadingAlarmDetailsScreenContent(modifier = modifier)

        AlarmDetailsState.Error -> ErrorScreenContent(
            modifier = modifier,
            errorHeading = "Couldn't load this alarm",
            errorDescription = "Something went wrong while loading it. Nothing was changed.",
            onRetry = onRetry,
            onDismiss = onDismiss
        )

        is AlarmDetailsState.Loaded -> AlarmDetailsLoadedContent(
            modifier = modifier,
            state = state,
            onBackClick = onBackClick,
            onSaveClick = onSaveClick,
            onDeleteClick = onDeleteClick,
            onAlarmTimeClick = onAlarmTimeClick,
            onDayToggle = onDayToggle,
            onCustomDateToggle = onCustomDateToggle,
            onCustomDateRowClick = onCustomDateRowClick,
            onSoundClick = onSoundClick,
            onSnoozeClick = onSnoozeClick,
            onGradualVolumeToggle = onGradualVolumeToggle,
            onVibrateToggle = onVibrateToggle,
            onLabelClick = onLabelClick
        )
    }
}

@Composable
fun LoadingAlarmDetailsScreenContent(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = LocalAlarmMeColors.current.primary)
    }
}

@Composable
fun AlarmDetailsLoadedContent(
    modifier: Modifier = Modifier,
    state: AlarmDetailsState.Loaded,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onAlarmTimeClick: () -> Unit,
    onDayToggle: (DayOfWeek) -> Unit,
    onCustomDateToggle: (Boolean) -> Unit,
    onCustomDateRowClick: () -> Unit,
    onSoundClick: () -> Unit,
    onSnoozeClick: () -> Unit,
    onGradualVolumeToggle: (Boolean) -> Unit,
    onVibrateToggle: (Boolean) -> Unit,
    onLabelClick: () -> Unit,
) {
    val screenCategory = currentScreenCategory()
    val draft = state.alarmDetails

    Column(modifier = modifier.fillMaxSize()) {
        ScreenDetailsTopBar(
            modifier = Modifier.padding(horizontal = 12.dp).fillMaxWidth(),
            alarmTitle = draft.label,
            canSave = state.canSave,
            onBackClick = onBackClick,
            onSaveButtonClick = onSaveClick,
            onDeleteButtonClick = onDeleteClick
        )

        if (screenCategory.isLandscapePhone || screenCategory.isExpandedWidthOrWider) {
            AlarmDetailsHorizontalContent(
                modifier = Modifier.fillMaxSize(),
                draft = draft,
                onAlarmTimeClick = onAlarmTimeClick,
                onDayToggle = onDayToggle,
                onCustomDateToggle = onCustomDateToggle,
                onCustomDateRowClick = onCustomDateRowClick,
                onSoundClick = onSoundClick,
                onSnoozeClick = onSnoozeClick,
                onGradualVolumeToggle = onGradualVolumeToggle,
                onVibrateToggle = onVibrateToggle,
                onLabelClick = onLabelClick
            )
        } else {
            AlarmDetailsVerticalContent(
                modifier = Modifier
                    .fillMaxSize()
                    .screenConstraints(screenCategory),
                draft = draft,
                onAlarmTimeClick = onAlarmTimeClick,
                onDeleteClick = onDeleteClick,
                onDayToggle = onDayToggle,
                onCustomDateToggle = onCustomDateToggle,
                onCustomDateRowClick = onCustomDateRowClick,
                onSoundClick = onSoundClick,
                onSnoozeClick = onSnoozeClick,
                onGradualVolumeToggle = onGradualVolumeToggle,
                onVibrateToggle = onVibrateToggle,
                onLabelClick = onLabelClick
            )
        }
    }
}

/** Compact-portrait and medium-width: one scrolling column, capped/centered past medium width. */
@Composable
private fun AlarmDetailsVerticalContent(
    draft: AlarmDetails,
    onAlarmTimeClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onDayToggle: (DayOfWeek) -> Unit,
    onCustomDateToggle: (Boolean) -> Unit,
    onCustomDateRowClick: () -> Unit,
    onSoundClick: () -> Unit,
    onSnoozeClick: () -> Unit,
    onGradualVolumeToggle: (Boolean) -> Unit,
    onVibrateToggle: (Boolean) -> Unit,
    onLabelClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .widthIn(max = VerticalContentMaxWidth)
            .padding(horizontal = 24.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(22.dp)
    ) {
        AlarmTimeDetails(
            modifier = Modifier.fillMaxWidth(),
            time = draft.time,
            onClick = onAlarmTimeClick
        )

        ScheduleDetails(
            modifier = Modifier.fillMaxWidth(),
            repeatDays = draft.repeatDays,
            customDateEnabled = draft.customDateEnabled,
            customDateLabel = draft.customDates.toCustomDateLabel(),
            onDayToggle = onDayToggle,
            onCustomDateToggle = onCustomDateToggle,
            onCustomDateRowClick = onCustomDateRowClick
        )

        AlarmSettingsDetails(
            modifier = Modifier.fillMaxWidth(),
            soundLabel = draft.soundName,
            snoozeLabel = draft.toSnoozeLabel(),
            gradualVolumeEnabled = draft.gradualVolumeEnabled,
            vibrateEnabled = draft.vibrateEnabled,
            alarmLabel = draft.label,
            onSoundClick = onSoundClick,
            onSnoozeClick = onSnoozeClick,
            onGradualVolumeToggle = onGradualVolumeToggle,
            onVibrateToggle = onVibrateToggle,
            onLabelClick = onLabelClick
        )

        AmPlainTextButton(
            text = "Delete alarm",
            contentColor = LocalAlarmMeColors.current.error,
            onClick = onDeleteClick
        )
    }
}

/** Landscape-phone and expanded-width: two independently-scrolling panes side by side. */
@Composable
private fun AlarmDetailsHorizontalContent(
    draft: AlarmDetails,
    onAlarmTimeClick: () -> Unit,
    onDayToggle: (DayOfWeek) -> Unit,
    onCustomDateToggle: (Boolean) -> Unit,
    onCustomDateRowClick: () -> Unit,
    onSoundClick: () -> Unit,
    onSnoozeClick: () -> Unit,
    onGradualVolumeToggle: (Boolean) -> Unit,
    onVibrateToggle: (Boolean) -> Unit,
    onLabelClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalAlarmMeColors.current
    val screenCategory = currentScreenCategory()

    Row(modifier = modifier) {
        Column(
            modifier = Modifier
                .leftPaneWidth(screenCategory)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
                .drawBehind {
                    drawLine(
                        color = colors.outline.copy(0.5f),
                        start = Offset(size.width, 0f),
                        end = Offset(size.width, size.height),
                        strokeWidth = 1f
                    )
                }
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            AlarmTimeDetails(
                modifier = Modifier.fillMaxWidth(),
                time = draft.time,
                onClick = onAlarmTimeClick
            )

            ScheduleDetails(
                modifier = Modifier.fillMaxWidth(),
                repeatDays = draft.repeatDays,
                customDateEnabled = draft.customDateEnabled,
                customDateLabel = draft.customDates.toCustomDateLabel(),
                onDayToggle = onDayToggle,
                onCustomDateToggle = onCustomDateToggle,
                onCustomDateRowClick = onCustomDateRowClick
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            AlarmSettingsDetails(
                modifier = Modifier.fillMaxWidth(),
                soundLabel = draft.soundName,
                snoozeLabel = draft.toSnoozeLabel(),
                gradualVolumeEnabled = draft.gradualVolumeEnabled,
                vibrateEnabled = draft.vibrateEnabled,
                alarmLabel = draft.label,
                onSoundClick = onSoundClick,
                onSnoozeClick = onSnoozeClick,
                onGradualVolumeToggle = onGradualVolumeToggle,
                onVibrateToggle = onVibrateToggle,
                onLabelClick = onLabelClick
            )
        }
    }
}

/** Left-pane width in [AlarmDetailsHorizontalContent], keyed off the same breakpoint the design uses. */
private val LandscapePhoneLeftPaneWidth = 352.dp
private val ExpandedLeftPaneWidth = 480.dp


private fun Modifier.leftPaneWidth(screenCategory: ScreenCategory): Modifier {
    val width = if (screenCategory.isExpandedWidthOrWider)
        ExpandedLeftPaneWidth
    else
        LandscapePhoneLeftPaneWidth

    return this.width(width)
}

private fun AlarmDetails.toSnoozeLabel(): String =
    "$snoozeDurationMinutes min · $snoozeCount times"

private fun List<LocalDate>.toCustomDateLabel(): String {
    if (isEmpty()) return "No dates set"
    val month = first().month.name.take(3).lowercase().replaceFirstChar { it.uppercase() }
    return "$month " + joinToString(", ") { it.day.toString() }
}

private val PreviewDate = LocalDate(2026, 9, 17)

private fun previewAlarmDetails(): AlarmDetails = AlarmDetails(
    id = AlarmDetailsId("weekday"),
    label = "Weekday mornings",
    time = LocalDateTime(PreviewDate, LocalTime(6, 40)),
    repeatDays = setOf(
        DayOfWeek.MONDAY,
        DayOfWeek.TUESDAY,
        DayOfWeek.WEDNESDAY,
        DayOfWeek.THURSDAY,
        DayOfWeek.FRIDAY
    ),
    customDateEnabled = false,
    customDates = listOf(
        LocalDate(2026, 9, 20),
        LocalDate(2026, 9, 24),
        LocalDate(2026, 9, 28)
    ),
    soundName = "Sunrise chime",
    snoozeDurationMinutes = 9,
    snoozeCount = 3,
    gradualVolumeEnabled = true,
    vibrateEnabled = false,
)

@Composable
private fun PreviewAlarmDetailsScreen(state: AlarmDetailsState) {
    AlarmMeThemeProvider {
        AlarmDetailsScreenContent(
            modifier = Modifier
                .fillMaxSize()
                .background(LocalAlarmMeColors.current.background),
            state = state,
            onBackClick = {},
            onSaveClick = {},
            onDeleteClick = {},
            onAlarmTimeClick = {},
            onDayToggle = {},
            onCustomDateToggle = {},
            onCustomDateRowClick = {},
            onSoundClick = {},
            onSnoozeClick = {},
            onGradualVolumeToggle = {},
            onVibrateToggle = {},
            onLabelClick = {},
            onRetry = {},
            onDismiss = {}
        )
    }
}

/**
 * The three common buckets cover phone-portrait (single scrolling column), phone-landscape via
 * compact height (two-pane split), and expanded width (wider two-pane split).
 */
@CommonScreenCategoriesPreview
@Composable
private fun AlarmDetailsScreenContentPreview() {
    val details = previewAlarmDetails()
    PreviewAlarmDetailsScreen(AlarmDetailsState.Loaded(alarmDetails = details, canSave = false))
}

/** A dirty draft (custom date toggled on) so Save shows enabled. */
@CommonScreenCategoriesPreview
@Composable
private fun AlarmDetailsScreenContentUnsavedChangesPreview() {
    val details = previewAlarmDetails()
    PreviewAlarmDetailsScreen(
        AlarmDetailsState.Loaded(
            alarmDetails = details.copy(customDateEnabled = true),
            canSave = true
        )
    )
}

@CommonScreenCategoriesPreview
@Composable
private fun AlarmDetailsScreenContentLoadingPreview() {
    PreviewAlarmDetailsScreen(AlarmDetailsState.Loading)
}

@CommonScreenCategoriesPreview
@Composable
private fun AlarmDetailsScreenContentErrorPreview() {
    PreviewAlarmDetailsScreen(AlarmDetailsState.Error)
}
