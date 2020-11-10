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
import androidx.compose.ui.onPositioned
import androidx.compose.ui.platform.DensityAmbient
import androidx.compose.ui.unit.dp
import com.andb.apps.composecolorpicker.util.HSB

import com.andb.apps.composecolorpicker.util.toColor
import com.andb.apps.composecolorpicker.util.toHSB

private val hues: List<Color> = (0..360).map { HSB(it / 360f, 1f, 1f).toColor() }

@Composable
fun ExpandedColorPicker(_selected: Color, modifier: Modifier = Modifier, onSelect: (color: Color) -> Unit) {
    val oldHSB = remember { mutableStateOf(_selected.toHSB()) }
    val hue = remember { mutableStateOf(_selected.toHSB().hue) }
    val saturation = remember { mutableStateOf(_selected.toHSB().saturation) }
    val brightness = remember { mutableStateOf(_selected.toHSB().brightness) }
    val alpha = remember { mutableStateOf(1f) }
    val hsb = HSB(hue.value, saturation.value, brightness.value)

    if (_selected != oldHSB.value.toColor()) {
        val tempHSB = _selected.toHSB()
        hue.value = tempHSB.hue
        saturation.value = tempHSB.saturation
        brightness.value = tempHSB.brightness
        alpha.value = _selected.alpha
        oldHSB.value = tempHSB
    }

    println("hsb updated - hsb = $hsb")
    Column(modifier) {
        Text(text = "Pick Color".toUpperCase(), style = MaterialTheme.typography.subtitle1, modifier = Modifier.padding(bottom = 32.dp))
        Row {
            val rowHeight = remember { mutableStateOf(0) } // track height of SaturationLightnessPicker and give it to HuePicker since fillMaxHeight doesn't work as row has Constraints.Infinite so fillMax doesn't work
            SaturationBrightnessPicker(
                hue.value, saturation.value, brightness.value,
                modifier = Modifier.weight(1f).aspectRatio(1f).padding(end = 32.dp).onGloballyPositioned { rowHeight.value = it.size.height }
            ) { newSaturation, newBrightness ->
                println("changing saturation and brightness, current hsb = $hsb")
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

