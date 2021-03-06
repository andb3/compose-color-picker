package com.andb.apps.composecolorpicker.ui

import androidx.compose.ui.Alignment
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.dp
import com.andb.apps.composecolorpicker.data.HSB

import com.andb.apps.composecolorpicker.data.toColor

/**
 * A slider that allows the user to select a hue value
 * Often used along with a [SaturationBrightnessPicker] to select a full color
 * @param hue The current hue
 * @param modifier The modifier to be applied to the HuePicker
 * @param onSelect The callback function for when the user changes the saturation or brightness
 */
@Composable
fun HueSlider(
    colors: List<Color>,
    hue: Float,
    modifier: Modifier = Modifier,
    orientation: Orientation = Orientation.Horizontal,
    thumb: @Composable (hueColor: Color) -> Unit = { hueColor -> DefaultHueThumb(hueColor) },
    onSelect: (hue: Float) -> Unit
) {
    val colorStops = colors.mapIndexed { index, color -> 1f / colors.size * index to color }

    AlternativeSlider(
        position = hue,
        orientation = orientation,
        track = {
            Box(modifier = Modifier
                .clipToBounds()
                .fillMaxWidth()
                .fillMaxHeight()
                .align(Alignment.Center)
                .drawBehind {
                    val gradient = when(orientation) {
                        Orientation.Vertical -> Brush.verticalGradient(startY = 0f, endY = size.height, tileMode = TileMode.Clamp, colorStops = colorStops.toTypedArray())
                        Orientation.Horizontal -> Brush.horizontalGradient(startX = 0f, endX = size.width, tileMode = TileMode.Clamp, colorStops = colorStops.toTypedArray())
                    }
                    drawRect(gradient)
                }
            )
        },
        thumb = {
            thumb(HSB(hue, 1f, 1f).toColor())
        },
        onChange = onSelect,
        modifier = modifier.fillMaxSize()
    )
}

@Composable
fun DefaultHueThumb(hueColor: Color) {
    Box(
        modifier = Modifier
            .padding(2.dp)
            .size(28.dp)
            .shadow(2.dp, shape = CircleShape)
            .border(BorderStroke(3.dp, Color.White), CircleShape)
            .background(hueColor, CircleShape),
    )
}