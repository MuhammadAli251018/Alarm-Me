package dev.alidroid.alarmme.ui.component

import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * An interaction source stuck in a single state, so a `@Preview` can render pressed and focused
 * appearances that otherwise only exist mid-gesture. Preview scaffolding — not for production use.
 */
private class StuckInteractionSource(
    interaction: Interaction
) : MutableInteractionSource {
    override val interactions: Flow<Interaction> = flowOf(interaction)

    override suspend fun emit(interaction: Interaction) = Unit

    override fun tryEmit(interaction: Interaction): Boolean = false
}

@Composable
internal fun rememberPressedInteractionSource(): MutableInteractionSource =
    remember { StuckInteractionSource(PressInteraction.Press(Offset.Zero)) }

@Composable
fun rememberFocusedInteractionSource(): MutableInteractionSource =
    remember { StuckInteractionSource(FocusInteraction.Focus()) }
