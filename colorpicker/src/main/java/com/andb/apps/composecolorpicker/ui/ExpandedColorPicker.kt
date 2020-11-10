package com.andb.apps.composecolorpicker.ui

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.onGloballyPositioned
import androidx.compose.ui.platform.DensityAmbient
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

    val hue = remember { mutableStateOf(selected.toHSB().hue) }
    val saturation = remember { mutableStateOf(selected.toHSB().saturation) }
    val brightness = remember { mutableStateOf(selected.toHSB().brightness) }
    val alpha = remember { mutableStateOf(1f) }

    // only update the HSB values from the outside Color if it is different from what the user selected to prevent lost data
    // i.e. if the color is Black, Color.toHSB will make the hue 0, when the user could have chosen any hue
    if (selected != oldHSB.value.toColor()) {
        val tempHSB = selected.toHSB()
        hue.value = tempHSB.hue
        saturation.value = tempHSB.saturation
        brightness.value = tempHSB.brightness
        alpha.value = selected.alpha
        oldHSB.value = tempHSB
    }

    Column(modifier) {
        Text(text = "Pick Color".toUpperCase(), style = MaterialTheme.typography.subtitle1, modifier = Modifier.padding(bottom = 32.dp))
        Row {
            val rowHeight = remember { mutableStateOf(0) } // track height of SaturationLightnessPicker and give it to HuePicker since fillMaxHeight doesn't work as row has Constraints.Infinite so fillMax doesn't work
            SaturationBrightnessPicker(
                hue.value, saturation.value, brightness.value,
                modifier = Modifier.weight(1f).aspectRatio(1f).padding(end = 32.dp).onGloballyPositioned { rowHeight.value = it.size.height }
            ) { newSaturation, newBrightness ->
                saturation.value = newSaturation
                brightness.value = newBrightness
                oldHSB.value = HSB(hue.value, newSaturation, newBrightness)
                onSelect.invoke(oldHSB.value.toColor().copy(alpha = alpha.value))
            }
            HuePicker(colors = hues, hue = hue.value, modifier = Modifier.height(with(DensityAmbient.current) { rowHeight.value.toDp() })) { newHue ->
                hue.value = newHue
                oldHSB.value = HSB(newHue, saturation.value, brightness.value)
                onSelect.invoke(oldHSB.value.toColor().copy(alpha = alpha.value))
            }
        }
        Text(text = "Opacity".toUpperCase(), style = MaterialTheme.typography.subtitle1, modifier = Modifier.padding(vertical = 32.dp))
        OpacityPicker(color = HSB(hue.value, saturation.value, brightness.value).toColor().copy(alpha = 1f), alpha = alpha.value) { newAlpha ->
            alpha.value = newAlpha
            oldHSB.value = HSB(hue.value, saturation.value, brightness.value)
            onSelect.invoke(oldHSB.value.toColor().copy(alpha = newAlpha))
        }

/*        ColorPickerTextField(selected = hsb.toColor().copy(alpha = alpha), modifier = Modifier.padding(top = 32.dp)) {

        }*/
    }
}

