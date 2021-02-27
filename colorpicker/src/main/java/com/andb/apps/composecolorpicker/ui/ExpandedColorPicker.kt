package com.andb.apps.composecolorpicker.ui

import androidx.compose.material.Text
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.andb.apps.composecolorpicker.data.HSB

import com.andb.apps.composecolorpicker.data.toColor
import com.andb.apps.composecolorpicker.data.toHSB

private val hues: List<Color> = (0..360).map { HSB(it / 360f, 1f, 1f).toColor() }

/**
 * A color picker that allows the user to select a hue, saturation, brightness, and opacity value
 * @param selected The currently selected color
 * @param modifier The modifier to be applied to the ExpandedColorPicker
 * @param onSelect The callback function for when the user changes the color
 */
@Composable
fun ExpandedColorPicker(selected: Color, modifier: Modifier = Modifier, onSelect: (color: Color) -> Unit) {
    val oldHSB = remember { mutableStateOf(selected.toHSB()) }
    val alpha = remember { mutableStateOf(1f) }

    // only update the HSB values from the outside Color if it is different from what the user selected to prevent lost data
    // i.e. if the color is Black, Color.toHSB will make the hue 0, when the user could have chosen any hue
    if (selected != oldHSB.value.toColor() || selected.alpha != alpha.value) {
        val tempHSB = selected.toHSB()
        alpha.value = selected.alpha
        oldHSB.value = tempHSB
    }

    Column(modifier, verticalArrangement = Arrangement.spacedBy(32.dp)) {
        Text(text = "Pick Color".toUpperCase(), style = MaterialTheme.typography.subtitle1)
        Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
            val rowHeight = remember { mutableStateOf(0) } // track height of SaturationLightnessPicker and give it to HuePicker since fillMaxHeight doesn't work as row has Constraints.Infinite so fillMax doesn't work
            SaturationBrightnessPicker(
                oldHSB.value.hue, oldHSB.value.saturation, oldHSB.value.brightness,
                modifier = Modifier.weight(1f).aspectRatio(1f).onGloballyPositioned { rowHeight.value = it.size.height }
            ) { newSaturation, newBrightness ->
                oldHSB.value = HSB(oldHSB.value.hue, newSaturation, newBrightness)
                onSelect.invoke(oldHSB.value.toColor().copy(alpha = alpha.value))
            }
            HuePicker(colors = hues, hue = oldHSB.value.hue, modifier = Modifier.height(with(LocalDensity.current) { rowHeight.value.toDp() })) { newHue ->
                oldHSB.value = HSB(newHue, oldHSB.value.saturation, oldHSB.value.brightness)
                onSelect.invoke(oldHSB.value.toColor().copy(alpha = alpha.value))
            }
        }
        ColorPickerTextField(selected = oldHSB.value.toColor()) {
            onSelect.invoke(it)
        }
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(text = "Opacity".toUpperCase(), style = MaterialTheme.typography.subtitle1)
            OpacityPicker(color = oldHSB.value.toColor().copy(alpha = 1f), alpha = alpha.value) { newAlpha ->
                alpha.value = newAlpha
                onSelect.invoke(oldHSB.value.toColor().copy(alpha = newAlpha))
            }
        }
    }
}

