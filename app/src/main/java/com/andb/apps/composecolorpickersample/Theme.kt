package com.andb.apps.composecolorpickersample

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp


@Composable
fun AppTheme(content: @Composable () -> Unit) {

    val typography = MaterialTheme.typography.copy(
        overline = MaterialTheme.typography.overline.copy(letterSpacing = 0.sp)
    )
    MaterialTheme(
        typography = typography,
        colors = MaterialTheme.colors,
        shapes = MaterialTheme.shapes,
        content = content
    )
}