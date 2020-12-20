package com.andb.apps.composecolorpicker

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.input.OffsetMap
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun VisualTransformation.Companion.prefixTransformation(prefix: String, color: Color) = object : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText = TransformedText(AnnotatedString("#", spanStyle = SpanStyle(color = color)) + text, object : OffsetMap {
        override fun originalToTransformed(offset: Int) = (offset + prefix.length).coerceAtLeast(0)
        override fun transformedToOriginal(offset: Int) = (offset - prefix.length).coerceAtLeast(0)
    })
}