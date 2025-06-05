package com.muhammadali.alarmme.common.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.muhammadali.alarmme.R
import com.muhammadali.alarmme.common.presentation.ui.theme.AlarmMeTheme


@Composable
fun Picker(
    modifier: Modifier = Modifier,
    text: String = "Text",
    fontSize: TextUnit = 15.sp,
    textColor: Color = Color.Black,
    textAlignment: Alignment = Alignment.CenterStart,
    iconAlignment: Alignment = Alignment.CenterEnd,
    iconText: String = "Text",
    iconTextFontSize: TextUnit = 15.sp,
    iconTextColor: Color = Color.Black,
    iconPainter: Painter = painterResource(id = R.drawable.ic_add_24),
    iconDescription: String = "Icon description",
    iconTint: Color = Color.Black,
    iconSize: Dp = 20.dp,
    onPickerClick: () -> Unit = {}
) {
    Row (
        modifier = modifier
            .clickable { onPickerClick() },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
            ){

        Box(
            modifier = Modifier
                .weight(1f),
            contentAlignment = textAlignment
        ){
            Text(
                text = text,
                fontSize = fontSize,
                color = textColor
            )
        }

        Box(
            modifier = Modifier
                .weight(1f),
            contentAlignment = iconAlignment
        ){
            TextIcon(
                text= iconText,
                fontSize = iconTextFontSize,
                textColor = iconTextColor,
                iconPainter = iconPainter,
                iconDescription = iconDescription,
                iconTint = iconTint,
                iconSize = iconSize
            )
        }
    }
}

//todo make overload to components functions so that you can use either bundles or the parameters

@Composable
fun TextIcon(
    modifier: Modifier= Modifier,
    text: String = "Text",
    fontSize: TextUnit = 15.sp,
    textColor: Color = Color.Black,
    iconPainter: Painter = painterResource(id = R.drawable.ic_add_24),
    iconDescription: String = "Icon description",
    iconTint: Color = Color.Black,
    iconSize: Dp = 20.dp
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = fontSize
        )
        Icon(
            modifier= Modifier.size(iconSize),
            painter = iconPainter,
            contentDescription = iconDescription,
            tint = iconTint,
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PickerPreview() {
    AlarmMeTheme {
        Picker()
    }
}