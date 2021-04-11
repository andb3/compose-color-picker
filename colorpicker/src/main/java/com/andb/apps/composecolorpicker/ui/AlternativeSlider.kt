package com.andb.apps.composecolorpicker.ui

import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize


@Composable
fun AlternativeSlider(
    position: Float,
    modifier: Modifier = Modifier,
    orientation: Orientation = Orientation.Horizontal,
    track: @Composable() BoxScope.() -> Unit,
    thumb: @Composable() BoxScope.() -> Unit,
    onChange: (Float) -> Unit
) {
    val (trackSize, setTrackSize) = remember { mutableStateOf(0f) }
    val (thumbSize, setThumbSize) = remember { mutableStateOf(0f) }
    val adjustedTrackSize = derivedStateOf { trackSize - thumbSize }.value
    val draggedPx = remember(adjustedTrackSize, position) { mutableStateOf(adjustedTrackSize * position) }

    fun update() {
        onChange.invoke(draggedPx.value / adjustedTrackSize)
    }

    Box(
        modifier = modifier
            .pointerInput(adjustedTrackSize) {
                detectDragGestures(
                    onDragStart = {
                        val startPosition = if (orientation == Orientation.Horizontal) it.x else it.y
                        draggedPx.value = startPosition.coerceIn(0f..adjustedTrackSize)
                    },
                    onDrag = { change, dragAmount ->
                        change.consumePositionChange()
                        val delta = if (orientation == Orientation.Horizontal) dragAmount.x else dragAmount.y
                        val newPx = (draggedPx.value + delta)
                        draggedPx.value = newPx.coerceIn(0f..adjustedTrackSize)
                        update()
                    }
                )
            }
            .pointerInput(adjustedTrackSize) {
                this.detectTapGestures {
                    val pressedPosition = if (orientation == Orientation.Horizontal) it.x else it.y
                    val onThumb = pressedPosition in draggedPx.value..(draggedPx.value + thumbSize)
                    if (!onThumb) {
                        draggedPx.value = pressedPosition.coerceIn(0f..adjustedTrackSize)
                        update()
                    }
                }
            }
    ) {
        Box(Modifier.onGloballyPositioned { setTrackSize(it.size.toSize().run { if (orientation == Orientation.Horizontal) width else height }) }) {
            track()
        }
        val offset = with(LocalDensity.current) { draggedPx.value.toDp() }
        Box(
            modifier = Modifier
                .onGloballyPositioned {
                    setThumbSize(it.size
                        .toSize()
                        .run { if (orientation == Orientation.Horizontal) width else height })
                }
                .offset(
                    x = if (orientation == Orientation.Horizontal) offset else 0.dp,
                    y = if (orientation == Orientation.Vertical) offset else 0.dp
                )
        ) {
            thumb()
        }
    }
}