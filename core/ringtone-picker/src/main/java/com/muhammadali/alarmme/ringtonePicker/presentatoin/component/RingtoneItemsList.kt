package com.muhammadali.alarmme.ringtonePicker.presentatoin.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.muhammadali.alarmme.ui.screenCategory.CommonScreenCategoriesPreview
import com.muhammadali.alarmme.ui.screenCategory.CompactWidthMediumHeightPreview
import com.muhammadali.alarmme.ui.screenCategory.MediumWidthMediumHeightPreview
import com.muhammadali.alarmme.ui.screenCategory.currentScreenCategory
import com.muhammadali.alarmme.ui.screenCategory.isExpandedWidthOrWider
import com.muhammadali.alarmme.ui.theme.AlarmMeThemeProvider
import kotlin.time.Duration.Companion.seconds

@Composable
fun RingtoneItemsList(
    modifier: Modifier = Modifier,
    ringtones: List<RingtoneItem>,
    selectedRingtoneId: String?,
    playingRingtoneId: String?,
    onRingtoneClick: (String) -> Unit,
    onRingtoneSelect: (String) -> Unit,
) {
    val screenCategory = currentScreenCategory()

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(
            if (screenCategory.isExpandedWidthOrWider) 10.dp else 8.dp
        ),
        contentPadding = PaddingValues(vertical = 4.dp)
    ) {
        items(ringtones, key = { it.id }) { ringtone ->
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

internal fun previewRingtones(): List<RingtoneItem> = listOf(
    RingtoneItem("sunrise-chime", "Sunrise chime", "Gentle", 42.seconds),
    RingtoneItem("slow-bell", "Slow bell", "Gentle", 64.seconds),
    RingtoneItem("morning-birds", "Morning birds", "Natural", 130.seconds),
    RingtoneItem("ocean-drift", "Ocean drift", "Ambient", 90.seconds),
    RingtoneItem("woodland-whistle", "Woodland whistle", "Natural", 55.seconds),
)

/** The list across screen categories: [CommonScreenCategoriesPreview]'s three buckets cover the
 * phone-shaped 8dp gap (compact/medium buckets) and the expanded 10dp gap. */
@CommonScreenCategoriesPreview
@Composable
private fun RingtoneItemsListPreview() {
    AlarmMeThemeProvider {
        RingtoneItemsList(
            ringtones = previewRingtones(),
            selectedRingtoneId = "sunrise-chime",
            playingRingtoneId = "morning-birds",
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
            selectedRingtoneId = "sunrise-chime",
            playingRingtoneId = null,
            onRingtoneClick = {},
            onRingtoneSelect = {}
        )
    }
}
