package com.andb.apps.composecolorpicker.ui

import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.gesture.pressIndicatorGestureFilter
import androidx.compose.ui.gesture.scrollorientationlocking.Orientation
import androidx.compose.ui.onGloballyPositioned
import androidx.compose.ui.platform.DensityAmbient
import androidx.compose.ui.unit.dp


@Composable
fun AlternativeSlider(position: Float, orientation: Orientation = Orientation.Horizontal, modifier: Modifier = Modifier, track: @Composable() BoxScope.() -> Unit, thumb: @Composable() BoxScope.() -> Unit, onChange: (Float) -> Unit){
    val (trackSize, setTrackSize) = remember { mutableStateOf(0) }
    val (thumbSize, setThumbSize) = remember { mutableStateOf(0) }
    val draggedPx = stateFor(trackSize, thumbSize, position) { (trackSize - thumbSize) * position }

    fun update() {
        onChange.invoke(draggedPx.value / (trackSize - thumbSize).toFloat())
    }

    Box(
        modifier = modifier.draggable(orientation) { delta ->
            val newPx = (draggedPx.value + delta)
            draggedPx.value = newPx.coerceIn(0f..(trackSize - thumbSize).toFloat())
            update()
        }.pressIndicatorGestureFilter(onStart = {
            val pressedPosition = if (orientation == Orientation.Horizontal) it.x else it.y
            val onThumb = pressedPosition in draggedPx.value..(draggedPx.value + thumbSize)
            if (!onThumb) {
                draggedPx.value = pressedPosition.coerceIn(0f..(trackSize - thumbSize).toFloat())
                update()
            }
        })
    ) {
        Box(Modifier.onGloballyPositioned { setTrackSize(it.size.run { if (orientation == Orientation.Horizontal) width else height }) }) {
            track()
        }
        val offset = with(DensityAmbient.current) { draggedPx.value.toDp() }
        Box(
            modifier = Modifier
                .onGloballyPositioned { setThumbSize(it.size.run { if (orientation == Orientation.Horizontal) width else height }) }
                .offset(
                    x = if (orientation == Orientation.Horizontal) offset else 0.dp,
                    y = if (orientation == Orientation.Vertical) offset else 0.dp
                )
        ) {
            thumb()
        }
    }
}