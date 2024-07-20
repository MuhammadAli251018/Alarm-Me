package com.muhammadali.alarmme.common.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.magnifier
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.muhammadali.alarmme.common.ui.theme.AlarmMeTheme
import com.muhammadali.alarmme.common.ui.theme.Calypso


private object PreviewValue {
    val repeat = arrayOf(false, true,  true,  true,  true,  true,  true)
}

@Composable
fun DaysRepeatPicker(
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 15.sp,
    spacesCount: Int = 5,
    repeat: Array<Boolean> = PreviewValue.repeat,
    onDayClick: (Int) -> Unit = {},
) {
    val days = arrayOf('M', 'T', 'W', 'T', 'F', 'S', 'S')

    Row {
        repeat.forEachIndexed { index, isRepeated ->
            val color = if (isRepeated) MaterialTheme.colorScheme.primary else Color.White

            Text(
                modifier = modifier
                    .clickable {
                        onDayClick(index)
                    },
                text = days[index].toString(),
                color = color,
                fontSize = fontSize
            )

            Spacer(modifier = Modifier.width(spacesCount.dp))
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DaysRepeatPickerPreview() {
    AlarmMeTheme() {
        Column {
            DaysRepeatPicker()
        }
    }
}