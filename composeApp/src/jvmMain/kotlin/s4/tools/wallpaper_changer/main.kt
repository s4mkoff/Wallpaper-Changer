package s4.tools.wallpaper_changer

import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import kotlinx.coroutines.runBlocking
import s4.tools.wallpaper_changer.data.MainFun
import s4.tools.wallpaper_changer.data.api.wallhaven.WallhavenApi
import s4.tools.wallpaper_changer.data.storage.WallpaperStorage

const val SERVICE_ELEMENT = "-service"

fun main(
    args: Array<String>
) {
    if (args.contains(SERVICE_ELEMENT)) {
        runBlocking {
            val workDir = getFilesDirectory()
            val api = WallhavenApi()
            WallpaperStorage.loadApiParams(
                workDir
            ) { settings -> api.applySettings(settings) }
            MainFun(getFilesDirectory()).invoke(api.buildLink()).join()
        }
    } else {
        application {
            Window(
                state = rememberWindowState().also {
                    it.size = DpSize(360.dp, 640.dp)
                    it.placement = WindowPlacement.Floating
                },
                onCloseRequest = ::exitApplication,
                resizable = false,
                title = "Wallpaper Changer",
            ) {
                App()
            }
        }
    }
}