package s4.tools.wallpaper_changer

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
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
                onCloseRequest = ::exitApplication,
                title = "wallpaper_changer",
            ) {
                App()
            }
        }
    }
}