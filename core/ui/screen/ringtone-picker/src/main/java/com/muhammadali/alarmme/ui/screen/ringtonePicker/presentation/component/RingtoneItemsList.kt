package com.muhammadali.alarmme.ui.screen.ringtonePicker.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.muhammadali.alarmme.alarms.domain.RingtoneId
import com.muhammadali.alarmme.ui.screen.ringtonePicker.presentation.model.RingtoneItemState
import com.muhammadali.alarmme.ui.screenCategory.CommonScreenCategoriesPreview
import com.muhammadali.alarmme.ui.screenCategory.MediumWidthMediumHeightPreview
import com.muhammadali.alarmme.ui.screenCategory.currentScreenCategory
import com.muhammadali.alarmme.ui.screenCategory.isExpandedWidthOrWider
import com.muhammadali.alarmme.ui.theme.AlarmMeThemeProvider
import kotlin.time.Duration.Companion.seconds

@Composable
fun RingtoneItemsList(
    modifier: Modifier = Modifier,
    ringtones: List<RingtoneItemState>,
    selectedRingtoneId: RingtoneId?,
    playingRingtoneId: RingtoneId?,
    onRingtoneClick: (RingtoneId) -> Unit,
    onRingtoneSelect: (RingtoneId) -> Unit,
) {
    val screenCategory = currentScreenCategory()

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(
            if (screenCategory.isExpandedWidthOrWider) 10.dp else 8.dp
        ),
        contentPadding = PaddingValues(vertical = 4.dp)
    ) {
        items(ringtones, key = { it.id.value }) { ringtone ->
            RingtoneItem(
                modifier = Modifier.fillMaxWidth(),
                ringtone = ringtone,
                selected = ringtone.id == selectedRingtoneId,
                playing = ringtone.id == playingRingtoneId,
                onClick = { onRingtoneClick(ringtone.id) },
                onSelect = { onRingtoneSelect(ringtone.id) }
            )
        }
    }
}

internal fun previewRingtones(): List<RingtoneItemState> = listOf(
    RingtoneItemState(RingtoneId("sunrise-chime"), "Sunrise chime", 42.seconds),
    RingtoneItemState(RingtoneId("slow-bell"), "Slow bell", 64.seconds),
    RingtoneItemState(RingtoneId("morning-birds"), "Morning birds", 130.seconds),
    RingtoneItemState(RingtoneId("ocean-drift"), "Ocean drift", 90.seconds),
    RingtoneItemState(RingtoneId("woodland-whistle"), "Woodland whistle", null),
)

/** The list across screen categories: [CommonScreenCategoriesPreview]'s three buckets cover the
 * phone-shaped 8dp gap (compact/medium buckets) and the expanded 10dp gap. */
@CommonScreenCategoriesPreview
@Composable
private fun RingtoneItemsListPreview() {
    AlarmMeThemeProvider {
        RingtoneItemsList(
            ringtones = previewRingtones(),
            selectedRingtoneId = RingtoneId("sunrise-chime"),
            playingRingtoneId = RingtoneId("morning-birds"),
            onRingtoneClick = {},
            onRingtoneSelect = {}
        )
    }
}

/** Medium width with non-compact height is neither phone-shaped nor expanded, so it is the only
 * bucket that renders the phone-shaped gap inside the wider, centered frame. */
@MediumWidthMediumHeightPreview
@Composable
private fun RingtoneItemsListMediumWidthPreview() {
    AlarmMeThemeProvider {
        RingtoneItemsList(
            ringtones = previewRingtones(),
            selectedRingtoneId = RingtoneId("sunrise-chime"),
            playingRingtoneId = null,
            onRingtoneClick = {},
            onRingtoneSelect = {}
        )
    }
}

/** Nothing picked yet — the state the picker now opens in, with Save disabled. */
@CommonScreenCategoriesPreview
@Composable
private fun RingtoneItemsListNoSelectionPreview() {
    AlarmMeThemeProvider {
        RingtoneItemsList(
            ringtones = previewRingtones(),
            selectedRingtoneId = null,
            playingRingtoneId = null,
            onRingtoneClick = {},
            onRingtoneSelect = {}
        )
    }
}
