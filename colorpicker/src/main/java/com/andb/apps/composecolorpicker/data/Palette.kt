package com.andb.apps.composecolorpicker.data

import androidx.compose.ui.graphics.Color

public data class Palette (val values: List<String>, val colors: List<List<Color>>)

public val primaryValues: List<String> = listOf("50", "100", "200", "300", "400", "500", "600", "700", "800", "900")
public val accentValues: List<String> = listOf("100", "200", "400", "700")
public val primaryColors: List<List<Color>> = listOf(
    listOf("ffebee", "ffcdd2", "ef9a9a", "e57373", "ef5350", "f44336", "e53935", "d32f2f", "c62828", "b71c1c"), // red
    listOf("fce4ec", "f8bbd0", "f48fb1", "f06292", "ec407a", "e91e63", "d81b60", "c2185b", "ad1457", "880e4f"), // pink
    listOf("f3e5f5", "e1bee7", "ce93d8", "ba68c8", "ab47bc", "9c27b0", "8e24aa", "7b1fa2", "6a1b9a", "4a148c"), // purple
    listOf("ede7f6", "d1c4e9", "b39ddb", "9575cd", "7e57c2", "673ab7", "5e35b1", "512da8", "4527a0", "311b92"), // deep purple
    listOf("E8EAF6", "C5CAE9", "9FA8DA", "7986CB", "5C6BC0", "3F51B5", "3949AB", "303F9F", "283593", "1A237E"), // indigo
    listOf("E3F2FD", "BBDEFB", "90CAF9", "64B5F6", "42A5F5", "2196F3", "1E88E5", "1976D2", "1565C0", "0D47A1"), // blue
    listOf("E1F5FE", "B3E5FC", "81D4FA", "4FC3F7", "29B6F6", "03A9F4", "039BE5", "0288D1", "0277BD", "01579B"), // light blue
    listOf("E0F7FA", "B2EBF2", "80DEEA", "4DD0E1", "26C6DA", "00BCD4", "00ACC1", "0097A7", "00838F", "006064"), // cyan
    listOf("E0F2F1", "B2DFDB", "80CBC4", "4DB6AC", "26A69A", "009688", "00897B", "00796B", "00695C", "004D40"), // teal
    listOf("E8F5E9", "C8E6C9", "A5D6A7", "81C784", "66BB6A", "4CAF50", "43A047", "388E3C", "2E7D32", "1B5E20"), // green
    listOf("F1F8E9", "DCEDC8", "C5E1A5", "AED581", "9CCC65", "8BC34A", "7CB342", "689F38", "558B2F", "33691E"), // light green
    listOf("F9FBE7", "F0F4C3", "E6EE9C", "DCE775", "D4E157", "CDDC39", "C0CA33", "AFB42B", "9E9D24", "827717"), // lime
    listOf("FFFDE7", "FFF9C4", "FFF59D", "FFF176", "FFEE58", "FFEB3B", "FDD835", "FBC02D", "F9A825", "F57F17"), // yellow
    listOf("FFF8E1", "FFECB3", "FFE082", "FFD54F", "FFCA28", "FFC107", "FFB300", "FFA000", "FF8F00", "FF6F00"), // amber
    listOf("FFF3E0", "FFE0B2", "FFCC80", "FFB74D", "FFA726", "FF9800", "FB8C00", "F57C00", "EF6C00", "E65100"), // orange
    listOf("FBE9E7", "FFCCBC", "FFAB91", "FF8A65", "FF7043", "FF5722", "F4511E", "E64A19", "D84315", "BF360C"), // deep orange
    listOf("EFEBE9", "D7CCC8", "BCAAA4", "A1887F", "8D6E63", "795548", "6D4C41", "5D4037", "4E342E", "3E2723"), // brown
    listOf("FAFAFA", "F5F5F5", "EEEEEE", "E0E0E0", "BDBDBD", "9E9E9E", "757575", "616161", "424242", "212121"), // gray
    listOf("ECEFF1", "CFD8DC", "B0BEC5", "90A4AE", "78909C", "607D8B", "546E7A", "455A64", "37474F", "263238"), // blue gray
).map { hue -> hue.map { Color(android.graphics.Color.parseColor("#ff$it")) } }

public val accentColors: List<List<Color>> = listOf(
    listOf("ff8a80", "ff5252", "ff1744", "d50000"), // red
    listOf("ff80ab", "ff4081", "f50057", "c51162"), // pink
    listOf("ea80fc", "e040fb", "d500f9", "aa00ff"), // purple
    listOf("b388ff", "7c4dff", "651fff", "6200ea"), // deep purple
    listOf("8C9EFF", "536DFE", "3D5AFE", "304FFE"), // indigo
    listOf("82B1FF", "448AFF", "2979FF", "2962FF"), // blue
    listOf("80D8FF", "40C4FF", "00B0FF", "0091EA"), // light blue
    listOf("84FFFF", "18FFFF", "00E5FF", "00B8D4"), // cyan
    listOf("A7FFEB", "64FFDA", "1DE9B6", "00BFA5"), // teal
    listOf("B9F6CA", "69F0AE", "00E676", "00C853"), // green
    listOf("CCFF90", "B2FF59", "76FF03", "64DD17"), // light green
    listOf("F4FF81", "EEFF41", "C6FF00", "AEEA00"), // lime
    listOf("FFFF8D", "FFFF00", "FFEA00", "FFD600"), // yellow
    listOf("FFE57F", "FFD740", "FFC400", "FFAB00"), // amber
    listOf("FFD180", "FFAB40", "FF9100", "FF6D00"), // orange
    listOf("FF9E80", "FF6E40", "FF3D00", "DD2C00"), // deep orange
).map { hue -> hue.map { Color(android.graphics.Color.parseColor("#ff$it")) } }

public val primaryPalette = Palette(primaryValues, primaryColors)
public val accentPalette = Palette(accentValues, accentColors)