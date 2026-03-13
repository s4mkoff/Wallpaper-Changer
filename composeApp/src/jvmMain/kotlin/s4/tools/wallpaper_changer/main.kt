package s4.tools.wallpaper_changer

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import s4.tools.wallpaper_changer.data.local.AppManagers
import s4.tools.wallpaper_changer.data.remote.api.WallhavenApi
import s4.tools.wallpaper_changer.domain.models.AppSettings
import s4.tools.wallpaper_changer.domain.usecase.WallpaperUseCases

const val SERVICE_ELEMENT = "-service"

fun main(
    args: Array<String>
) {
    if (args.contains(SERVICE_ELEMENT)) {
        runBlocking {
            val useCases = WallpaperUseCases(
                api = WallhavenApi(),
            )
            val json = Json { prettyPrint = true }
            val appSettings = AppManagers.storageManager.loadAppSettings()?.let {
                json.decodeFromString<AppSettings>(it)
            } ?: AppSettings()
            useCases.randomWallpaper(appSettings.singleWallpaper)
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