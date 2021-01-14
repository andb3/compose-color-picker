package com.andb.apps.composecolorpicker.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.gesture.scrollorientationlocking.Orientation
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
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
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        OpacitySlider(color = color, alpha = alpha, modifier = modifier.fillMaxWidth().weight(1f), onSelect = onSelect)
        OpacityTextField(alpha = alpha, onSelect = onSelect, modifier = Modifier.width(48.dp))
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
                    .border(BorderStroke(3.dp, Color.White), CircleShape)
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
    val dragged = remember(alpha) { mutableStateOf(Pair(alpha, 0f)) }
    val onBackground = MaterialTheme.colors.onBackground
    Row(
        modifier = modifier
            .preferredSize(48.dp, 32.dp)
            .drawBehind {
                drawRect(
                    color = onBackground.copy(alpha = .5f),
                    topLeft = Offset.Zero.copy(y = this.size.height - 1.dp.toPx()),
                    size = this.size.copy(height = 1.dp.toPx())
                )
            }
            .background(
                onBackground.copy(alpha = .05f),
                shape = RoundedCornerShape(topLeft = 8.dp, topRight = 8.dp)
            )
            .draggable(
                orientation = Orientation.Vertical,
                onDragStopped = {
                    dragged.value = Pair(alpha, 0f)
                },
                onDrag = { delta ->
                    dragged.value = dragged.value.copy(second = dragged.value.second - delta) // minus since dragging down is a positive delta, but should make numbers go down
                    val numbersDragged = dragged.value.second.toDp().value / 100
                    onSelect.invoke((dragged.value.first + numbersDragged).coerceIn(0f..1f))
                }
            )
            .draggable(
                orientation = Orientation.Horizontal,
                onDragStopped = {
                    dragged.value = Pair(alpha, 0f)
                },
                onDrag = { delta ->
                    dragged.value = dragged.value.copy(second = dragged.value.second + delta) // minus since dragging down is a positive delta, but should make numbers go down
                    val numbersDragged = dragged.value.second.toDp().value / 100
                    onSelect.invoke((dragged.value.first + numbersDragged).coerceIn(0f..1f))
                }
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            value = text.value,
            onValueChange = {
                if (it.toIntOrNull() != null) {
                    onSelect.invoke((it.toInt() / 100f).coerceIn(0f..1f))
                }
            },
            textStyle = TextStyle(textAlign = TextAlign.Center, color = MaterialTheme.colors.onBackground),
            visualTransformation = object : VisualTransformation {
                override fun filter(text: AnnotatedString): TransformedText =
                    TransformedText(text + AnnotatedString("%"), OffsetMapping.Identity)
            }
        )
    }
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