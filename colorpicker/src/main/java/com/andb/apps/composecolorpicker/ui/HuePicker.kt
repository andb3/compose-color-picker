package com.andb.apps.composecolorpicker.ui

import androidx.compose.ui.Alignment
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawShadow
import androidx.compose.ui.drawBehind
import androidx.compose.ui.geometry.Radius
import androidx.compose.ui.gesture.scrollorientationlocking.Orientation
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorStop
import androidx.compose.ui.graphics.VerticalGradient
import androidx.compose.ui.unit.dp
import com.andb.apps.composecolorpicker.util.HSB

import com.andb.apps.composecolorpicker.util.toColor


@Composable
fun HuePicker(colors: List<Color>, hue: Float, modifier: Modifier = Modifier, onSelect: (hue: Float) -> Unit) {
    val colorStops = colors.mapIndexed { index, color -> ColorStop(1f / colors.size * index, color) }

    AlternativeSlider(
        position = hue,
        orientation = Orientation.Vertical,
        track = {
            Box(modifier = Modifier
                .width(32.dp)
                .fillMaxHeight()
                .align(Alignment.Center)
                .drawBehind {
                    val gradient = VerticalGradient(
                        colorStops = *colorStops.toTypedArray(),
                        startY = 0f,
                        endY = size.height
                    )

                    drawRoundRect(gradient, radius = Radius(16.dp.toPx()))
                }
            )
        },
        thumb = {
            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .size(28.dp)
                    .drawShadow(2.dp, shape = CircleShape)
                    .border(BorderStroke(3.dp, MaterialTheme.colors.background), CircleShape)
                    .background(HSB(hue, 1f, 1f).toColor(), CircleShape),
            )
        },
        onChange = onSelect,
        modifier = modifier.fillMaxHeight()
    )
}