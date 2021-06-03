object Versions {
    const val minSDK = 21
    const val targetSDK = 29
    const val compileSDK = 29
    const val kotlin = "1.5.10"
    const val compose = "1.0.0-beta08"
    const val activity = "1.3.0-alpha08"
}

object Dependencies {
    object Activity {
        const val compose = "androidx.activity:activity-compose:${Versions.activity}"
    }
    object Compose {
        const val layout = "androidx.compose.foundation:foundation-layout:${Versions.compose}"
        const val material = "androidx.compose.material:material:${Versions.compose}"
        const val icons = "androidx.compose.material:material-icons-extended:${Versions.compose}"
        const val tooling = "androidx.compose.ui:ui-tooling:${Versions.compose}"
        const val compiler = "androidx.compose.compiler:compiler:${Versions.compose}"
    }
}