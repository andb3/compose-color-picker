package com.andb.apps.composecolorpicker.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawShadow
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.gesture.DragObserver
import androidx.compose.ui.gesture.dragGestureFilter
import androidx.compose.ui.gesture.pressIndicatorGestureFilter
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.DensityAmbient
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
    val (boxSize, setBoxSize) = remember { mutableStateOf(IntSize(0, 0)) }
    val dragPosition = remember(boxSize, saturation, brightness) { mutableStateOf(Pair(boxSize.width * saturation, boxSize.height * (1f - brightness))) }
    val thumbSize = with(DensityAmbient.current) { 24.dp.toIntPx() }

    fun update() {
        //percent dragged from bottom left
        val xPct = dragPosition.value.first / boxSize.width
        val yPct = 1f - dragPosition.value.second / boxSize.height
        onSelect.invoke(xPct, yPct)
    }

    Box(
        modifier = modifier
            .dragGestureFilter(object : DragObserver {
                override fun onDrag(dragDistance: Offset): Offset {
                    val newX = (dragPosition.value.first + dragDistance.x).coerceIn(0f..boxSize.width.toFloat())
                    val newY = (dragPosition.value.second + dragDistance.y).coerceIn(0f..boxSize.height.toFloat())
                    dragPosition.value = Pair(newX, newY)
                    update()
                    return super.onDrag(dragDistance)
                }
            })
            .pressIndicatorGestureFilter(onStart = { pointer ->
                val onThumb = dragPosition.value.let { pointer.x in it.first..(it.first + thumbSize) && pointer.y in it.second..(it.second + thumbSize) }
                if (!onThumb) {
                    dragPosition.value = Pair(pointer.x.coerceIn(0f..boxSize.width.toFloat()), pointer.y.coerceIn(0f..boxSize.height.toFloat()))
                    update()
                }
            })
            .drawBehind {
                val saturationGradient = Brush.horizontalGradient(
                    ColorStop(0f, Color.Transparent), ColorStop(1f, HSB(hue, 1f, 1f).toColor()),
                    startX = 0f,
                    endX = size.width,
                    tileMode = TileMode.Clamp)
                val lightnessGradient = Brush.verticalGradient(
                    ColorStop(0f, Color.Transparent), ColorStop(1f, Color.Black),
                    startY = 0f,
                    endY = size.height,
                    tileMode = TileMode.Clamp)

                drawRoundRect(Color.White, cornerRadius = CornerRadius(12.dp.toPx()))
                drawRoundRect(saturationGradient, cornerRadius = CornerRadius(12.dp.toPx()))
                drawRoundRect(lightnessGradient, cornerRadius = CornerRadius(12.dp.toPx()))
            }
            .onGloballyPositioned {
                setBoxSize(it.size.run { IntSize(width - thumbSize, height - thumbSize) })
            }
    ) {
        val offsetDp = with(DensityAmbient.current) {
            val positionPx = dragPosition.value
            Pair(positionPx.first.toDp(), positionPx.second.toDp())
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
