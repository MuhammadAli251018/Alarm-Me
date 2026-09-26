package com.muhammadali.alarmme.feature.alarmList.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import com.muhammadali.alarmme.alarms.domain.AlarmId
import com.muhammadali.alarmme.feature.alarmList.presentation.component.AlarmItemsList
import com.muhammadali.alarmme.feature.alarmList.presentation.component.AlarmItemsListState
import com.muhammadali.alarmme.feature.alarmList.presentation.component.AlarmsListSkeleton
import com.muhammadali.alarmme.feature.alarmList.presentation.component.AlarmsListTopBar
import com.muhammadali.alarmme.feature.alarmList.presentation.component.TopBarSkeleton
import com.muhammadali.alarmme.feature.alarmList.presentation.component.rememberAlarmItemsListState
import com.muhammadali.alarmme.feature.alarmList.presentation.model.AlarmItemState
import com.muhammadali.alarmme.feature.alarmList.presentation.model.AlarmsListEvent
import com.muhammadali.alarmme.feature.alarmList.presentation.model.AlarmsListState
import com.muhammadali.alarmme.navigation.NavDestination
import com.muhammadali.alarmme.navigation.navigateTo
import com.muhammadali.alarmme.ui.component.AmDestructiveTextButton
import com.muhammadali.alarmme.ui.component.AmFabButton
import com.muhammadali.alarmme.ui.component.AmFabExtendedButton
import com.muhammadali.alarmme.ui.component.AmIcon
import com.muhammadali.alarmme.ui.component.AmIconDefaults
import com.muhammadali.alarmme.ui.component.AmOutlineTextButton
import com.muhammadali.alarmme.ui.component.ErrorScreenContent
import com.muhammadali.alarmme.ui.screenCategory.CommonScreenCategoriesPreview
import com.muhammadali.alarmme.ui.screenCategory.CompactWidthMediumHeightPreview
import com.muhammadali.alarmme.ui.screenCategory.MediumWidthMediumHeightPreview
import com.muhammadali.alarmme.ui.screenCategory.WidthCategory
import com.muhammadali.alarmme.ui.screenCategory.currentScreenCategory
import com.muhammadali.alarmme.ui.screenCategory.isExpandedWidthOrWider
import com.muhammadali.alarmme.ui.screenCategory.isLandscapePhone
import com.muhammadali.alarmme.ui.screenCategory.isPhone
import com.muhammadali.alarmme.ui.screenCategory.isPortraitPhone
import com.muhammadali.alarmme.ui.theme.AlarmMeIcons
import com.muhammadali.alarmme.ui.theme.AlarmMeThemeProvider
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeColors
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

class AlarmListScreen(
    private val alarmDetailsDestination: (AlarmId?) -> NavDestination
) : Screen {

    @Composable
    override fun Content() {
        val screenModel: AlarmsListScreenModel = koinScreenModel()
        val state by screenModel.state.collectAsState()
        val dispatch = screenModel::handleEvent
        val navigator = LocalNavigator.current


        AlarmListScreenContent(
            modifier = Modifier
                .imePadding()
                .systemBarsPadding()
                .fillMaxSize()
                .background(LocalAlarmMeColors.current.background),
            state = state,
            onAlarmClick = { id -> navigator?.navigateTo(alarmDetailsDestination(id)) },
            onAlarmCheckedChange = { id, enabled ->
                dispatch(AlarmsListEvent.OnEnableOrDisableAlarm(id, enabled))
            },
            onNewAlarmClick = { navigator?.navigateTo(alarmDetailsDestination(null)) },
            onDisableSelected = { ids ->
                dispatch(AlarmsListEvent.OnEnableOrDisableAlarms(ids = ids, enabled = false))
            },
            onDeleteSelected = { ids -> dispatch(AlarmsListEvent.OnDeleteAlarms(ids)) },
            onRetry = { dispatch(AlarmsListEvent.OnRetry) },
            onDismiss = { /*TODO*/ }
        )
    }
}

@Composable
fun AlarmListScreenContent(
    state: AlarmsListState,
    onAlarmClick: (AlarmId) -> Unit,
    onAlarmCheckedChange: (AlarmId, Boolean) -> Unit,
    onNewAlarmClick: () -> Unit,
    onDisableSelected: (Set<AlarmId>) -> Unit,
    onDeleteSelected: (Set<AlarmId>) -> Unit,
    onRetry: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (state) {
        AlarmsListState.Error -> ErrorAlarmListScreenContent(modifier, onRetry, onDismiss)
        AlarmsListState.Loading -> LoadingAlarmListScreenContent(modifier)
        is AlarmsListState.Loaded -> LoadedAlarmListScreenContent(
            alarms = state.alarms,
            nextAlarmTime = state.nextAlarmTime,
            onAlarmClick = onAlarmClick,
            onAlarmCheckedChange = onAlarmCheckedChange,
            onNewAlarmClick = onNewAlarmClick,
            onDisableSelected = onDisableSelected,
            onDeleteSelected = onDeleteSelected,
            modifier = modifier
        )
    }
}

@Composable
fun LoadedAlarmListScreenContent(
    alarms: List<AlarmItemState>,
    nextAlarmTime: LocalDateTime?,
    onAlarmClick: (AlarmId) -> Unit,
    onAlarmCheckedChange: (AlarmId, Boolean) -> Unit,
    onNewAlarmClick: () -> Unit,
    onDisableSelected: (Set<AlarmId>) -> Unit,
    onDeleteSelected: (Set<AlarmId>) -> Unit,
    modifier: Modifier = Modifier,
    listState: AlarmItemsListState = rememberAlarmItemsListState(),
) {
    val selectedIds = listState.selectedAlarmItems

    Box(modifier) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxSize()
                .widthIn(max = contentMaxWidth())
        ) {
            AlarmsListTopBar(
                modifier = Modifier.padding().fillMaxWidth(),
                selectedAlarmsCount = selectedIds.size,
                nextAlarmTime = nextAlarmTime,
                onDismissSelection = { listState.clear() },
                onSelectAll = { listState.addAll(alarms.map { it.id }) },
                onSelectionClick = {}, // TODO: IMPLEMENT
                onNewAlarmClick = onNewAlarmClick
            )

            AlarmItemsList(
                alarmItems = alarms,
                onAlarmClick = onAlarmClick,
                onAlarmCheckedChange = onAlarmCheckedChange,
                modifier = Modifier.weight(1f),
                alarmsListState = listState
            )
        }

        BottomFade(Modifier.align(Alignment.BottomCenter))

        if (listState.isSelectionMode) {
            SelectionActionBar(
                modifier = Modifier.align(Alignment.BottomCenter),
                selectedCount = selectedIds.size,
                onDisable = {
                    onDisableSelected(selectedIds)
                    listState.clear()
                },
                onDelete = {
                    onDeleteSelected(selectedIds)
                    listState.clear()
                }
            )
        }

        NewAlarmFabButton(
            modifier = Modifier.align(Alignment.BottomEnd),
            onNewAlarmClick = onNewAlarmClick
        )
    }
}

@Composable
fun NewAlarmFabButton(modifier: Modifier = Modifier, onNewAlarmClick: () -> Unit) {
    val screenCategory = currentScreenCategory()

    when {
        screenCategory.isPortraitPhone -> AmFabButton(
            modifier = modifier.padding(end = 22.dp, bottom = 26.dp),
            onClick = onNewAlarmClick
        ) {
            AmIcon(AlarmMeIcons.Add, contentDescription = "New alarm", size = AmIconDefaults.Large)
        }

        screenCategory.width == WidthCategory.Medium && !screenCategory.isLandscapePhone -> AmFabExtendedButton(
            text = "New alarm",
            modifier = modifier.padding(end = 32.dp, bottom = 30.dp),
            icon = { AmIcon(AlarmMeIcons.Add, null) },
            onClick = onNewAlarmClick
        )
    }
}

@Composable
fun alarmsListHorizontalPaddings(): Dp {
    val screenCategory = currentScreenCategory()
    return when {
        screenCategory.isPhone -> 20.dp
        screenCategory.width == WidthCategory.Medium -> 32.dp
        screenCategory.isExpandedWidthOrWider -> 48.dp
        else -> 20.dp
    }
}

/**
 * Replaces the FAB while alarms are selected. Delete is the destructive half and takes the error
 * container; both actions leave selection mode once they have fired.
 */
@Composable
private fun SelectionActionBar(
    selectedCount: Int,
    onDisable: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalAlarmMeColors.current

    Column(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(colors.outlineVariant)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(colors.surfaceContainer)
                .padding(start = 16.dp, end = 16.dp, top = 14.dp, bottom = 22.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            AmOutlineTextButton(
                text = "Disable $selectedCount",
                modifier = Modifier.weight(1f),
                onClick = onDisable
            )
            AmDestructiveTextButton(
                text = "Delete $selectedCount",
                modifier = Modifier.weight(1f),
                onClick = onDelete
            )
        }
    }
}

@Composable
fun BottomFade(modifier: Modifier) {
    val colors = LocalAlarmMeColors.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(bottomFadeHeight())
            .background(
                Brush.verticalGradient(
                    0f to Color.Transparent,
                    0.66f to colors.background,
                    1f to colors.background
                )
            )
    )
}

@Composable
fun ErrorAlarmListScreenContent(
    modifier: Modifier = Modifier,
    onRetry: () -> Unit,
    onDismiss: () -> Unit
) {
    ErrorScreenContent(
        modifier = modifier,
        errorHeading = "Couldn't load your alarms",
        errorDescription = "The alarm database didn't open. Your alarms are still saved — " +
            "nothing was deleted.",
        onRetry = onRetry,
        onDismiss = onDismiss
    )
}

/**
 * Alarms load from Room in well under 100ms, so this is a skeleton rather than a spinner — a
 * shimmering list reads calmer than a progress indicator that barely gets to show itself.
 *
 * It is deliberately the same shape as [LoadedAlarmListScreenContent]: same width cap, same bar,
 * same grid, so nothing shifts when the alarms arrive. The FAB is left out — it is chrome the
 * skeleton has no data to wait for, and a placeholder would only disable an action that works.
 */
@Composable
fun LoadingAlarmListScreenContent(modifier: Modifier = Modifier) {
    Box(modifier) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxSize()
                .widthIn(max = contentMaxWidth())
        ) {
            TopBarSkeleton(modifier = Modifier.fillMaxWidth())

            AlarmsListSkeleton(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
internal fun contentMaxWidth(): Dp =
    if (currentScreenCategory().isExpandedWidthOrWider) 1040.dp else 560.dp

/** Shorter on expanded, where the list ends well above the bottom edge. */
@Composable
private fun bottomFadeHeight(): Dp =
    if (currentScreenCategory().isExpandedWidthOrWider) 80.dp else 110.dp

private fun previewAlarms(): List<AlarmItemState> = listOf(
    AlarmItemState(AlarmId("gym"), true, LocalTime(5, 30), "Mon, Wed, Fri · Gym — leave by six"),
    AlarmItemState(AlarmId("weekday"), true, LocalTime(6, 40), "Mon–Fri · Weekday mornings, gently"),
    AlarmItemState(AlarmId("standup"), true, LocalTime(9, 55), "Mon–Fri · Standup in five"),
    AlarmItemState(AlarmId("meds"), true, LocalTime(12, 0), "Every day"),
    AlarmItemState(AlarmId("bread"), false, LocalTime(13, 5), "Thu 10 Sep · Take the bread out"),
    AlarmItemState(AlarmId("laundry"), false, LocalTime(15, 20), "Sun · Move the laundry over before it sits too long"),
    AlarmItemState(AlarmId("bins"), true, LocalTime(19, 10), "Wed · Bins out"),
    AlarmItemState(AlarmId("winddown"), false, LocalTime(21, 45), "Every day · Wind down, screens off"),
    AlarmItemState(AlarmId("kettle"), true, LocalTime(22, 30), "Every day · Kettle off"),
)

@Composable
private fun PreviewLoaded(
    alarms: List<AlarmItemState> = previewAlarms(),
    listState: AlarmItemsListState = rememberAlarmItemsListState(),
) {
    AlarmMeThemeProvider {
        LoadedAlarmListScreenContent(
            alarms = alarms,
            nextAlarmTime = LocalDateTime(2026, 9, 11, 6, 40),
            onAlarmClick = {},
            onAlarmCheckedChange = { _, _ -> },
            onNewAlarmClick = {},
            onDisableSelected = {},
            onDeleteSelected = {},
            modifier = Modifier.fillMaxSize().background(LocalAlarmMeColors.current.background),
            listState = listState
        )
    }
}

/**
 * The three buckets cover the whole FAB split: compact portrait gets the squircle, compact
 * landscape and expanded get nothing because their top bar already carries "New alarm".
 */
@CommonScreenCategoriesPreview
@Composable
private fun LoadedAlarmListScreenContentPreview() {
    PreviewLoaded()
}

/** The only bucket that renders the extended FAB. */
@MediumWidthMediumHeightPreview
@Composable
private fun LoadedAlarmListScreenContentMediumPreview() {
    PreviewLoaded()
}

/** FAB gives way to the two-action bar, and the top bar swaps to its selection variant. */
@CompactWidthMediumHeightPreview
@Composable
private fun LoadedAlarmListScreenContentSelectionPreview() {
    PreviewLoaded(
        listState = rememberAlarmItemsListState(
            selectedAlarmItems = listOf(AlarmId("gym"), AlarmId("bread"), AlarmId("bins"))
        )
    )
}

/** Nothing scheduled yet — the pane should still hold its bar and FAB. */
@CompactWidthMediumHeightPreview
@Composable
private fun LoadedAlarmListScreenContentEmptyPreview() {
    PreviewLoaded(alarms = emptyList())
}

@CommonScreenCategoriesPreview
@Composable
private fun LoadingAlarmListScreenContentPreview() {
    AlarmMeThemeProvider {
        LoadingAlarmListScreenContent(
            modifier = Modifier.fillMaxSize().background(LocalAlarmMeColors.current.background)
        )
    }
}
