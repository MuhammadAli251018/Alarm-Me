package com.muhammadali.alarmme.feature.ringtonePicker.presentatoin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import com.muhammadali.alarmme.feature.ringtonePicker.presentatoin.component.RingtoneItem
import com.muhammadali.alarmme.feature.ringtonePicker.presentatoin.component.RingtoneItemsList
import com.muhammadali.alarmme.feature.ringtonePicker.presentatoin.component.RingtonePickerTopBar
import com.muhammadali.alarmme.feature.ringtonePicker.presentatoin.component.previewRingtones
import com.muhammadali.alarmme.feature.ringtonePicker.presentatoin.model.RingtonePickerEvent
import com.muhammadali.alarmme.feature.ringtonePicker.presentatoin.model.RingtonePickerState
import com.muhammadali.alarmme.ui.component.AmOutlineButtonBar
import com.muhammadali.alarmme.ui.screen.ErrorScreenContent
import com.muhammadali.alarmme.ui.screenCategory.CommonScreenCategoriesPreview
import com.muhammadali.alarmme.ui.screenCategory.MediumWidthExpandedHeightPreview
import com.muhammadali.alarmme.ui.screenCategory.currentScreenCategory
import com.muhammadali.alarmme.ui.theme.AlarmMeThemeProvider
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeColors

class RingtonePickerScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel: RingtonePickerScreenModel = koinScreenModel()
        val state by screenModel.state.collectAsState()
        val dispatch = screenModel::handleEvent
        val navigator = LocalNavigator.current

        RingtonePickerScreenContent(
            modifier = Modifier
                .fillMaxSize()
                .background(LocalAlarmMeColors.current.background),
            state = state,
            onBackClick = { navigator?.pop() },
            onSaveClick = { navigator?.pop() },
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
    onRingtoneClick: (String) -> Unit,
    onRingtoneSelect: (String) -> Unit,
    onCustomSoundClick: () -> Unit,
    onRetry: () -> Unit,
    onDismiss: () -> Unit
) {
    when (state) {
        RingtonePickerState.Loading -> LoadingRingtoneScreenContent(modifier = modifier)

        RingtonePickerState.Error -> ErrorScreenContent(
            modifier = modifier,
            errorHeading = "Couldn't load ringtones",
            errorDescription = "Something went wrong while loading the sounds. Nothing was changed.",
            onRetry = onRetry,
            onDismiss = onDismiss
        )

        is RingtonePickerState.Loaded -> RingtoneScreenContent(
            modifier = modifier,
            ringtones = state.ringtones,
            selectedRingtoneId = state.selectedRingtoneId,
            playingRingtoneId = state.playingRingtoneId,
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
    ringtones: List<RingtoneItem>,
    selectedRingtoneId: String?,
    playingRingtoneId: String?,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    onRingtoneClick: (String) -> Unit,
    onRingtoneSelect: (String) -> Unit,
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
                    text = "Add custom sound",
                    icon = { Text(text = "＋", color = colors.primary) },
                    onClick = onCustomSoundClick
                )
            }
        }
    }
}

@Composable
private fun PreviewRingtoneScreen(
    ringtones: List<RingtoneItem> = previewRingtones(),
    selectedRingtoneId: String? = "sunrise-chime",
    playingRingtoneId: String? = "morning-birds",
) {
    AlarmMeThemeProvider {
        RingtoneScreenContent(
            modifier = Modifier
                .fillMaxSize()
                .background(LocalAlarmMeColors.current.background),
            ringtones = ringtones,
            selectedRingtoneId = selectedRingtoneId,
            playingRingtoneId = playingRingtoneId,
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