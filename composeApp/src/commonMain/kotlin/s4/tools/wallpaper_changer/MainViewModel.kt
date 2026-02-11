package s4.tools.wallpaper_changer

import androidx.lifecycle.ViewModel
import kotlinx.serialization.json.Json
import s4.tools.wallpaper_changer.data.FileNames
import s4.tools.wallpaper_changer.data.FilesOperation
import s4.tools.wallpaper_changer.data.MainFun
import s4.tools.wallpaper_changer.data.api.GeneralApi
import s4.tools.wallpaper_changer.data.api.wallhaven.WallhavenApi
import s4.tools.wallpaper_changer.data.api.wallhaven.WallhavenSettings
import s4.tools.wallpaper_changer.data.storage.WallpaperStorage
import java.io.File

class MainViewModel: ViewModel() {

    val workDir = getFilesDirectory()
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

    fun loadApiParams() {
        WallpaperStorage.loadApiParams(
            workDir
        ) { settings -> api.applySettings(settings) }
    }

}