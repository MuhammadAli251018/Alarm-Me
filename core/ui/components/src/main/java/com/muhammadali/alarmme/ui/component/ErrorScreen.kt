package com.muhammadali.alarmme.ui.component

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.muhammadali.alarmme.ui.screenCategory.CommonScreenCategoriesPreview
import com.muhammadali.alarmme.ui.screenCategory.currentScreenCategory
import com.muhammadali.alarmme.ui.screenCategory.isExpandedWidthOrWider
import com.muhammadali.alarmme.ui.theme.AlarmMeIcons
import com.muhammadali.alarmme.ui.theme.AlarmMeThemeProvider
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeColors
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeShapes
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeTypography

@Composable
fun ErrorScreenContent(
    modifier: Modifier,
    errorHeading: String,
    errorDescription: String,
    onRetry: () -> Unit,
    onDismiss: () -> Unit
) {
    val colors = LocalAlarmMeColors.current
    val typography = LocalAlarmMeTypography.current

    BackHandler(onBack = onDismiss)

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = contentMaxWidth())
                .padding(horizontal = 22.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .border(1.dp, colors.outlineVariant, LocalAlarmMeShapes.current.full)
                    .clip(LocalAlarmMeShapes.current.full)
                    .clickable(onClick = onDismiss),
                contentAlignment = Alignment.Center
            ) {
                AmIcon(
                    painter = AlarmMeIcons.Close,
                    contentDescription = "Dismiss",
                    tint = colors.outline
                )
            }

            Text(
                text = errorHeading,
                style = typography.titleLarge,
                color = colors.onBackground,
                textAlign = TextAlign.Center
            )

            Text(
                text = errorDescription,
                style = typography.bodyMedium,
                color = colors.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            AmFilledTextButton(text = "Try again", onClick = onRetry)
        }
    }
}

@Composable
internal fun contentMaxWidth(): Dp =
    if (currentScreenCategory().isExpandedWidthOrWider) 1040.dp else 560.dp

@CommonScreenCategoriesPreview
@Composable
private fun ErrorAlarmListScreenContentPreview() {
    AlarmMeThemeProvider {
        ErrorScreenContent(
            modifier = Modifier
                .fillMaxSize()
                .background(LocalAlarmMeColors.current.background),
            errorHeading = "Couldn't load your alarms",
            errorDescription = "The alarm database didn't open. Your alarms are still saved — " +
                "nothing was deleted.",
            onRetry = {},
            onDismiss = {}
        )
    }
}