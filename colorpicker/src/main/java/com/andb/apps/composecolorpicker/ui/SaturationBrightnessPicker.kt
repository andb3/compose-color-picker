package com.andb.apps.composecolorpicker.ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.andb.apps.composecolorpicker.data.HSB
import com.andb.apps.composecolorpicker.data.toColor

/**
 * A color picker that allows the user to select a saturation and brightness value
 * Often used along with a [HuePicker] to select a full color
 * @param hue The hue to create a full color with the saturation and brightness
 * @param saturation The current saturation
 * @param brightness The current brightness
 * @param modifier The modifier to be applied to the SaturationBrightnessPicker
 * @param onSelect The callback function for when the user changes the saturation or brightness
 */
@Composable
fun SaturationBrightnessPicker(hue: Float, saturation: Float, brightness: Float, modifier: Modifier = Modifier, onSelect: (saturation: Float, brightness: Float) -> Unit) {
    val (boxSize, setBoxSize) = remember { mutableStateOf(Size(1f, 1f)) }
    val dragPosition = remember(boxSize, saturation, brightness) { mutableStateOf(Offset(boxSize.width * saturation, boxSize.height * (1f - brightness))) }
    val thumbSize = with(LocalDensity.current) { 24.dp.toPx() }

    fun update() {
        //percent dragged from bottom left
        val xPct = dragPosition.value.x / boxSize.width
        val yPct = 1f - dragPosition.value.y / boxSize.height
        onSelect.invoke(xPct, yPct)
    }


    Box(
        modifier = modifier
            .onGloballyPositioned {
                setBoxSize(it.size.run { Size(width - thumbSize, height - thumbSize) })
            }
            .pointerInput(boxSize) {
                detectTapGestures { pointer ->
                    val onThumb = dragPosition.value.let { pointer.x in (it.x - thumbSize)..(it.x + thumbSize) && pointer.y in (it.y - thumbSize)..(it.y + thumbSize) }
                    println("onThumb = $onThumb, pointer = $pointer, dragPosition = ${dragPosition.value}")
                    if (!onThumb) {
                        dragPosition.value = Offset(pointer.x.coerceIn(0f..boxSize.width), pointer.y.coerceIn(0f..boxSize.height))
                        update()
                    }
                }
            }
            .pointerInput(boxSize) {
                detectDragGestures(
                    onDragStart = { dragPosition.value = Offset(it.x.coerceIn(0f..boxSize.width), it.y.coerceIn(0f..boxSize.height)) },
                    onDrag = { change, dragAmount ->
                        change.consumePositionChange()
                        println("detected drag")
                        val newX = (dragPosition.value.x + dragAmount.x).coerceIn(0f..boxSize.width)
                        val newY = (dragPosition.value.y + dragAmount.y).coerceIn(0f..boxSize.height)
                        //dragPosition.value = Offset(change.position.x.coerceIn(0f..boxSize.width), change.position.y.coerceIn(0f..boxSize.height))
                        dragPosition.value = Offset(newX.coerceIn(0f..boxSize.width), newY.coerceIn(0f..boxSize.height))
                        update()
                    }
                )
            }
            .drawBehind {
                val saturationGradient = Brush.horizontalGradient(
                    0f to Color.Transparent, 1f to HSB(hue, 1f, 1f).toColor(),
                    startX = 0f,
                    endX = size.width,
                    tileMode = TileMode.Clamp
                )
                val lightnessGradient = Brush.verticalGradient(
                    0f to Color.Transparent, 1f to Color.Black,
                    startY = 0f,
                    endY = size.height,
                    tileMode = TileMode.Clamp
                )

                drawRoundRect(Color.White, cornerRadius = CornerRadius(12.dp.toPx()))
                drawRoundRect(saturationGradient, cornerRadius = CornerRadius(12.dp.toPx()))
                drawRoundRect(lightnessGradient, cornerRadius = CornerRadius(12.dp.toPx()))
            }
    ) {
        val offsetDp = with(LocalDensity.current) {
            val positionPx = dragPosition.value
            Pair(animateDpAsState(positionPx.x.toDp(), spring(stiffness = Spring.StiffnessHigh)).value, animateDpAsState(positionPx.y.toDp(), spring(stiffness = Spring.StiffnessHigh)).value)
            Pair(positionPx.x.toDp(), positionPx.y.toDp())
        }
        Box(
            modifier = Modifier
                .offset(offsetDp.first, offsetDp.second)
                .size(24.dp)
                .shadow(2.dp, shape = CircleShape)
                .border(BorderStroke(3.dp, Color.White), CircleShape)
                .background(HSB(hue, saturation, brightness).toColor(), CircleShape)
        )
    }
}
