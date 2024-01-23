package com.muhammadali.alarmme.feature.main.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.muhammadali.alarmme.common.ui.theme.AlarmMeTheme

@Composable
fun ChooseColumn(
    data: Array<String> = arrayOf("bla", "bla", "bla", "bla", ),
    chosenIndex: Int = 2,
    onItemClick: (Int) -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        itemsIndexed(data) {index, itemData ->
            ChoseColumnItem(isChosen = chosenIndex == index, itemData = itemData) {
                onItemClick(index)
            }
        }
    }
}

@Composable
fun ChoseColumnItem(
    isChosen: Boolean,
    itemData: String,
    onClick: () -> Unit
){
    Column (
        Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .clickable { onClick() },
        verticalArrangement = Arrangement.Center
    ) {

        Row (
            verticalAlignment = Alignment.CenterVertically
                ){
            Box(
                modifier = Modifier
                    .weight(1f),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = itemData,
                    fontSize = 25.sp,
                    color = Color.White
                )
            }

            Box(
                modifier = Modifier
                    .padding(5.dp)
                    .weight(1f),
                contentAlignment = Alignment.CenterEnd
            ) {
                val circleColor = MaterialTheme.colorScheme.primary

                if (isChosen) {
                    Canvas(
                        Modifier
                            .padding(horizontal = 20.dp)
                            .background(Color.Red)) {
                        drawCircle(
                            color = circleColor,
                            radius = 15.dp.value,
                            center = center
                        )
                    }
                }
            }

        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewChoseColumnItem() {
    AlarmMeTheme {
        ChooseColumn()
    }
}