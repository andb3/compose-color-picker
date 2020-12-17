object Versions {
    const val minSDK = 21
    const val targetSDK = 29
    const val compileSDK = 29
    const val kotlin = "1.4.21"
    const val compose = "1.0.0-alpha09"
}

object Dependencies {
    object Compose {
        const val layout = "androidx.compose.foundation:foundation-layout:${Versions.compose}"
        const val material = "androidx.compose.material:material:${Versions.compose}"
        const val icons = "androidx.compose.material:material-icons-extended:${Versions.compose}"
        const val tooling = "androidx.compose.ui:ui-tooling:${Versions.compose}"
        const val compiler = "androidx.compose.compiler:compiler:${Versions.compose}"
    }
}