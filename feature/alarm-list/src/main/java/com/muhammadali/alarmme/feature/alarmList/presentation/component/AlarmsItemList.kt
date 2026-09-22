package com.muhammadali.alarmme.feature.alarmList.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.muhammadali.alarmme.feature.alarmList.domain.AlarmItemId
import com.muhammadali.alarmme.feature.alarmList.presentation.alarmsListHorizontalPaddings
import com.muhammadali.alarmme.ui.screenCategory.CommonScreenCategoriesPreview
import com.muhammadali.alarmme.ui.screenCategory.CompactWidthMediumHeightPreview
import com.muhammadali.alarmme.ui.screenCategory.LargeWidthExpandedHeightPreview
import com.muhammadali.alarmme.ui.screenCategory.MediumWidthMediumHeightPreview
import com.muhammadali.alarmme.ui.screenCategory.WidthCategory
import com.muhammadali.alarmme.ui.screenCategory.currentScreenCategory
import com.muhammadali.alarmme.ui.screenCategory.isExpandedWidthOrWider
import com.muhammadali.alarmme.ui.screenCategory.isPhone
import com.muhammadali.alarmme.ui.theme.AlarmMeThemeProvider
import kotlinx.datetime.LocalDateTime

/** Shared with [AlarmsListSkeleton] so the placeholder grid lands on the same lines as the real one. */
internal val AlarmsListVerticalPadding: Dp = 12.dp
internal val AlarmsGridHorizontalSpacing: Dp = 16.dp
internal val AlarmsGridVerticalSpacing: Dp = 12.dp

class AlarmItemsListState(selectedAlarmItems: List<AlarmItemId>) {
    private val _selectedAlarmItems: MutableState<Set<AlarmItemId>> = mutableStateOf(selectedAlarmItems.toSet())

    val selectedAlarmItems: Set<AlarmItemId>
        get() = _selectedAlarmItems.value

    val isSelectionMode: Boolean
        get() = _selectedAlarmItems.value.isNotEmpty()

    fun add(alarmItemId: AlarmItemId) {
        _selectedAlarmItems.value += alarmItemId
    }

    fun remove(alarmItemId: AlarmItemId) {
        _selectedAlarmItems.value -= alarmItemId
    }

    fun addAll(alarmItemIds: List<AlarmItemId>) {
        _selectedAlarmItems.value += alarmItemIds
    }

    fun clear() {
        _selectedAlarmItems.value = emptySet()
    }

    fun addOrRemove(alarmItemId: AlarmItemId) {
        if (isSelected(alarmItemId)) remove(alarmItemId) else add(alarmItemId)
    }

    fun isSelected(alarmItemId: AlarmItemId): Boolean {
        return _selectedAlarmItems.value.contains(alarmItemId)
    }

    companion object {
        /**
         * Survives configuration changes. This list reconfigures on rotation, so losing the
         * selection on a device turn would be a routine occurrence rather than an edge case.
         */
        val Saver: Saver<AlarmItemsListState, Any> = listSaver(
            save = { state -> state.selectedAlarmItems.map { it.value } },
            restore = { ids -> AlarmItemsListState(ids.map(::AlarmItemId)) }
        )
    }
}

operator fun AlarmItemsListState.plusAssign(alarmItemId: AlarmItemId) {
    add(alarmItemId)
}

operator fun AlarmItemsListState.minusAssign(alarmItemId: AlarmItemId) {
    remove(alarmItemId)
}

@Composable
fun rememberAlarmItemsListState(
    selectedAlarmItems: List<AlarmItemId> = emptyList()
): AlarmItemsListState {
    return rememberSaveable(selectedAlarmItems, saver = AlarmItemsListState.Saver) {
        AlarmItemsListState(selectedAlarmItems)
    }
}


@Composable
fun AlarmItemsList(
    alarmItems: List<AlarmItem>,
    onAlarmClick: (AlarmItemId) -> Unit,
    onAlarmCheckedChange: (AlarmItemId, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    alarmsListState: AlarmItemsListState = rememberAlarmItemsListState(),
) {
    val horizontalPaddings = alarmsListHorizontalPaddings()
    val columns = alarmsListColumnsCount()

    LazyVerticalGrid(
        modifier = modifier,
        contentPadding = PaddingValues(
            horizontal = horizontalPaddings,
            vertical = AlarmsListVerticalPadding
        ),
        columns = GridCells.Fixed(columns),
        horizontalArrangement = Arrangement.spacedBy(AlarmsGridHorizontalSpacing),
        verticalArrangement = Arrangement.spacedBy(AlarmsGridVerticalSpacing)
    ) {
        items(
            count = alarmItems.size,
            key = { index -> alarmItems[index].id.value }
        ) { index ->
            val alarmItem = alarmItems[index]

            AlarmItem(
                modifier = Modifier.fillMaxWidth(),
                alarmItem = alarmItem,
                onLongClick = { alarmsListState.addOrRemove(alarmItem.id) },
                selected = alarmsListState.isSelected(alarmItem.id),
                selectionMode = alarmsListState.isSelectionMode,
                onCheckedChange = { checked -> onAlarmCheckedChange(alarmItem.id, checked) },
                onSelectedChange = { alarmsListState.addOrRemove(alarmItem.id) },
                onClick = {
                    if (alarmsListState.isSelectionMode)
                        alarmsListState.addOrRemove(alarmItem.id)
                    else
                        onAlarmClick(alarmItem.id)
                }
            )
        }

        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}

@Composable
internal fun alarmsListColumnsCount(): Int {
    val screenCategory = currentScreenCategory()
    return if (screenCategory.isExpandedWidthOrWider) 2 else 1
}
/**
 * A spread of alarms covering both enabled states and a range of description lengths, so previews
 * exercise the OFF badge, the disabled styling, and description truncation in the narrow buckets.
 */
private fun previewAlarmItems(): List<AlarmItem> = listOf(
    AlarmItem(
        id = AlarmItemId("gym"),
        enabled = true,
        time = LocalDateTime(2026, 9, 11, 5, 30),
        description = "Mon, Wed, Fri · Gym — leave by six"
    ),
    AlarmItem(
        id = AlarmItemId("weekday"),
        enabled = true,
        time = LocalDateTime(2026, 9, 11, 6, 40),
        description = "Mon–Fri · Weekday mornings, gently"
    ),
    AlarmItem(
        id = AlarmItemId("weekend"),
        enabled = true,
        time = LocalDateTime(2026, 9, 11, 7, 15),
        description = "Sat, Sun"
    ),
    AlarmItem(
        id = AlarmItemId("bread"),
        enabled = false,
        time = LocalDateTime(2026, 9, 11, 13, 5),
        description = "Thu 10 Sep · Take the bread out"
    ),
    AlarmItem(
        id = AlarmItemId("winddown"),
        enabled = false,
        time = LocalDateTime(2026, 9, 11, 21, 45),
        description = "Every day · Wind down, screens off, curtains drawn"
    ),
    AlarmItem(
        id = AlarmItemId("kettle"),
        enabled = true,
        time = LocalDateTime(2026, 9, 11, 22, 30),
        description = "Every day · Kettle off"
    ),
    AlarmItem(
        id = AlarmItemId("dawn"),
        enabled = true,
        time = LocalDateTime(2026, 9, 11, 4, 45),
        description = "Sat · Airport run"
    ),
    AlarmItem(
        id = AlarmItemId("standup"),
        enabled = true,
        time = LocalDateTime(2026, 9, 11, 9, 55),
        description = "Mon–Fri · Standup in five"
    ),
    AlarmItem(
        id = AlarmItemId("meds"),
        enabled = true,
        time = LocalDateTime(2026, 9, 11, 12, 0),
        description = "Every day"
    ),
    AlarmItem(
        id = AlarmItemId("laundry"),
        enabled = false,
        time = LocalDateTime(2026, 9, 11, 15, 20),
        description = "Sun · Move the laundry over before it sits too long"
    ),
    AlarmItem(
        id = AlarmItemId("call"),
        enabled = false,
        time = LocalDateTime(2026, 9, 11, 18, 30),
        description = "Tue · Call home"
    ),
    AlarmItem(
        id = AlarmItemId("bins"),
        enabled = true,
        time = LocalDateTime(2026, 9, 11, 19, 10),
        description = "Wed · Bins out"
    ),
    AlarmItem(
        id = AlarmItemId("stretch"),
        enabled = false,
        time = LocalDateTime(2026, 9, 11, 20, 5),
        description = "Mon, Thu · Stretch — ten minutes, no excuses"
    ),
    AlarmItem(
        id = AlarmItemId("lights"),
        enabled = true,
        time = LocalDateTime(2026, 9, 11, 23, 15),
        description = "Every day · Lights out"
    ),
)

/**
 * The grid across screen categories. [CommonScreenCategoriesPreview]'s three buckets cover three of
 * the four padding/column outcomes: compact width + medium height and medium width + compact height
 * are both phones (20dp gutters, one column), while expanded width switches to 48dp and two columns.
 */
@CommonScreenCategoriesPreview
@Composable
private fun AlarmItemsListPreview() {
    AlarmMeThemeProvider {
        AlarmItemsList(
            alarmItems = previewAlarmItems(),
            onAlarmClick = {},
            onAlarmCheckedChange = { _, _ -> }
        )
    }
}

/**
 * Medium width with non-compact height is the only bucket that is neither a phone nor expanded, so
 * it is the only way to see the 32dp gutter branch.
 */
@MediumWidthMediumHeightPreview
@Composable
private fun AlarmItemsListMediumWidthPreview() {
    AlarmMeThemeProvider {
        AlarmItemsList(
            alarmItems = previewAlarmItems(),
            onAlarmClick = {},
            onAlarmCheckedChange = { _, _ -> }
        )
    }
}

/** Selection chrome does not vary by screen category, so one bucket is enough. */
@CompactWidthMediumHeightPreview
@Composable
private fun AlarmItemsListSelectionModePreview() {
    AlarmMeThemeProvider {
        AlarmItemsList(
            alarmsListState = rememberAlarmItemsListState(
                selectedAlarmItems = listOf(AlarmItemId("gym"), AlarmItemId("bread"))
            ),
            alarmItems = previewAlarmItems(),
            onAlarmClick = {},
            onAlarmCheckedChange = { _, _ -> }
        )
    }
}

/** Nothing scheduled - the grid should collapse to empty space rather than misbehave. */
@CompactWidthMediumHeightPreview
@Composable
private fun AlarmItemsListEmptyPreview() {
    AlarmMeThemeProvider {
        AlarmItemsList(
            alarmItems = emptyList(),
            onAlarmClick = {},
            onAlarmCheckedChange = { _, _ -> }
        )
    }
}

/** Large width still gets the same fixed two columns, which is where the layout looks sparsest. */
@LargeWidthExpandedHeightPreview
@Composable
private fun AlarmItemsListLargeWidthPreview() {
    AlarmMeThemeProvider {
        AlarmItemsList(
            alarmItems = previewAlarmItems(),
            onAlarmClick = {},
            onAlarmCheckedChange = { _, _ -> }
        )
    }
}
