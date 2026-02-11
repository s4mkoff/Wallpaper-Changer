package s4.tools.wallpaper_changer

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "wallpaper_changer",
    ) {
        App()
    }
}