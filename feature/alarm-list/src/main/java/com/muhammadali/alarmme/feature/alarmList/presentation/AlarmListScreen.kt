package com.muhammadali.alarmme.feature.alarmList.presentation

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import com.muhammadali.alarmme.feature.alarmList.presentation.component.AlarmItem
import com.muhammadali.alarmme.feature.alarmList.presentation.component.AlarmItemId
import com.muhammadali.alarmme.feature.alarmList.presentation.component.AlarmItemsList
import com.muhammadali.alarmme.feature.alarmList.presentation.component.AlarmItemsListState
import com.muhammadali.alarmme.feature.alarmList.presentation.component.AlarmsListTopBar
import com.muhammadali.alarmme.feature.alarmList.presentation.component.rememberAlarmItemsListState
import com.muhammadali.alarmme.feature.alarmList.presentation.model.AlarmsListEvent
import com.muhammadali.alarmme.feature.alarmList.presentation.model.AlarmsListState
import com.muhammadali.alarmme.ui.component.AmFabButton
import com.muhammadali.alarmme.ui.component.AmFabExtendedButton
import com.muhammadali.alarmme.ui.component.AmFilledTextButton
import com.muhammadali.alarmme.ui.component.AmOutlineTextButton
import com.muhammadali.alarmme.ui.component.BaseTextButton
import com.muhammadali.alarmme.ui.screenCategory.CommonScreenCategoriesPreview
import com.muhammadali.alarmme.ui.screenCategory.CompactWidthMediumHeightPreview
import com.muhammadali.alarmme.ui.screenCategory.HeightCategory
import com.muhammadali.alarmme.ui.screenCategory.MediumWidthMediumHeightPreview
import com.muhammadali.alarmme.ui.screenCategory.WidthCategory
import com.muhammadali.alarmme.ui.screenCategory.currentScreenCategory
import com.muhammadali.alarmme.ui.screenCategory.isExpandedWidthOrWider
import com.muhammadali.alarmme.ui.screenCategory.isLandscapePhone
import com.muhammadali.alarmme.ui.screenCategory.isPhone
import com.muhammadali.alarmme.ui.screenCategory.isPortraitPhone
import com.muhammadali.alarmme.ui.theme.AlarmMeThemeProvider
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeColors
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeShapes
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeTypography
import kotlinx.datetime.LocalDateTime

class AlarmListScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel: AlarmsListScreenModel = rememberScreenModel{ AlarmsListScreenModel() }
        val state by viewModel.state.collectAsState()
        val dispatch = viewModel::handleEvent

        AlarmListScreenContent(
            state = state,
            onAlarmClick = {},
            onAlarmCheckedChange = { _, _ -> },
            onNewAlarmClick = {},
            onDisableSelected = {},
            onDeleteSelected = { ids -> dispatch(AlarmsListEvent.OnDeleteAlarms(ids.map { it.value })) },
            onRetry = {},
            modifier = Modifier.fillMaxSize().background(LocalAlarmMeColors.current.background)
        )
    }
}

@Composable
fun AlarmListScreenContent(
    state: AlarmsListState,
    onAlarmClick: (AlarmItemId) -> Unit,
    onAlarmCheckedChange: (AlarmItemId, Boolean) -> Unit,
    onNewAlarmClick: () -> Unit,
    onDisableSelected: (List<AlarmItemId>) -> Unit,
    onDeleteSelected: (List<AlarmItemId>) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (state) {
        AlarmsListState.Error -> ErrorAlarmListScreenContent(onRetry, modifier)
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
    alarms: List<AlarmItem>,
    nextAlarmTime: LocalDateTime?,
    onAlarmClick: (AlarmItemId) -> Unit,
    onAlarmCheckedChange: (AlarmItemId, Boolean) -> Unit,
    onNewAlarmClick: () -> Unit,
    onDisableSelected: (List<AlarmItemId>) -> Unit,
    onDeleteSelected: (List<AlarmItemId>) -> Unit,
    modifier: Modifier = Modifier,
    listState: AlarmItemsListState = rememberAlarmItemsListState(),
) {
    val screenCategory = currentScreenCategory()
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
                onSelectionClick = {},
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

        when {
            listState.isSelectionMode -> SelectionActionBar(
                selectedCount = selectedIds.size,
                onDisable = {
                    onDisableSelected(selectedIds.toList())
                    listState.clear()
                },
                onDelete = {
                    onDeleteSelected(selectedIds.toList())
                    listState.clear()
                },
                modifier = Modifier.align(Alignment.BottomCenter)
            )

            screenCategory.isPortraitPhone -> AmFabButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 22.dp, bottom = 26.dp),
                onClick = onNewAlarmClick
            ) {
                Text(text = "＋")
            }

            screenCategory.width == WidthCategory.Medium && !screenCategory.isLandscapePhone -> AmFabExtendedButton(
                text = "New alarm",
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 32.dp, bottom = 30.dp),
                icon = { Text(text = "＋") },
                onClick = onNewAlarmClick
            )
        }
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
            BaseTextButton(
                text = "Delete $selectedCount",
                containerColor = colors.errorContainer,
                contentColor = colors.onErrorContainer,
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
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalAlarmMeColors.current
    val typography = LocalAlarmMeTypography.current

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = contentMaxWidth())
                .padding(horizontal = 22.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(LocalAlarmMeShapes.current.full)
                    .background(colors.outlineVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "✕",
                    style = typography.titleMedium,
                    color = colors.outline
                )
            }

            Text(
                text = "Couldn't load your alarms",
                style = typography.titleLarge,
                color = colors.onBackground,
                textAlign = TextAlign.Center
            )

            Text(
                text = "The alarm database didn't open. Your alarms are still saved — " +
                    "nothing was deleted.",
                style = typography.bodyMedium,
                color = colors.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            AmFilledTextButton(text = "Try again", onClick = onRetry)
        }
    }
}

/**
 * Alarms load from Room in well under 100ms, so this is a skeleton rather than a spinner — a
 * shimmering list reads calmer than a progress indicator that barely gets to show itself.
 */
@Composable
fun LoadingAlarmListScreenContent(modifier: Modifier = Modifier) {
    val colors = LocalAlarmMeColors.current
    val shapes = LocalAlarmMeShapes.current

    val transition = rememberInfiniteTransition(label = "skeleton")
    val shimmer by transition.animateFloat(
        initialValue = 0.45f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(900),
            repeatMode = RepeatMode.Reverse
        ),
        label = "skeletonAlpha"
    )

    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .widthIn(max = contentMaxWidth())
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 34.dp)
                .alpha(shimmer)
        ) {
            SkeletonBar(width = 150.dp, height = 11.dp)
            Spacer(modifier = Modifier.height(8.dp))
            SkeletonBar(width = 110.dp, height = 26.dp)

            Spacer(modifier = Modifier.height(22.dp))

            SkeletonRowWidths.forEach { (timeWidth, subtitleWidth) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shapes.large)
                        .background(colors.surface)
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(shapes.full)
                            .background(colors.surfaceContainer)
                    )
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(7.dp)
                    ) {
                        SkeletonBar(width = timeWidth, height = 22.dp)
                        SkeletonBar(width = subtitleWidth, height = 10.dp)
                    }
                    Box(
                        modifier = Modifier
                            .width(52.dp)
                            .height(32.dp)
                            .clip(shapes.full)
                            .background(colors.surfaceContainer)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

private val SkeletonRowWidths = listOf(
    108.dp to 150.dp,
    96.dp to 120.dp,
    112.dp to 164.dp,
    90.dp to 104.dp,
)

@Composable
private fun SkeletonBar(width: Dp, height: Dp) {
    Box(
        modifier = Modifier
            .width(width)
            .height(height)
            .clip(LocalAlarmMeShapes.current.small)
            .background(LocalAlarmMeColors.current.surfaceContainer)
    )
}

/**
 * The single pane is capped rather than split: 560dp normally, 1040dp once there is room for the
 * two-column card grid. Everything wider than that becomes margin.
 */
@Composable
private fun contentMaxWidth(): Dp =
    if (currentScreenCategory().isExpandedWidthOrWider) 1040.dp else 560.dp

/** Shorter on expanded, where the list ends well above the bottom edge. */
@Composable
private fun bottomFadeHeight(): Dp =
    if (currentScreenCategory().isExpandedWidthOrWider) 80.dp else 110.dp

private fun previewAlarms(): List<AlarmItem> = listOf(
    AlarmItem(AlarmItemId("gym"), true, LocalDateTime(2026, 9, 11, 5, 30), "Mon, Wed, Fri · Gym — leave by six"),
    AlarmItem(AlarmItemId("weekday"), true, LocalDateTime(2026, 9, 11, 6, 40), "Mon–Fri · Weekday mornings, gently"),
    AlarmItem(AlarmItemId("standup"), true, LocalDateTime(2026, 9, 11, 9, 55), "Mon–Fri · Standup in five"),
    AlarmItem(AlarmItemId("meds"), true, LocalDateTime(2026, 9, 11, 12, 0), "Every day"),
    AlarmItem(AlarmItemId("bread"), false, LocalDateTime(2026, 9, 11, 13, 5), "Thu 10 Sep · Take the bread out"),
    AlarmItem(AlarmItemId("laundry"), false, LocalDateTime(2026, 9, 11, 15, 20), "Sun · Move the laundry over before it sits too long"),
    AlarmItem(AlarmItemId("bins"), true, LocalDateTime(2026, 9, 11, 19, 10), "Wed · Bins out"),
    AlarmItem(AlarmItemId("winddown"), false, LocalDateTime(2026, 9, 11, 21, 45), "Every day · Wind down, screens off"),
    AlarmItem(AlarmItemId("kettle"), true, LocalDateTime(2026, 9, 11, 22, 30), "Every day · Kettle off"),
)

@Composable
private fun PreviewLoaded(
    alarms: List<AlarmItem> = previewAlarms(),
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
            modifier = Modifier.fillMaxSize(),
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
            selectedAlarmItems = listOf(AlarmItemId("gym"), AlarmItemId("bread"), AlarmItemId("bins"))
        )
    )
}

/** Nothing scheduled yet — the pane should still hold its bar and FAB. */
@CompactWidthMediumHeightPreview
@Composable
private fun LoadedAlarmListScreenContentEmptyPreview() {
    PreviewLoaded(alarms = emptyList())
}

@CompactWidthMediumHeightPreview
@Composable
private fun LoadingAlarmListScreenContentPreview() {
    AlarmMeThemeProvider {
        LoadingAlarmListScreenContent(modifier = Modifier.fillMaxSize())
    }
}

@CompactWidthMediumHeightPreview
@Composable
private fun ErrorAlarmListScreenContentPreview() {
    AlarmMeThemeProvider {
        ErrorAlarmListScreenContent(onRetry = {}, modifier = Modifier.fillMaxSize())
    }
}
