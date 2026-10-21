package dev.alidroid.alarmme.ui.screen.ringtonePicker.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import dev.alidroid.alarmme.alarms.domain.Ringtone
import dev.alidroid.alarmme.alarms.domain.RingtoneId
import dev.alidroid.alarmme.navigation.LocalNavResultRepository
import dev.alidroid.alarmme.navigation.NavResultKey
import dev.alidroid.alarmme.navigation.NavigationResult
import dev.alidroid.alarmme.ui.screen.ringtonePicker.R
import dev.alidroid.alarmme.ui.screen.ringtonePicker.presentation.component.RingtoneItemsList
import dev.alidroid.alarmme.ui.screen.ringtonePicker.presentation.component.RingtonePickerTopBar
import dev.alidroid.alarmme.ui.screen.ringtonePicker.presentation.component.previewRingtones
import dev.alidroid.alarmme.ui.screen.ringtonePicker.presentation.model.RingtoneItemState
import dev.alidroid.alarmme.ui.screen.ringtonePicker.presentation.model.RingtonePickerEvent
import dev.alidroid.alarmme.ui.screen.ringtonePicker.presentation.model.RingtonePickerState
import dev.alidroid.alarmme.ui.component.AmIcon
import dev.alidroid.alarmme.ui.component.AmOutlineButtonBar
import dev.alidroid.alarmme.ui.component.ErrorScreenContent
import dev.alidroid.alarmme.ui.screenCategory.CommonScreenCategoriesPreview
import dev.alidroid.alarmme.ui.screenCategory.MediumWidthExpandedHeightPreview
import dev.alidroid.alarmme.ui.screenCategory.RtlPreview
import dev.alidroid.alarmme.ui.screenCategory.currentScreenCategory
import dev.alidroid.alarmme.ui.theme.AlarmMeIcons
import dev.alidroid.alarmme.ui.theme.AlarmMeThemeProvider
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeColors

/**
 * What the picker hands back to whoever navigated to it — the whole tone, so the caller has
 * everything it needs without looking anything up.
 */
data class PickedRingtoneResult(val ringtone: Ringtone) : NavigationResult

class RingtonePickerScreen : Screen {

    companion object {
        val RINGTONE_NAV_RESULT_KEY = NavResultKey<PickedRingtoneResult>("picked_ringtone")
    }

    @Composable
    override fun Content() {
        val screenModel: RingtonePickerScreenModel = koinScreenModel()
        val state by screenModel.state.collectAsState()
        val dispatch = screenModel::handleEvent
        val navigator = LocalNavigator.current
        val navResultRepository = LocalNavResultRepository.current

        RingtonePickerScreenContent(
            modifier = Modifier
                .imePadding()
                .systemBarsPadding()
                .fillMaxSize()
                .background(LocalAlarmMeColors.current.background),
            state = state,
            onBackClick = { navigator?.pop() },
            onSaveClick = {
                val loaded = state as? RingtonePickerState.Loaded ?: return@RingtonePickerScreenContent
                val selectedRingtone = loaded.selectedRingtone ?: return@RingtonePickerScreenContent

                navResultRepository.save(
                    RINGTONE_NAV_RESULT_KEY,
                    PickedRingtoneResult(selectedRingtone)
                )

                navigator?.pop()
            },
            onRingtoneClick = { id -> dispatch(RingtonePickerEvent.OnRingtoneClick(id)) },
            onRingtoneSelect = { id -> dispatch(RingtonePickerEvent.OnRingtoneSelect(id)) },
            onCustomSoundClick = {}, // TODO: open the custom sound picker once it exists
            onRetry = { dispatch(RingtonePickerEvent.OnRetry) },
            onDismiss = { /*TODO*/ }
        )
    }
}

@Composable
fun RingtonePickerScreenContent(
    modifier: Modifier = Modifier,
    state: RingtonePickerState,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    onRingtoneClick: (RingtoneId) -> Unit,
    onRingtoneSelect: (RingtoneId) -> Unit,
    onCustomSoundClick: () -> Unit,
    onRetry: () -> Unit,
    onDismiss: () -> Unit
) {
    when (state) {
        RingtonePickerState.Loading -> LoadingRingtoneScreenContent(modifier = modifier)

        RingtonePickerState.Error -> ErrorScreenContent(
            modifier = modifier,
            errorHeading = stringResource(R.string.ringtone_picker_error_heading),
            errorDescription = stringResource(R.string.ringtone_picker_error_description),
            onRetry = onRetry,
            onDismiss = onDismiss
        )

        is RingtonePickerState.Loaded -> RingtoneScreenContent(
            modifier = modifier,
            ringtones = state.ringtones,
            // Only the id goes further down: the rows compare against it, and have no use for
            // the rest of the tone.
            selectedRingtoneId = state.selectedRingtone?.id,
            playingRingtoneId = state.playingRingtoneId,
            canSave = state.selectedRingtone != null,
            onBackClick = onBackClick,
            onSaveClick = onSaveClick,
            onRingtoneClick = onRingtoneClick,
            onRingtoneSelect = onRingtoneSelect,
            onCustomSoundClick = onCustomSoundClick
        )
    }
}

@Composable
fun LoadingRingtoneScreenContent(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = LocalAlarmMeColors.current.primary)
    }
}

@Composable
fun RingtoneScreenContent(
    modifier: Modifier = Modifier,
    ringtones: List<RingtoneItemState>,
    selectedRingtoneId: RingtoneId?,
    playingRingtoneId: RingtoneId?,
    canSave: Boolean,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    onRingtoneClick: (RingtoneId) -> Unit,
    onRingtoneSelect: (RingtoneId) -> Unit,
    onCustomSoundClick: () -> Unit,
) {
    val screenCategory = currentScreenCategory()
    val colors = LocalAlarmMeColors.current

    Box(modifier) {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RingtonePickerTopBar(
                modifier = Modifier.fillMaxWidth(),
                canSave = canSave,
                onBackClick = onBackClick,
                onSaveClick = onSaveClick,
                onCustomSoundClick = onCustomSoundClick
            )

            Spacer(Modifier.height(16.dp))

            RingtoneItemsList(
                modifier = Modifier
                    .screenConstraints(screenCategory)
                    .weight(1f),
                ringtones = ringtones,
                selectedRingtoneId = selectedRingtoneId,
                playingRingtoneId = playingRingtoneId,
                onRingtoneClick = onRingtoneClick,
                onRingtoneSelect = onRingtoneSelect
            )
        }

        if (!screenCategory.hasCustomSoundInTopBar()) {
            Column(
                modifier = Modifier.align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(
                    Modifier
                        .height(8.dp)
                        .screenConstraints(screenCategory)
                        .fillMaxWidth()
                        .drawBehind {
                            drawLine(
                                color = colors.outline.copy(0.5f),
                                start = Offset(0f, 0f),
                                end = Offset(size.width, 0f),
                                strokeWidth = 1f
                            )
                        }
                )

                AmOutlineButtonBar(
                    modifier = Modifier
                        .screenConstraints(screenCategory)
                        .padding(top = 16.dp, bottom = 22.dp),
                    text = stringResource(R.string.ringtone_picker_add_custom_sound),
                    icon = { AmIcon(AlarmMeIcons.Add, null, tint = colors.primary) },
                    onClick = onCustomSoundClick
                )
            }
        }
    }
}

@Composable
private fun PreviewRingtoneScreen(
    ringtones: List<RingtoneItemState> = previewRingtones(),
    selectedRingtoneId: RingtoneId? = RingtoneId("sunrise-chime"),
    playingRingtoneId: RingtoneId? = RingtoneId("morning-birds"),
) {
    AlarmMeThemeProvider {
        RingtoneScreenContent(
            modifier = Modifier
                .fillMaxSize()
                .background(LocalAlarmMeColors.current.background),
            ringtones = ringtones,
            selectedRingtoneId = selectedRingtoneId,
            playingRingtoneId = playingRingtoneId,
            canSave = selectedRingtoneId != null,
            onBackClick = {},
            onSaveClick = {},
            onRingtoneClick = {},
            onRingtoneSelect = {},
            onCustomSoundClick = {}
        )
    }
}

/**
 * The three common buckets cover phone-portrait (bottom dock bar), phone-landscape via compact
 * height (top-bar chip), and expanded width (top-bar chip).
 */
@CommonScreenCategoriesPreview
@RtlPreview
@Composable
private fun RingtoneScreenContentPreview() {
    PreviewRingtoneScreen()
}

/** Medium-width tablet portrait - still the bottom dock bar, just at the wider 480dp frame. */
@MediumWidthExpandedHeightPreview
@Composable
private fun RingtoneScreenContentMediumWidthPreview() {
    PreviewRingtoneScreen()
}

/** How the picker now opens: nothing picked, so Save is disabled. */
@CommonScreenCategoriesPreview
@Composable
private fun RingtoneScreenContentNoSelectionPreview() {
    PreviewRingtoneScreen(selectedRingtoneId = null, playingRingtoneId = null)
}

@CommonScreenCategoriesPreview
@Composable
private fun RingtonePickerScreenContentLoadingPreview() {
    AlarmMeThemeProvider {
        RingtonePickerScreenContent(
            modifier = Modifier
                .fillMaxSize()
                .background(LocalAlarmMeColors.current.background),
            state = RingtonePickerState.Loading,
            onBackClick = {},
            onSaveClick = {},
            onRingtoneClick = {},
            onRingtoneSelect = {},
            onCustomSoundClick = {},
            onRetry = {},
            onDismiss = {}
        )
    }
}

@CommonScreenCategoriesPreview
@Composable
private fun RingtonePickerScreenContentErrorPreview() {
    AlarmMeThemeProvider {
        RingtonePickerScreenContent(
            modifier = Modifier
                .fillMaxSize()
                .background(LocalAlarmMeColors.current.background),
            state = RingtonePickerState.Error,
            onBackClick = {},
            onSaveClick = {},
            onRingtoneClick = {},
            onRingtoneSelect = {},
            onCustomSoundClick = {},
            onRetry = {},
            onDismiss = {}
        )
    }
}
