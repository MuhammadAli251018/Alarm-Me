package com.muhammadali.alarmme.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldDecorator
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.selectAll
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.muhammadali.alarmme.ui.screenCategory.CommonScreenCategoriesPreview
import com.muhammadali.alarmme.ui.theme.AlarmMeThemeProvider
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeColors
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeShapes
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeTypography
import kotlinx.coroutines.flow.drop

private const val DefaultDigitCount = 2
private const val PlaceholderChar = '–'
private const val PlaceholderAlpha = 0.3f
private val DigitBoxHorizontalPadding = 20.dp
private val DigitBoxVerticalPadding = 14.dp
private val DigitFieldWidth = 88.dp
private val LabelGap = 6.dp
private val LabelFontSize = 9.sp
private val UnderlineThickness = 2.dp
private val UnderlineGap = 2.dp
private val InvisibleTextSelectionColors = TextSelectionColors(Color.Transparent, Color.Transparent)

// TODO: Need to be unit tested.
@Stable
internal class DigitFieldState(
    initialValue: Int,
    private val minValue: Int,
    private val maxValue: Int,
    val digitCount: Int = DefaultDigitCount,
) {
    init {
        require(minValue <= maxValue) { "Min value must be less than or equal to max value" }
        require(minValue >= 0) { "Min value must be non-negative" }
        require(maxValue.toString().length <= digitCount) {
            "Max value $maxValue does not fit in $digitCount digits"
        }
        require(initialValue in minValue..maxValue) { "Initial value must be in range" }
    }

    val textFieldState = TextFieldState(initialText = initialValue.padded())

    /** The current value, or `null` while the text is a partial or empty placeholder */
    val value: Int?
        get() = textFieldState.text.toString().toIntOrNull()?.takeIf { it in minValue..maxValue }

    /** Whether the user has typed enough digits that there is nothing left to enter here. */
    val isComplete: Boolean
        get() = textFieldState.text.length == digitCount

    /**
     * The cells the next keystroke will write to: the selected cells when there is a selection,
     * otherwise the single cell the caret sits in front of. Always a valid range of rendered
     * cells, so the underline has geometry even when no digit has been typed yet.
     */
    val affectedCells: IntRange
        get() {
            val selection = textFieldState.selection
            val lastCell = digitCount - 1

            return if (selection.collapsed) {
                val cell = selection.start.coerceIn(0, lastCell)
                cell..cell
            } else {
                selection.min.coerceIn(0, lastCell)..(selection.max - 1).coerceIn(0, lastCell)
            }
        }

    /** Rejects any edit that no amount of further typing could turn into a value in range. */
    val inputTransformation = InputTransformation {
        if (!isViablePrefix(asCharSequence())) revertAllChanges()
    }

    /** Selects the whole text so the next keystroke replaces it rather than appending to it. */
    fun selectAll() {
        textFieldState.edit {
            this@edit.selectAll()
        }
    }

    /** Pads a partially typed value back out, falling back to [minValue] when nothing is typed. */
    fun formatToPadded() {
        setText((value ?: minValue).padded())
    }

    /** Applies a value supplied from outside, e.g. when the hoisted state changes elsewhere. */
    fun setValue(value: Int) {
        setText(value.coerceIn(minValue, maxValue).padded())
    }

    private fun setText(text: String) {
        if (textFieldState.text.contentEquals(text)) return
        textFieldState.setTextAndPlaceCursorAtEnd(text)
    }

    /**
     * Whether [text] is a prefix of *some* value in range. The user may stop typing at any width,
     * so every remaining-digit count is a candidate: `"9"` is viable as an hour because it can
     * stand alone as 9, even though 90..99 is out of range, while `"13"` is viable at no width at
     * all. `"0"` is viable only because it can still grow into `"01"`.
     */
    private fun isViablePrefix(text: CharSequence): Boolean {
        if (text.isEmpty()) return true
        if (text.length > digitCount) return false
        if (!text.all { it.isDigit() }) return false

        var lowest = text.toString().toLongOrNull() ?: return false
        var span = 1L
        repeat(digitCount - text.length + 1) {
            if (lowest <= maxValue && minValue <= lowest + span - 1) return true
            lowest *= 10
            span *= 10
        }
        return false
    }

    private fun Int.padded(): String = toString().padStart(digitCount, '0')

    companion object {
        /**
         * Survives configuration changes, keeping the raw in-progress text rather than the parsed
         * value — a rotation mid-edit would otherwise silently pad `"1"` out to `"01"`.
         */
        fun saver(minValue: Int, maxValue: Int, digitCount: Int): Saver<DigitFieldState, Any> =
            listSaver(
                save = { state -> listOf(state.textFieldState.text.toString()) },
                restore = { saved ->
                    val text = saved.first()
                    val restored = DigitFieldState(
                        initialValue = text.toIntOrNull()?.coerceIn(minValue, maxValue) ?: minValue,
                        minValue = minValue,
                        maxValue = maxValue,
                        digitCount = digitCount,
                    )
                    restored.textFieldState.setTextAndPlaceCursorAtEnd(text)
                    restored
                }
            )
    }
}

/**
 * [value] is deliberately not a key: an external change is applied through
 * [DigitFieldState.setValue] so that recreating the state never discards in-progress typing.
 */
@Composable
internal fun rememberDigitFieldState(
    value: Int,
    minValue: Int,
    maxValue: Int,
    digitCount: Int = DefaultDigitCount,
): DigitFieldState {
    return rememberSaveable(
        minValue,
        maxValue,
        digitCount,
        saver = DigitFieldState.saver(minValue, maxValue, digitCount)
    ) {
        DigitFieldState(value, minValue, maxValue, digitCount)
    }
}

@Composable
internal fun DigitField(
    state: DigitFieldState,
    value: Int,
    label: String,
    enabled: Boolean,
    imeAction: ImeAction,
    focusRequester: FocusRequester,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onDigitsComplete: () -> Unit = {},
    onDeleteAtStart: () -> Unit = {},
) {
    val colors = LocalAlarmMeColors.current
    val typography = LocalAlarmMeTypography.current
    val shapes = LocalAlarmMeShapes.current
    val isFocused by interactionSource.collectIsFocusedAsState()
    val currentOnValueChange by rememberUpdatedState(onValueChange)
    val currentOnDigitsComplete by rememberUpdatedState(onDigitsComplete)

    val placeholderStyle = SpanStyle(color = colors.onSurfaceVariant.copy(alpha = PlaceholderAlpha))
    val placeholderTransformation = remember(state.digitCount, placeholderStyle) {
        OutputTransformation {
            val missing = state.digitCount - length
            if (missing > 0) {
                val placeholderStart = length
                repeat(missing) { append(PlaceholderChar) }
                addStyle(placeholderStyle, placeholderStart, length)
            }
        }
    }

    // Note: Drops the initial text, so the onValueChange is invoked only on a true change.
    LaunchedEffect(state) {
        snapshotFlow { state.textFieldState.text.toString() }
            .drop(1)
            .collect {
                state.value?.let(currentOnValueChange)
                if (state.isComplete && isFocused) currentOnDigitsComplete()
            }
    }

    LaunchedEffect(value) {
        if (!isFocused && value != state.value) state.setValue(value)
    }

    val outlineColor by animateColorAsState(
        targetValue = if (isFocused) colors.primary else colors.outlineVariant,
        label = "outlineColor",
        animationSpec = tween(300)
    )

    val outlineWidth by animateDpAsState(
        targetValue = if (isFocused) 2.dp else 1.dp,
        label = "outlineWidth",
        animationSpec = tween(300)
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(LabelGap)
    ) {
        CompositionLocalProvider(LocalTextSelectionColors provides InvisibleTextSelectionColors) {

            var textLayout: TextLayoutResult? by remember { mutableStateOf(null) }

            BasicTextField(
                state = state.textFieldState,
                modifier = Modifier
                    .width(DigitFieldWidth)
                    .focusRequester(focusRequester)
                    .onFocusChanged {
                        if (it.isFocused) state.selectAll() else state.formatToPadded()
                    }
                    .onKeyEvent { keyEvent ->
                        keyEvent.isBackspaceAtStart(state, onDeleteAtStart)
                    },
                enabled = enabled,
                inputTransformation = state.inputTransformation,
                outputTransformation = placeholderTransformation,
                onTextLayout = { layoutFactory -> textLayout = layoutFactory() },
                textStyle = typography.alarmWheelNeighbor.copy(
                    color = colors.onSurface,
                    textAlign = TextAlign.Center
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = imeAction
                ),
                onKeyboardAction = KeyboardActionHandler { currentOnDigitsComplete() },
                lineLimits = TextFieldLineLimits.SingleLine,
                interactionSource = interactionSource,
                cursorBrush = SolidColor(Color.Transparent),
                decorator = TextFieldDecorator { innerTextField ->
                    Box(
                        modifier = Modifier
                            .clip(shapes.medium)
                            .background(colors.surfaceContainer)
                            .clickable(
                                onClick = { focusRequester.requestFocus() },
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            )
                            .border(
                                width = outlineWidth,
                                color = outlineColor,
                                shapes.medium
                            )
                            .padding(
                                horizontal = DigitBoxHorizontalPadding,
                                vertical = DigitBoxVerticalPadding
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier.drawBehind {
                                if (!isFocused) return@drawBehind
                                val layout = textLayout ?: return@drawBehind

                                // The placeholder guarantees a cell per digit, so these are
                                // always in bounds however little has been typed.
                                val cells = state.affectedCells
                                val startBox = layout.getBoundingBox(cells.first)
                                val endBox = layout.getBoundingBox(cells.last)
                                val y = startBox.bottom + UnderlineGap.toPx()

                                drawLine(
                                    color = colors.primary,
                                    start = Offset(startBox.left, y),
                                    end = Offset(endBox.right, y),
                                    strokeWidth = UnderlineThickness.toPx(),
                                    cap = StrokeCap.Round
                                )
                            }
                        ) {
                            innerTextField()
                        }
                    }
                }
            )
        }

        Text(
            text = label,
            style = typography.labelSmall.copy(fontSize = LabelFontSize),
            color = colors.onSurfaceVariant
        )
    }
}

/**
 * Only `KeyDown` counts — without the filter the matching `KeyUp` fires [block] a second time and
 * the caller skips back two fields on one press.
 */
private fun KeyEvent.isBackspaceAtStart(state: DigitFieldState, block: () -> Unit): Boolean {
    if (type != KeyEventType.KeyDown || key != Key.Backspace) return false

    val selection = state.textFieldState.selection
    val atStart = state.textFieldState.text.isEmpty() ||
            (selection.collapsed && selection.start == 0)

    return atStart.also { if (it) block() }
}

@CommonScreenCategoriesPreview
@Composable
private fun DigitFieldPreview() {
    AlarmMeThemeProvider {
        Box(
            Modifier
                .background(LocalAlarmMeColors.current.background)
                .padding(16.dp)
        ) {
            DigitField(
                state = rememberDigitFieldState(value = 7, minValue = 1, maxValue = 12),
                value = 7,
                label = "HOUR",
                enabled = true,
                imeAction = ImeAction.Next,
                focusRequester = remember { FocusRequester() },
                onValueChange = {}
            )
        }
    }
}

@CommonScreenCategoriesPreview
@Composable
private fun DigitFieldFocusedPreview() {
    AlarmMeThemeProvider {
        Box(
            Modifier
                .background(LocalAlarmMeColors.current.background)
                .padding(16.dp)
        ) {
            DigitField(
                state = rememberDigitFieldState(value = 30, minValue = 0, maxValue = 59),
                value = 30,
                label = "MINUTE",
                enabled = true,
                imeAction = ImeAction.Done,
                focusRequester = remember { FocusRequester() },
                interactionSource = rememberFocusedInteractionSource(),
                onValueChange = {}
            )
        }
    }
}

@CommonScreenCategoriesPreview
@Composable
private fun DigitFieldDisabledPreview() {
    AlarmMeThemeProvider {
        Box(
            Modifier
                .background(LocalAlarmMeColors.current.background)
                .padding(16.dp)
        ) {
            DigitField(
                state = rememberDigitFieldState(value = 5, minValue = 0, maxValue = 59),
                value = 5,
                label = "MINUTE",
                enabled = false,
                imeAction = ImeAction.Done,
                focusRequester = remember { FocusRequester() },
                onValueChange = {}
            )
        }
    }
}
