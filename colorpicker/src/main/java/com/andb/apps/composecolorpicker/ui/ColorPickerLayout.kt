package com.andb.apps.composecolorpicker.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.material.Text
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.andb.apps.composecolorpicker.data.HSB

import com.andb.apps.composecolorpicker.data.toColor
import com.andb.apps.composecolorpicker.data.toHSB

val hues: List<Color> = (0..360).map { HSB(it / 360f, 1f, 1f).toColor() }

/**
 * Example color picker that allows the user to select a hue, saturation, brightness, and opacity value. Demonstrates [SaturationBrightnessPicker], [HueSlider], [OpacityPicker], and [ColorPickerTextField] all working together to pick a color.
 * @param selected The currently selected color
 * @param modifier The modifier to be applied to the ExpandedColorPicker
 * @param onSelect The callback function for when the user changes the color
 */
@Composable
fun ColorPickerLayout(selected: Color, modifier: Modifier = Modifier, onSelect: (color: Color) -> Unit) {
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
        Row(Modifier.height(IntrinsicSize.Min), horizontalArrangement = Arrangement.spacedBy(0.dp)) {
            SaturationBrightnessPicker(
                hue = oldHSB.value.hue,
                saturation = oldHSB.value.saturation,
                brightness = oldHSB.value.brightness,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(12.dp))
            ) { newSaturation, newBrightness ->
                oldHSB.value = HSB(oldHSB.value.hue, newSaturation, newBrightness)
                onSelect.invoke(oldHSB.value.toColor().copy(alpha = alpha.value))
            }
            Spacer(modifier = Modifier.width(32.dp))
            HueSlider(
                colors = hues,
                hue = oldHSB.value.hue,
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .width(32.dp)
                    .fillMaxHeight(),
                orientation = Orientation.Vertical
            ) { newHue ->
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

/**
 * An opacity picker that allows the user to select an alpha value from a slider
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

