package com.andb.apps.composecolorpicker.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
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
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.andb.apps.composecolorpicker.data.HSB
import com.andb.apps.composecolorpicker.data.toColor


/**
 * A slider that allows the user to select an opacity value
 * @param color The color shown on the picker (should have alpha of 1f)
 * @param alpha The current alpha
 * @param modifier The modifier to be applied to the OpacityPicker
 * @param onSelect The callback function for when the user changes the opacity
 */
@Composable
fun OpacitySlider(
    color: Color,
    alpha: Float,
    modifier: Modifier = Modifier,
    orientation: Orientation = Orientation.Horizontal,
    thumb: @Composable (colorWithAlpha: Color) -> Unit = { colorWithAlpha -> DefaultOpacityThumb(colorWithAlpha) },
    onSelect: (alpha: Float) -> Unit
) {
    AlternativeSlider(
        position = alpha,
        orientation = orientation,
        track = {
            Box(modifier = Modifier
                .clipToBounds()
                .fillMaxHeight()
                .fillMaxWidth()
                .align(Alignment.Center)
                .drawBehind {
                    val gradient = when(orientation) {
                        Orientation.Vertical -> Brush.verticalGradient(colors = listOf(Color.Transparent, color), startY = 0f, endY = size.height)
                        Orientation.Horizontal -> Brush.horizontalGradient(colors = listOf(Color.Transparent, color), startX = 0f, endX = size.width)
                    }
                    when(orientation) {
                        Orientation.Vertical -> drawTilesVertical(4)
                        Orientation.Horizontal -> drawTilesHorizontal(4)
                    }
                    drawRect(gradient)
                }
            )
        },
        thumb = {
            thumb(color.copy(alpha = alpha))
        },
        modifier = modifier.fillMaxSize(),
        onChange = onSelect
    )
}

@Composable
fun OpacityTextField(alpha: Float, modifier: Modifier = Modifier, onSelect: (alpha: Float) -> Unit) {
    val text = remember(alpha) { mutableStateOf((alpha * 100).toInt().toString()) }
    val dragged = remember(alpha) { mutableStateOf(Pair(alpha, 0f)) }
    val onBackground = MaterialTheme.colors.onBackground
    Row(
        modifier = modifier
            .size(48.dp, 32.dp)
            .drawBehind {
                drawRect(
                    color = onBackground.copy(alpha = .5f),
                    topLeft = Offset.Zero.copy(y = this.size.height - 1.dp.toPx()),
                    size = this.size.copy(height = 1.dp.toPx())
                )
            }
            .background(
                onBackground.copy(alpha = .05f),
                shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
            )
            .draggable(
                orientation = Orientation.Vertical,
                onDragStopped = {
                    dragged.value = Pair(alpha, 0f)
                },
                state = rememberDraggableState { delta ->
                    dragged.value = dragged.value.copy(second = dragged.value.second - delta) // minus since dragging down is a positive delta, but should make numbers go down
                    val numbersDragged = dragged.value.second / 100
                    onSelect.invoke((dragged.value.first + numbersDragged).coerceIn(0f..1f))
                }
            )
            .draggable(
                orientation = Orientation.Horizontal,
                onDragStopped = {
                    dragged.value = Pair(alpha, 0f)
                },
                state = rememberDraggableState { delta ->
                    dragged.value = dragged.value.copy(second = dragged.value.second + delta)
                    val numbersDragged = dragged.value.second / 100
                    onSelect.invoke((dragged.value.first + numbersDragged).coerceIn(0f..1f))
                }
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            value = text.value,
            onValueChange = { newText ->
                text.value = newText.filter { it in '0'..'9' }.let { if (it.length > 3) it.dropLast(it.length - 3) else it }
                if (newText.toIntOrNull() != null) {
                    onSelect.invoke((newText.toInt() / 100f).coerceIn(0f..1f))
                }
            },
            textStyle = TextStyle(textAlign = TextAlign.Center, color = MaterialTheme.colors.onBackground),
            visualTransformation = { text -> TransformedText(text + AnnotatedString("%"), OffsetMapping.Identity) }
        )
    }
}

private fun DrawScope.drawTilesHorizontal(rows: Int, color1: Color = Color.LightGray, color2: Color = Color.White) {
    for (row in 0 until rows) {
        val tileSize = size.height / rows
        val tilesPerRow = (size.width / tileSize).toInt() + 1
        for (col in 0 until tilesPerRow) {
            val grey = (row + col) % 2 == 0
            drawRect(if (grey) color1 else color2, topLeft = Offset(col * tileSize, row * tileSize), size = Size(tileSize, tileSize))
        }
    }
}

private fun DrawScope.drawTilesVertical(columns: Int, color1: Color = Color.LightGray, color2: Color = Color.White) {
    for (column in 0 until columns) {
        val tileSize = size.width / columns
        val tilesPerRow = (size.height / tileSize).toInt() + 1
        for (row in 0 until tilesPerRow) {
            val grey = (column + row) % 2 == 0
            drawRect(if (grey) color1 else color2, topLeft = Offset(column * tileSize, row * tileSize), size = Size(tileSize, tileSize))
        }
    }
}

@Composable
fun DefaultOpacityThumb(colorWithAlpha: Color) {
    Box(
        modifier = Modifier
            .padding(2.dp)
            .border(BorderStroke(3.dp, Color.White), CircleShape)
            .size(28.dp)
            .shadow(2.dp, shape = CircleShape)
            .drawBehind {
                drawTilesHorizontal(2)
                drawRect(color = colorWithAlpha)
            }
    )
}