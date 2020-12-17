package com.andb.apps.composecolorpicker.ui

import androidx.compose.ui.Alignment
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * An opacity picker that allows the user to select an alpha value from a slider
 * @param color The color shown on the picker (should have alpha of 1f)
 * @param alpha The current alpha
 * @param modifier The modifier to be applied to the OpacityPicker
 * @param onSelect The callback function for when the user changes the opacity
 */
@Composable
fun OpacityPicker(color: Color, alpha: Float, modifier: Modifier = Modifier, onSelect: (alpha: Float) -> Unit) {
    Row {
        OpacitySlider(color = color, alpha = alpha, modifier = modifier.fillMaxWidth().weight(1f), onSelect = onSelect)
        //OpacityTextField(alpha = alpha, onSelect = onSelect, modifier = Modifier.width(56.dp))
    }
}

@Composable
private fun OpacitySlider(color: Color, alpha: Float, modifier: Modifier = Modifier, onSelect: (alpha: Float) -> Unit){
    AlternativeSlider(
        position = alpha,
        track = {
            Box(modifier = Modifier
                .height(32.dp)
                .fillMaxWidth()
                .align(Alignment.Center)
                .drawBehind {
                    val gradient = Brush.horizontalGradient(colors = listOf(Color.Transparent, color),
                        startX = 0f,
                        endX = size.width
                    )
                    drawTiles(4, 16.dp)
                    drawRoundRect(gradient, cornerRadius = CornerRadius(16.dp.toPx()))
                }
            )
        },
        thumb = {
            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .border(BorderStroke(3.dp, MaterialTheme.colors.background), CircleShape)
                    .size(28.dp)
                    .shadow(2.dp, shape = CircleShape)
                    .drawBehind {
                        drawTiles(2, 14.dp)
                        drawCircle(color = color.copy(alpha = alpha), radius = 14.dp.toPx())
                    }
            )
        },
        modifier = modifier,
        onChange = onSelect
    )
}

@Composable
private fun OpacityTextField(alpha: Float, modifier: Modifier = Modifier, onSelect: (alpha: Float) -> Unit) {
    val text = remember(alpha) { mutableStateOf((alpha * 100).toInt().toString()) }
/*    Box(modifier){
        FilledTextField(value = text.value, onValueChange = { text.value = it }, label = {}, modifier = Modifier.height(32.dp))
    }*/
}

private fun DrawScope.drawTiles(rows: Int, radius: Dp, color1: Color = Color.LightGray, color2: Color = Color.White){
    val roundRect = RoundRect(0f, 0f, size.width, size.height, CornerRadius(radius.toPx(), radius.toPx()))
    clipPath(Path().apply { addRoundRect(roundRect) }) {
        for (row in 0 until rows){
            val tileSize = size.height / rows
            val tilesPerRow = (size.width/tileSize).toInt() + 1
            for (col in 0 until tilesPerRow){
                val grey = (row + col) % 2 == 0
                drawRect(if (grey) color1 else color2, topLeft = Offset(col * tileSize, row * tileSize), size = Size(tileSize, tileSize))
            }
        }
    }
}