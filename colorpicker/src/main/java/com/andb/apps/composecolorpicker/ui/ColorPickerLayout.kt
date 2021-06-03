package com.andb.apps.composecolorpicker.ui

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.material.Text
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.andb.apps.composecolorpicker.data.HSB

import com.andb.apps.composecolorpicker.data.toColor
import com.andb.apps.composecolorpicker.data.toHSB

val hues: List<Color> = (0..360).map { HSB(it / 360f, 1f, 1f).toColor() }

/**
 * Example color picker that allows the user to select a hue, saturation, brightness, and opacity value. Demonstrates [SaturationBrightnessPicker], [HueSlider], [OpacityPicker], and [ColorPickerTextField] all working together to pick a color.
 * Includes code to maintain the HSB state when an RGB color loses data (i.e. #000000 loses the hue and saturation value)
 * @param selected The currently selected color
 * @param modifier The modifier to be applied to the [ColorPickerLayout]
 * @param onSelect The callback function for when the user changes the color
 */

@Composable
fun ColorPickerLayout(selected: Color, modifier: Modifier = Modifier, onSelect: (color: Color) -> Unit) {
    val oldHSB = remember { mutableStateOf(selected.toHSB()) }

    // only update the HSB values from the outside Color if it is different from what the user selected to prevent lost data
    // i.e. if the color is Black, Color.toHSB will make the hue 0, when the user could have chosen any hue
    if (selected.copy(alpha = 1f) != oldHSB.value.toColor()) {
        val tempHSB = selected.toHSB()
        oldHSB.value = tempHSB
    }

    ColorPickerLayout(oldHSB.value, selected.alpha, modifier) { hsb, alpha ->
        oldHSB.value = hsb
        onSelect.invoke(hsb.toColor().copy(alpha = alpha))
    }
}

/**
 * Example color picker that allows the user to select a hue, saturation, brightness, and opacity value. Demonstrates [SaturationBrightnessPicker], [HueSlider], [OpacityPicker], and [ColorPickerTextField] all working together to pick a color.
 * @param selectedHSB The currently selected color in [HSB] format
 * @param modifier The modifier to be applied to the [ColorPickerLayout]
 * @param onSelect The callback function for when the user changes the color
 */
@Composable
fun ColorPickerLayout(selectedHSB: HSB, selectedAlpha: Float, modifier: Modifier = Modifier, onSelect: (color: HSB, alpha: Float) -> Unit) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(32.dp)) {
        Text(text = "Pick Color".uppercase(), style = MaterialTheme.typography.subtitle1)
        Row(Modifier.height(IntrinsicSize.Min)) {
            SaturationBrightnessPicker(
                hue = selectedHSB.hue,
                saturation = selectedHSB.saturation,
                brightness = selectedHSB.brightness,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(12.dp))
            ) { newSaturation, newBrightness ->
                onSelect.invoke(HSB(selectedHSB.hue, newSaturation, newBrightness), selectedAlpha)
            }
            Spacer(modifier = Modifier.width(32.dp))
            HueSlider(
                colors = hues,
                hue = selectedHSB.hue,
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .width(32.dp)
                    .fillMaxHeight(),
                orientation = Orientation.Vertical
            ) { newHue ->
                onSelect.invoke(HSB(newHue, selectedHSB.saturation, selectedHSB.brightness), selectedAlpha)
            }
        }
        ColorPickerTextField(selected = selectedHSB.toColor()) {
            onSelect.invoke(it.toHSB(), selectedAlpha)
        }
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(text = "Opacity".uppercase(), style = MaterialTheme.typography.subtitle1)
            OpacityPicker(color = selectedHSB.toColor().copy(alpha = 1f), alpha = selectedAlpha) { newAlpha ->
                onSelect.invoke(selectedHSB, newAlpha)
            }
        }
    }
}

/**
 * An opacity picker that allows the user to select an alpha value from a slider and a text field
 * @param color The color shown on the picker (should have alpha of 1f)
 * @param alpha The current alpha
 * @param modifier The modifier to be applied to the OpacityPicker
 * @param onSelect The callback function for when the user changes the opacity
 */
@Composable
fun OpacityPicker(color: Color, alpha: Float, modifier: Modifier = Modifier, onSelect: (alpha: Float) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        OpacitySlider(color = color, alpha = alpha, modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .height(32.dp)
            .fillMaxWidth()
            .weight(1f), onSelect = onSelect)
        OpacityTextField(alpha = alpha, onSelect = onSelect, modifier = Modifier.width(48.dp))
    }
}