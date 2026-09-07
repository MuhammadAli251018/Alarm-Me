package com.muhammadali.alarmme.ui.component

import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.invalidateDraw
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.muhammadali.alarmme.ui.theme.AlarmMeThemeProvider
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeColors
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.launch

class TextButtonIndication(
    private val pressColor: Color
) : IndicationNodeFactory {

    override fun create(interactionSource: InteractionSource): DelegatableNode =
        FilledButtonIndicationNode(interactionSource, pressColor)

    override fun hashCode(): Int = pressColor.hashCode()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TextButtonIndication) return false
        return pressColor == other.pressColor
    }
}

@Composable
fun rememberTextButtonIndication(pressColor: Color): IndicationNodeFactory =
    remember(pressColor) { TextButtonIndication(pressColor) }

private class FilledButtonIndicationNode(
    private val interactionSource: InteractionSource,
    private val pressColor: Color
) : Modifier.Node(), DrawModifierNode {

    private var isPressed = false

    override fun onAttach() {
        coroutineScope.launch {
            interactionSource.interactions.fold(false) { wasPressed, interaction ->
                val pressed = interaction is PressInteraction.Press

                if(wasPressed != pressed) {
                    isPressed = pressed
                    invalidateDraw()
                }
                pressed
            }
        }
    }

    override fun ContentDrawScope.draw() {
        drawContent()
        if (isPressed) drawRect(color = pressColor)
    }
}

@Preview(name = "Light Mode")
@Preview(
    name = "Dark Mode",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun FilledButtonIndicationPreview() {
    val pressedSource = remember {
        object : MutableInteractionSource {
            override suspend fun emit(interaction: Interaction) = Unit

            override fun tryEmit(interaction: Interaction): Boolean = false


            override val interactions: Flow<Interaction> =
                flowOf(PressInteraction.Press(Offset(5f, 5f)))
        }
    }

    AlarmMeThemeProvider {
        Column(
            modifier = Modifier
                .background(LocalAlarmMeColors.current.background)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AmFilledTextButton(
                text = "Pressed",
                interactionSource = pressedSource,
                onClick = {}
            )

            AmFilledTextButton(
                text = "Idle",
                onClick = {}
            )
        }
    }
}
