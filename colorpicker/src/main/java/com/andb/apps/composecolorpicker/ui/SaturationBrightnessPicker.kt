package com.andb.apps.composecolorpicker.ui

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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.andb.apps.composecolorpicker.data.HSB
import com.andb.apps.composecolorpicker.data.toColor
import com.andb.apps.composecolorpicker.minus

/**
 * A color picker that allows the user to select a saturation and brightness value
 * Often used along with a [HueSlider] to select a full color
 * @param hue The hue to create a full color with the saturation and brightness
 * @param saturation The current saturation
 * @param brightness The current brightness
 * @param modifier The modifier to be applied to the SaturationBrightnessPicker
 * @param onSelect The callback function for when the user changes the saturation or brightness
 */
@Composable
fun SaturationBrightnessPicker(
    hue: Float,
    saturation: Float,
    brightness: Float,
    modifier: Modifier = Modifier,
    thumb: @Composable (color: Color) -> Unit = { color -> DefaultSaturationBrightnessThumb(color) },
    onSelect: (saturation: Float, brightness: Float) -> Unit
) {
    val (boxSize, setBoxSize) = remember { mutableStateOf(Size(1f, 1f)) }
    val (thumbSize, setThumbSize) = remember { mutableStateOf(Size(0f, 0f)) }
    val adjustedBoxSize: Size = derivedStateOf { boxSize - thumbSize }.value
    val dragPosition = remember(adjustedBoxSize, saturation, brightness) { mutableStateOf(Offset(adjustedBoxSize.width * saturation, adjustedBoxSize.height * (1f - brightness))) }

    val onSelectState = rememberUpdatedState(onSelect)

    Box(
        modifier = modifier
            .onGloballyPositioned {
                setBoxSize(it.size.toSize())
            }
            .pointerInput(adjustedBoxSize) {
                detectTapGestures(
                    onPress = { pointer ->
                        val onThumb = dragPosition.value.let { pointer.x in (it.x - thumbSize.width)..(it.x + thumbSize.width) && pointer.y in (it.y - thumbSize.height)..(it.y + thumbSize.height) }
                        println("onThumb = $onThumb, pointer = $pointer, dragPosition = ${dragPosition.value}")
                        if (!onThumb) {
                            dragPosition.value = Offset(pointer.x.coerceIn(0f..adjustedBoxSize.width), pointer.y.coerceIn(0f..adjustedBoxSize.height))
                            onSelectState.value.invoke(dragPosition.value.x / adjustedBoxSize.width, 1f - dragPosition.value.y / adjustedBoxSize.height)
                        }
                    }
                )
            }
            .pointerInput(adjustedBoxSize) {
                detectDragGestures(
                    onDragStart = { dragPosition.value = Offset(it.x.coerceIn(0f..adjustedBoxSize.width), it.y.coerceIn(0f..adjustedBoxSize.height)) },
                    onDrag = { change, dragAmount ->
                        change.consumePositionChange()
                        println("detected drag")
                        val newX = (dragPosition.value.x + dragAmount.x).coerceIn(0f..adjustedBoxSize.width)
                        val newY = (dragPosition.value.y + dragAmount.y).coerceIn(0f..adjustedBoxSize.height)
                        dragPosition.value = Offset(newX.coerceIn(0f..adjustedBoxSize.width), newY.coerceIn(0f..adjustedBoxSize.height))
                        onSelectState.value.invoke(dragPosition.value.x / adjustedBoxSize.width, 1f - dragPosition.value.y / adjustedBoxSize.height)
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
                drawRect(Color.White)
                drawRect(saturationGradient)
                drawRect(lightnessGradient)
            }
    ) {
        val offsetDp = with(LocalDensity.current) {
            val positionPx = dragPosition.value
            //Pair(animateDpAsState(positionPx.x.toDp(), spring(stiffness = Spring.StiffnessHigh)).value, animateDpAsState(positionPx.y.toDp(), spring(stiffness = Spring.StiffnessHigh)).value)
            Pair(positionPx.x.toDp(), positionPx.y.toDp())
        }
        Box(modifier = Modifier
            .offset(offsetDp.first, offsetDp.second)
            .onGloballyPositioned {
                setThumbSize(it.size.toSize())
            }
        ) {
            thumb(color = HSB(hue, saturation, brightness).toColor())
        }
    }
}

@Composable
fun DefaultSaturationBrightnessThumb(color: Color) {
    Box(
        modifier = Modifier
            .size(24.dp)
            .shadow(2.dp, shape = CircleShape)
            .border(BorderStroke(3.dp, Color.White), CircleShape)
            .background(color, CircleShape)
    )
}