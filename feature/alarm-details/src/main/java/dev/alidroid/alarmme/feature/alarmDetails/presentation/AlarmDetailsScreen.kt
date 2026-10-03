package dev.alidroid.alarmme.feature.alarmDetails.presentation

import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import dev.alidroid.alarmme.alarms.domain.AlarmId
import dev.alidroid.alarmme.alarms.domain.Ringtone
import dev.alidroid.alarmme.alarms.domain.RingtoneId
import dev.alidroid.alarmme.feature.alarmDetails.presentation.component.AlarmSettingsDetails
import dev.alidroid.alarmme.feature.alarmDetails.presentation.component.AlarmTimeDetails
import dev.alidroid.alarmme.feature.alarmDetails.presentation.component.ScheduleDetails
import dev.alidroid.alarmme.feature.alarmDetails.presentation.component.ScreenDetailsTopBar
import dev.alidroid.alarmme.feature.alarmDetails.presentation.model.AlarmDetailsEffect
import dev.alidroid.alarmme.feature.alarmDetails.presentation.model.AlarmDetailsEvent
import dev.alidroid.alarmme.feature.alarmDetails.presentation.model.AlarmDetailsState
import dev.alidroid.alarmme.feature.alarmDetails.presentation.model.AlarmDetailsUiState
import dev.alidroid.alarmme.navigation.AlarmDetailsParams
import dev.alidroid.alarmme.navigation.AwaitNavResult
import dev.alidroid.alarmme.navigation.LocalNavGraph
import dev.alidroid.alarmme.navigation.NavDestination
import dev.alidroid.alarmme.navigation.navigateTo
import dev.alidroid.alarmme.ui.screen.ringtonePicker.presentation.RingtonePickerScreen
import dev.alidroid.alarmme.ui.component.AmPlainTextButton
import dev.alidroid.alarmme.ui.component.ErrorScreenContent
import dev.alidroid.alarmme.ui.screen.datePicker.VerticalDatePickerBottomSheet
import dev.alidroid.alarmme.ui.screen.datePicker.VerticalDatePickerDialog
import dev.alidroid.alarmme.ui.screen.timePicker.TimePickerBottomSheet
import dev.alidroid.alarmme.ui.screen.timePicker.TimePickerDialog
import dev.alidroid.alarmme.ui.screenCategory.CommonScreenCategoriesPreview
import dev.alidroid.alarmme.ui.screenCategory.ScreenCategory
import dev.alidroid.alarmme.ui.screenCategory.currentScreenCategory
import dev.alidroid.alarmme.ui.screenCategory.isExpandedWidthOrWider
import dev.alidroid.alarmme.ui.screenCategory.isLandscapePhone
import dev.alidroid.alarmme.ui.screenCategory.isMediumWidth
import dev.alidroid.alarmme.ui.screenCategory.isPhone
import dev.alidroid.alarmme.ui.theme.AlarmMeThemeProvider
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeColors
import dev.alidroid.alarmme.utility.kotlinDateTime.toGroupedLabel
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import org.koin.core.parameter.parametersOf
import kotlin.time.Duration.Companion.seconds

class AlarmDetailsScreen(
    val params: AlarmDetailsParams,
    val ringtonePickerDestination: NavDestination
) : Screen {

    sealed interface StartMode {
        data object New : StartMode
        data class Edit(val alarmId: AlarmId) : StartMode
    }

    @Composable
    override fun Content() {
        val startMode = remember(params) {
            params.alarmId?.let { StartMode.Edit(it) } ?: StartMode.New
        }
        val screenModel: AlarmDetailsScreenModel =
            koinScreenModel(parameters = { parametersOf(startMode) })
        val state by screenModel.state.collectAsState()
        val dispatch = screenModel::handleEvent
        val navigator = LocalNavigator.current
        val context = LocalContext.current
        val navGraph = LocalNavGraph.current
        val missingNavigatorToast = remember {
            Toast.makeText(context, "Navigator not found, can not navigate", Toast.LENGTH_SHORT)
        }

        AwaitNavResult(RingtonePickerScreen.RINGTONE_NAV_RESULT_KEY) { result ->
            dispatch(AlarmDetailsEvent.OnRingtoneUpdated(result.ringtone))
        }

        LaunchedEffect(screenModel) {
            screenModel.effect.collect { effect ->
                when (effect) {
                    // The alarm was deleted, so there is nothing left on this screen to show.
                    AlarmDetailsEffect.NavigateBack -> navigator?.pop()
                }
            }
        }

        AlarmDetailsScreenContent(
            modifier = Modifier
                .imePadding()
                .systemBarsPadding()
                .fillMaxSize()
                .background(LocalAlarmMeColors.current.background),
            state = state,
            onBackClick = { navigator?.pop() ?: missingNavigatorToast.show() },
            onAlarmTimeChanged = { time -> dispatch(AlarmDetailsEvent.OnAlarmTimeChanged(time)) },
            onSaveClick = { dispatch(AlarmDetailsEvent.OnSaveClick) },
            onDeleteClick = { dispatch(AlarmDetailsEvent.OnDeleteClick) },
            onDayToggle = { day -> dispatch(AlarmDetailsEvent.OnDayToggle(day)) },
            onCustomDateToggle = { enabled -> dispatch(AlarmDetailsEvent.OnCustomDateToggle(enabled)) },
            onCustomDatesChanged = { dates -> dispatch(AlarmDetailsEvent.OnCustomDatesChanged(dates)) },
            onSoundClick = { navigator?.navigateTo(ringtonePickerDestination, navGraph)  ?: missingNavigatorToast.show() },
            onSnoozeClick = { dispatch(AlarmDetailsEvent.OnSnoozeClick) },
            onGradualVolumeToggle = { enabled -> dispatch(AlarmDetailsEvent.OnGradualVolumeToggle(enabled)) },
            onVibrateToggle = { enabled -> dispatch(AlarmDetailsEvent.OnVibrateToggle(enabled)) },
            onLabelClick = { dispatch(AlarmDetailsEvent.OnLabelClick) },
            onRetry = { dispatch(AlarmDetailsEvent.OnRetry) },
            onDismiss = { navigator?.pop() ?: missingNavigatorToast.show()}
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
    onAlarmTimeChanged: (LocalTime) -> Unit,
    onDeleteClick: () -> Unit,
    onDayToggle: (DayOfWeek) -> Unit,
    onCustomDateToggle: (Boolean) -> Unit,
    onCustomDatesChanged: (Set<LocalDate>) -> Unit,
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
            onAlarmTimeChanged = onAlarmTimeChanged,
            onDayToggle = onDayToggle,
            onCustomDateToggle = onCustomDateToggle,
            onCustomDatesChanged = onCustomDatesChanged,
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
    onAlarmTimeChanged: (LocalTime) -> Unit,
    onDayToggle: (DayOfWeek) -> Unit,
    onCustomDateToggle: (Boolean) -> Unit,
    onCustomDatesChanged: (Set<LocalDate>) -> Unit,
    onSoundClick: () -> Unit,
    onSnoozeClick: () -> Unit,
    onGradualVolumeToggle: (Boolean) -> Unit,
    onVibrateToggle: (Boolean) -> Unit,
    onLabelClick: () -> Unit,
) {
    val screenCategory = currentScreenCategory()
    val details = state.details
    var showCustomDatePicker by rememberSaveable { mutableStateOf(false) }
    var showTimePicker by rememberSaveable { mutableStateOf(false) }
    val isHorizontalScreen = screenCategory.isLandscapePhone || screenCategory.isExpandedWidthOrWider

    Column(modifier = modifier.fillMaxSize()) {
        ScreenDetailsTopBar(
            modifier = Modifier.padding(horizontal = 12.dp).fillMaxWidth(),
            alarmTitle = details.label,
            canSave = state.canSave,
            onBackClick = onBackClick,
            onSaveButtonClick = onSaveClick,
            onDeleteButtonClick = onDeleteClick
        )

        if (isHorizontalScreen) {
            AlarmDetailsHorizontalContent(
                modifier = Modifier.fillMaxSize(),
                details = details,
                onAlarmTimeClick = { showTimePicker = true },
                onDayToggle = onDayToggle,
                onCustomDateToggle = onCustomDateToggle,
                onCustomDateRowClick = { showCustomDatePicker = true },
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
                details = details,
                onAlarmTimeClick = { showTimePicker = true },
                onDeleteClick = onDeleteClick,
                onDayToggle = onDayToggle,
                onCustomDateToggle = onCustomDateToggle,
                onCustomDateRowClick = { showCustomDatePicker = true },
                onSoundClick = onSoundClick,
                onSnoozeClick = onSnoozeClick,
                onGradualVolumeToggle = onGradualVolumeToggle,
                onVibrateToggle = onVibrateToggle,
                onLabelClick = onLabelClick
            )
        }
    }

    if (showCustomDatePicker) {
        var draftDates by remember { mutableStateOf(details.customDates.toSet()) }

        AlarmDatePicker(
            isHorizontal = isHorizontalScreen,
            draftDates = draftDates,
            onDateToggle = { date ->
                draftDates = if (date in draftDates) draftDates - date else draftDates + date
            },
            onSave = { dates ->
                onCustomDatesChanged(dates)
                showCustomDatePicker = false
            },
            onDismiss = { showCustomDatePicker = false }
        )
    }

    if (showTimePicker) {
        var draftHour by remember { mutableStateOf(details.time.toHour12()) }
        var draftMinute by remember { mutableStateOf(details.time.minute) }
        var draftIsAm by remember { mutableStateOf(details.time.isAm()) }

        AlarmTimePicker(
            isHorizontal = isHorizontalScreen,
            hour = draftHour,
            minute = draftMinute,
            isAm = draftIsAm,
            onHourChange = { draftHour = it },
            onMinuteChange = { draftMinute = it },
            onAmPmChange = { draftIsAm = it },
            onSave = {
                onAlarmTimeChanged(toLocalTime(draftHour, draftMinute, draftIsAm))
                showTimePicker = false
            },
            onDismiss = { showTimePicker = false }
        )
    }
}

@Composable
fun AlarmDatePicker(
    isHorizontal: Boolean,
    draftDates: Set<LocalDate>,
    onDateToggle: (LocalDate) -> Unit,
    onSave: (Set<LocalDate>) -> Unit,
    onDismiss: () -> Unit
) {
    if (isHorizontal) {
        VerticalDatePickerDialog(
            selectedDates = draftDates,
            onDateToggle = onDateToggle,
            onSave = onSave,
            onDismiss = onDismiss
        )
    } else {
        VerticalDatePickerBottomSheet(
            selectedDates = draftDates,
            onDateToggle = onDateToggle,
            onSave = onSave,
            onDismiss = onDismiss
        )
    }
}

@Composable
fun AlarmTimePicker(
    isHorizontal: Boolean,
    hour: Int,
    minute: Int,
    isAm: Boolean,
    onHourChange: (Int) -> Unit,
    onMinuteChange: (Int) -> Unit,
    onAmPmChange: (Boolean) -> Unit,
    onSave: () -> Unit,
    onDismiss: () -> Unit
) {
    if (isHorizontal) {
        TimePickerDialog(
            hour = hour,
            minute = minute,
            isAm = isAm,
            onHourChange = onHourChange,
            onMinuteChange = onMinuteChange,
            onAmPmChange = onAmPmChange,
            onSave = onSave,
            onDismiss = onDismiss
        )
    } else {
        TimePickerBottomSheet(
            hour = hour,
            minute = minute,
            isAm = isAm,
            onHourChange = onHourChange,
            onMinuteChange = onMinuteChange,
            onAmPmChange = onAmPmChange,
            onSave = onSave,
            onDismiss = onDismiss
        )
    }
}

/** Compact-portrait and medium-width: one scrolling column, capped/centered past medium width. */
@Composable
private fun AlarmDetailsVerticalContent(
    details: AlarmDetailsUiState,
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
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(22.dp)
    ) {
        AlarmTimeDetails(
            modifier = Modifier.fillMaxWidth(),
            time = details.time,
            nextOccurrence = details.nextOccurrence,
            onClick = onAlarmTimeClick
        )

        ScheduleDetails(
            modifier = Modifier.fillMaxWidth(),
            repeatDays = details.repeatDays,
            customDateEnabled = details.customDateEnabled,
            customDateLabel = details.customDates.toCustomDateLabel(),
            onDayToggle = onDayToggle,
            onCustomDateToggle = onCustomDateToggle,
            onCustomDateRowClick = onCustomDateRowClick
        )

        AlarmSettingsDetails(
            modifier = Modifier.fillMaxWidth(),
            soundLabel = details.ringtone.name,
            snoozeLabel = details.toSnoozeLabel(),
            gradualVolumeEnabled = details.gradualVolumeEnabled,
            vibrateEnabled = details.vibrateEnabled,
            alarmLabel = details.label,
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
    details: AlarmDetailsUiState,
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
                time = details.time,
                nextOccurrence = details.nextOccurrence,
                onClick = onAlarmTimeClick
            )

            ScheduleDetails(
                modifier = Modifier.fillMaxWidth(),
                repeatDays = details.repeatDays,
                customDateEnabled = details.customDateEnabled,
                customDateLabel = details.customDates.toCustomDateLabel(),
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
                soundLabel = details.ringtone.name,
                snoozeLabel = details.toSnoozeLabel(),
                gradualVolumeEnabled = details.gradualVolumeEnabled,
                vibrateEnabled = details.vibrateEnabled,
                alarmLabel = details.label,
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

private fun AlarmDetailsUiState.toSnoozeLabel(): String =
    "$snoozeDurationMinutes min · $snoozeCount times"

private fun List<LocalDate>.toCustomDateLabel(): String =
    if (isEmpty()) "No dates set" else toGroupedLabel()

private fun LocalTime.toHour12(): Int {
    val h = hour % 12
    return if (h == 0) 12 else h
}

private fun LocalTime.isAm(): Boolean = hour < 12

private fun toLocalTime(hour12: Int, minute: Int, isAm: Boolean): LocalTime {
    val hour24 = when {
        isAm && hour12 == 12 -> 0
        !isAm && hour12 != 12 -> hour12 + 12
        else -> hour12
    }
    return LocalTime(hour24, minute)
}

private fun previewDetails(): AlarmDetailsUiState = AlarmDetailsUiState(
    id = AlarmId("weekday"),
    label = "Weekday mornings",
    time = LocalTime(6, 40),
    nextOccurrence = LocalDateTime(2026, 9, 18, 6, 40),
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
    ringtone = Ringtone(
        id = RingtoneId("sunrise-chime"),
        name = "Sunrise chime",
        uri = "content://dev.alidroid.alarmme.ringtones/sunrise-chime",
        duration = 42.seconds,
    ),
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
            onAlarmTimeChanged = {},
            onDayToggle = {},
            onCustomDateToggle = {},
            onCustomDatesChanged = {},
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
    PreviewAlarmDetailsScreen(AlarmDetailsState.Loaded(details = previewDetails(), canSave = false))
}

/** A dirty draft (custom date toggled on) so Save shows enabled. */
@CommonScreenCategoriesPreview
@Composable
private fun AlarmDetailsScreenContentUnsavedChangesPreview() {
    PreviewAlarmDetailsScreen(
        AlarmDetailsState.Loaded(
            details = previewDetails().copy(customDateEnabled = true),
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
