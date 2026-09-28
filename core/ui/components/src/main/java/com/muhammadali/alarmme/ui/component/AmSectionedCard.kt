package com.muhammadali.alarmme.ui.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.muhammadali.alarmme.ui.theme.AlarmMeThemeProvider
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeColors
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeShapes
import com.muhammadali.alarmme.utility.compose.modifyIf
import kotlin.collections.emptyList

@Composable
fun AmSectionedCard(
    modifier: Modifier = Modifier,
    isDangerSection: Boolean = false,
    content:  AmSectionedCardScope.() -> Unit
) {
    val colors = LocalAlarmMeColors.current
    val borderColor = if (isDangerSection) colors.errorContainer else colors.outline.copy(0.5f)
    val separatorLineColor = colors.outline.copy(0.5f)

    AmSectionCard(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, borderColor, LocalAlarmMeShapes.current.large),
        shape = LocalAlarmMeShapes.current.large
    ) {
        val sections = AmSectionedCardScopeImpl().apply(content).entries
        if (sections.isEmpty()) return@AmSectionCard


        Column(modifier = Modifier.fillMaxWidth()) {
            sections.forEachIndexed { index, (key, section) ->
                key(key) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .modifyIf(index > 0) {
                                drawBehind {
                                    drawLine(
                                        color = separatorLineColor,
                                        start = Offset(0f, 0f),
                                        end = Offset(size.width, 0f),
                                        strokeWidth = 1f
                                    )
                                }
                            }
                    ) {
                        section()
                    }
                }
            }
        }
    }
}

interface AmSectionedCardScope {
    fun sections(
        count: Int,
        key: ((index: Int) -> Any)? = null,
        section: @Composable (index: Int) -> Unit
    )
}

inline fun <T> AmSectionedCardScope.sections(
    sections: List<T>,
    noinline key: ((item: T) -> Any)? = null,
    crossinline section: @Composable (item: T) -> Unit
) {
    sections(
        count = sections.size,
        key = if (key != null) { index: Int -> key(sections[index]) } else null,
        section = { section(sections[it]) }
    )
}

private data class AmSectionedCardScopeEntry(
    val key: Any,
    val section: @Composable () -> Unit
)

// Using clear && flush to manage the update the state,
// avoiding recomposition until the whole new list is reflected in the state.
private class AmSectionedCardScopeImpl(
    sections: List<AmSectionedCardScopeEntry> = emptyList()
) : AmSectionedCardScope {
    private val sections: MutableList<AmSectionedCardScopeEntry> = sections.toMutableStateList()
    private var callId = 0
    val entries: List<AmSectionedCardScopeEntry>
        get() = sections.toList()

    override fun sections(
        count: Int,
        key: ((index: Int) -> Any)?,
        section: @Composable ((index: Int) -> Unit)
    ) {
        callId++
        repeat(count) { index ->
            sections.add(
                AmSectionedCardScopeEntry(
                    key = key?.invoke(index) ?: callId,
                    section = { section(index) }
                )
            )
        }
    }
}


@Preview(name = "Light Mode", showBackground = true)
@Preview(
    name = "Dark Mode",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AmSectionedCardPreview() {
    AlarmMeThemeProvider {
        var gradualVolumeEnabled by remember { mutableStateOf(true) }
        var vibrateEnabled by remember { mutableStateOf(false) }

        AmSectionedCard(modifier = Modifier.padding(16.dp)) {
            sections(count = 5) { index ->
                when (index) {
                    0 -> AmNavigationDisclosureRow(
                        text = "Sound",
                        value = "Sunrise chime",
                        valueColor = LocalAlarmMeColors.current.primary,
                        onClick = {}
                    )

                    1 -> AmNavigationDisclosureRow(
                        text = "Snooze",
                        value = "9 min · 3 times",
                        onClick = {}
                    )

                    2 -> AmToggleDisclosureRow(
                        text = "Gradual volume",
                        checked = gradualVolumeEnabled,
                        onCheckedChange = { gradualVolumeEnabled = it }
                    )

                    3 -> AmToggleDisclosureRow(
                        text = "Vibrate",
                        checked = vibrateEnabled,
                        onCheckedChange = { vibrateEnabled = it }
                    )

                    else -> AmNavigationDisclosureRow(
                        text = "Label",
                        value = "Weekday mornings",
                        onClick = {}
                    )
                }
            }
        }
    }
}
