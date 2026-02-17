package s4.tools.wallpaper_changer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import s4.tools.wallpaper_changer.data.MainFun
import s4.tools.wallpaper_changer.data.api.wallhaven.WallhavenApi
import s4.tools.wallpaper_changer.data.storage.WallpaperStorage

class MainViewModel: ViewModel() {

    val workDir = getFilesDirectory()

    var boxColor: Color? by mutableStateOf(null)
    fun changeWallpaper(
        showSnackbar: (String) -> Unit
    ) {
        val mainFun = MainFun(workDir)
        mainFun.showSnackbar = showSnackbar
        mainFun.invoke(api.buildLink())
    }

    val api = WallhavenApi()

    fun saveApiParams() {
        WallpaperStorage.saveApiParams(
            workDir,
            api.toSettings()
        )
    }

    fun toComposeColor() {
        try {
            val hex = api.color.removePrefix("#")
            val value = when (hex.length) {
                6 -> ("FF" + hex).toLong(16) // додаємо alpha
                8 -> hex.toLong(16)
                else -> error("Invalid color: $this")
            }
            boxColor = Color(value)
        } catch (_: Exception) {
            boxColor = null
        }
    }

    fun loadApiParams() {
        WallpaperStorage.loadApiParams(
            workDir
        ) { settings -> api.applySettings(settings) }
    }

}