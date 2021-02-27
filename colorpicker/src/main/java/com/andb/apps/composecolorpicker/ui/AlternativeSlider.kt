package com.andb.apps.composecolorpicker.ui

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp


@Composable
fun AlternativeSlider(position: Float, modifier: Modifier = Modifier, orientation: Orientation = Orientation.Horizontal,  track: @Composable() BoxScope.() -> Unit, thumb: @Composable() BoxScope.() -> Unit, onChange: (Float) -> Unit){
    val (trackSize, setTrackSize) = remember { mutableStateOf(0) }
    val (thumbSize, setThumbSize) = remember { mutableStateOf(0) }
    val draggedPx = remember(trackSize, thumbSize, position) { mutableStateOf((trackSize - thumbSize) * position) }

    fun update() {
        onChange.invoke(draggedPx.value / (trackSize - thumbSize).toFloat())
    }

    Box(
        modifier = modifier.draggable(orientation = orientation, state = rememberDraggableState { delta ->
            val newPx = (draggedPx.value + delta)
            draggedPx.value = newPx.coerceIn(0f..(trackSize - thumbSize).toFloat())
            update()
        }).pointerInput(position) {
            this.detectTapGestures {
                val pressedPosition = if (orientation == Orientation.Horizontal) it.x else it.y
                val onThumb = pressedPosition in draggedPx.value..(draggedPx.value + thumbSize)
                if (!onThumb) {
                    draggedPx.value = pressedPosition.coerceIn(0f..(trackSize - thumbSize).toFloat())
                    update()
                }
            }
        }
    ) {
        Box(Modifier.onGloballyPositioned { setTrackSize(it.size.run { if (orientation == Orientation.Horizontal) width else height }) }) {
            track()
        }
        val offset = with(LocalDensity.current) { draggedPx.value.toDp() }
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