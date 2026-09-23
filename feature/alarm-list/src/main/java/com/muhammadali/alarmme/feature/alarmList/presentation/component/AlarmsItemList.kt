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
import com.muhammadali.alarmme.alarms.domain.AlarmId
import com.muhammadali.alarmme.feature.alarmList.presentation.alarmsListHorizontalPaddings
import com.muhammadali.alarmme.feature.alarmList.presentation.model.AlarmItemState
import com.muhammadali.alarmme.ui.screenCategory.CommonScreenCategoriesPreview
import com.muhammadali.alarmme.ui.screenCategory.CompactWidthMediumHeightPreview
import com.muhammadali.alarmme.ui.screenCategory.LargeWidthExpandedHeightPreview
import com.muhammadali.alarmme.ui.screenCategory.MediumWidthMediumHeightPreview
import com.muhammadali.alarmme.ui.screenCategory.currentScreenCategory
import com.muhammadali.alarmme.ui.screenCategory.isExpandedWidthOrWider
import com.muhammadali.alarmme.ui.theme.AlarmMeThemeProvider
import kotlinx.datetime.LocalTime

/** Shared with [AlarmsListSkeleton] so the placeholder grid lands on the same lines as the real one. */
internal val AlarmsListVerticalPadding: Dp = 12.dp
internal val AlarmsGridHorizontalSpacing: Dp = 16.dp
internal val AlarmsGridVerticalSpacing: Dp = 12.dp

class AlarmItemsListState(selectedAlarmItems: List<AlarmId>) {
    private val _selectedAlarmItems: MutableState<Set<AlarmId>> = mutableStateOf(selectedAlarmItems.toSet())

    val selectedAlarmItems: Set<AlarmId>
        get() = _selectedAlarmItems.value

    val isSelectionMode: Boolean
        get() = _selectedAlarmItems.value.isNotEmpty()

    fun add(alarmItemId: AlarmId) {
        _selectedAlarmItems.value += alarmItemId
    }

    fun remove(alarmItemId: AlarmId) {
        _selectedAlarmItems.value -= alarmItemId
    }

    fun addAll(alarmItemIds: List<AlarmId>) {
        _selectedAlarmItems.value += alarmItemIds
    }

    fun clear() {
        _selectedAlarmItems.value = emptySet()
    }

    fun addOrRemove(alarmItemId: AlarmId) {
        if (isSelected(alarmItemId)) remove(alarmItemId) else add(alarmItemId)
    }

    fun isSelected(alarmItemId: AlarmId): Boolean {
        return _selectedAlarmItems.value.contains(alarmItemId)
    }

    companion object {
        /**
         * Survives configuration changes. This list reconfigures on rotation, so losing the
         * selection on a device turn would be a routine occurrence rather than an edge case.
         */
        val Saver: Saver<AlarmItemsListState, Any> = listSaver(
            save = { state -> state.selectedAlarmItems.map { it.value } },
            restore = { ids -> AlarmItemsListState(ids.map(::AlarmId)) }
        )
    }
}

operator fun AlarmItemsListState.plusAssign(alarmItemId: AlarmId) {
    add(alarmItemId)
}

operator fun AlarmItemsListState.minusAssign(alarmItemId: AlarmId) {
    remove(alarmItemId)
}

@Composable
fun rememberAlarmItemsListState(
    selectedAlarmItems: List<AlarmId> = emptyList()
): AlarmItemsListState {
    return rememberSaveable(selectedAlarmItems, saver = AlarmItemsListState.Saver) {
        AlarmItemsListState(selectedAlarmItems)
    }
}


@Composable
fun AlarmItemsList(
    alarmItems: List<AlarmItemState>,
    onAlarmClick: (AlarmId) -> Unit,
    onAlarmCheckedChange: (AlarmId, Boolean) -> Unit,
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
private fun previewAlarmItems(): List<AlarmItemState> = listOf(
    AlarmItemState(
        id = AlarmId("gym"),
        enabled = true,
        time = LocalTime(5, 30),
        description = "Mon, Wed, Fri · Gym — leave by six"
    ),
    AlarmItemState(
        id = AlarmId("weekday"),
        enabled = true,
        time = LocalTime(6, 40),
        description = "Mon–Fri · Weekday mornings, gently"
    ),
    AlarmItemState(
        id = AlarmId("weekend"),
        enabled = true,
        time = LocalTime(7, 15),
        description = "Sat, Sun"
    ),
    AlarmItemState(
        id = AlarmId("bread"),
        enabled = false,
        time = LocalTime(13, 5),
        description = "Thu 10 Sep · Take the bread out"
    ),
    AlarmItemState(
        id = AlarmId("winddown"),
        enabled = false,
        time = LocalTime(21, 45),
        description = "Every day · Wind down, screens off, curtains drawn"
    ),
    AlarmItemState(
        id = AlarmId("kettle"),
        enabled = true,
        time = LocalTime(22, 30),
        description = "Every day · Kettle off"
    ),
    AlarmItemState(
        id = AlarmId("dawn"),
        enabled = true,
        time = LocalTime(4, 45),
        description = "Sat · Airport run"
    ),
    AlarmItemState(
        id = AlarmId("standup"),
        enabled = true,
        time = LocalTime(9, 55),
        description = "Mon–Fri · Standup in five"
    ),
    AlarmItemState(
        id = AlarmId("meds"),
        enabled = true,
        time = LocalTime(12, 0),
        description = "Every day"
    ),
    AlarmItemState(
        id = AlarmId("laundry"),
        enabled = false,
        time = LocalTime(15, 20),
        description = "Sun · Move the laundry over before it sits too long"
    ),
    AlarmItemState(
        id = AlarmId("call"),
        enabled = false,
        time = LocalTime(18, 30),
        description = "Tue · Call home"
    ),
    AlarmItemState(
        id = AlarmId("bins"),
        enabled = true,
        time = LocalTime(19, 10),
        description = "Wed · Bins out"
    ),
    AlarmItemState(
        id = AlarmId("stretch"),
        enabled = false,
        time = LocalTime(20, 5),
        description = "Mon, Thu · Stretch — ten minutes, no excuses"
    ),
    AlarmItemState(
        id = AlarmId("lights"),
        enabled = true,
        time = LocalTime(23, 15),
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
                selectedAlarmItems = listOf(AlarmId("gym"), AlarmId("bread"))
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
