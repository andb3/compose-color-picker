package com.andb.apps.composecolorpicker.util

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils

data class HSB(val hue: Float, val saturation: Float, val brightness: Float)

fun Color.toHSB(): HSB {
    val hsvOut = floatArrayOf(0f, 0f, 0f)
    android.graphics.Color.RGBToHSV((red * 255).toInt(), (green * 255).toInt(), (blue * 255).toInt(), hsvOut)
    return HSB(hsvOut[0]/360, hsvOut[1], hsvOut[2])
}

fun Color.toHSL(): Triple<Float, Float, Float> = this.toArgb().toHSL()

fun Int.toHSL(): Triple<Float, Float, Float> {
    val hslOut = floatArrayOf(0f, 0f, 0f)
    ColorUtils.colorToHSL(this, hslOut)
    return Triple(hslOut[0]/360, hslOut[1], hslOut[2])
}

fun HSB.toColor(): Color {
    val colorInt = android.graphics.Color.HSVToColor(floatArrayOf(hue * 360, saturation, brightness))
    println("hsv = $this, color = ${Color(colorInt)}")
    return Color(colorInt)
}
