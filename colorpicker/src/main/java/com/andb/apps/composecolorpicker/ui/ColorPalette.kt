package com.andb.apps.composecolorpicker.ui

import androidx.compose.material.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.andb.apps.composecolorpicker.data.Palette
import com.andb.apps.composecolorpicker.data.accentPalette
import com.andb.apps.composecolorpicker.data.primaryPalette

/**
 * A color picker that shows options from the Material Design primary and accent color palettes
 * @param selected The currently selected color
 * @param modifier The modifier that will be applied to the ColorPalette
 * @param onSelect The callback function for when the user changes the color
 */
@Composable
fun MaterialPalette(selected: Color, modifier: Modifier = Modifier, onSelect: (Color) -> Unit) {
    Column(modifier) {
        Text(text = "Material Palette".toUpperCase(), style = MaterialTheme.typography.subtitle1, modifier = Modifier.padding(bottom = 32.dp))
        Row {
            ColorPalette(
                palette = primaryPalette,
                name = "Primary",
                selected = selected,
                onSelect = onSelect,
                modifier = Modifier.weight(10f)
            )
            Spacer(modifier = Modifier.width(4.dp))
            ColorPalette(
                palette = accentPalette,
                name = "Accent",
                selected = selected,
                onSelect = onSelect,
                modifier = Modifier.weight(4f)
            )
        }
    }
}

/**
 * A color picker that shows options from a given [Palette]
 * @param palette The palette that will be shown
 * @param selected The currently selected color
 * @param modifier The modifier that will be applied to the ColorPalette
 * @param onSelect The callback function for when the user changes the color
 */
@Composable
fun ColorPalette(palette: Palette, name: String, selected: Color, modifier: Modifier = Modifier, onSelect: (Color) -> Unit) {
    Column(modifier) {
        Text(text = name, style = MaterialTheme.typography.subtitle2, fontWeight = FontWeight.Bold)
        Box(
            modifier = Modifier
                .padding(vertical = 2.dp)
                .background(MaterialTheme.colors.onBackground.copy(alpha = 0.05f))
                .height(1.dp)
                .fillMaxWidth()
        )
        Row(Modifier.padding(bottom = 4.dp), verticalAlignment = Alignment.Bottom) {
            palette.values.forEach {
                Text(text = it, modifier = Modifier.weight(1f), style = MaterialTheme.typography.overline)
            }
        }
        palette.colors.forEach { huePalette ->
            Row {
                huePalette.forEach { color ->
                    Box(
                        modifier = Modifier
                                then (if (color == selected) Modifier.border(2.dp, MaterialTheme.colors.background).shadow(2.dp) else Modifier)
                            .background(color)
                            .clickable { onSelect.invoke(color) }
                            .aspectRatio(1f)
                            .weight(1f)
                    )
                }
            }
        }
    }
}