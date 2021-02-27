package com.andb.apps.composecolorpicker.ui


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Palette
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.input.VisualTransformation
import androidx.core.graphics.toColorInt
import com.andb.apps.composecolorpicker.prefixTransformation


@Composable
fun ColorPickerTextField(selected: Color, modifier: Modifier = Modifier, onValid: (color: Color) -> Unit) {
    val currentText = remember(selected) { mutableStateOf(selected.toArgb().toUInt().toString(16).removePrefix("ff")) }
    TextField(
        value = currentText.value.toUpperCase(),
        onValueChange = { text ->
            currentText.value = text.filter { it in 'a'..'f' || it in 'A'..'F' || it in '0'..'9' }.let { if (it.length > 6) it.dropLast(it.length - 6) else it }
            println("currentText.value = ${currentText.value}")
            currentText.value.toColorIntOrNull()?.let { onValid.invoke(Color(it)) }
        },
        label = { Text(text = "Hex Color") },
        leadingIcon = { Icon(imageVector = Icons.Filled.Palette, null) },
        modifier = modifier.fillMaxWidth(),
        //isErrorValue = currentText.value.toColorIntOrNull() == null,
        visualTransformation = VisualTransformation.prefixTransformation(prefix = "#", color = MaterialTheme.colors.onBackground.copy(alpha = .5f))
    )
}

private fun String.toColorIntOrNull(): Int? {
    if (this.isEmpty()) return null
    return try {
        ("#$this").toColorInt()
    } catch (e: IllegalArgumentException) {
        null
    }
}