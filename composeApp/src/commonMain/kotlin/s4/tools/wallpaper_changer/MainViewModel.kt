package s4.tools.wallpaper_changer

import androidx.lifecycle.ViewModel
import kotlinx.serialization.json.Json
import s4.tools.wallpaper_changer.data.FileNames
import s4.tools.wallpaper_changer.data.FilesOperation
import s4.tools.wallpaper_changer.data.MainFun
import s4.tools.wallpaper_changer.data.api.GeneralApi
import s4.tools.wallpaper_changer.data.api.wallhaven.WallhavenApi
import s4.tools.wallpaper_changer.data.api.wallhaven.WallhavenSettings
import java.io.File

class MainViewModel: ViewModel() {

    val workDir = getFilesDirectory()
    fun changeWallpaper() {
        MainFun(workDir).invoke(api.buildLink())
    }

    val api = WallhavenApi()

    fun saveApiParams() {
        val json = Json { prettyPrint = true }
        val text = json.encodeToString(api.toSettings())
        val settingsFile = File(workDir+FileNames.SETTINGS_FILE_NAME)
        settingsFile.writeText(text)
        println("Saving api params: ${settingsFile.absolutePath}")
    }

    fun loadApiParams() {
        val settingsFile = File(workDir+FileNames.SETTINGS_FILE_NAME)
        if (!settingsFile.exists()) return
        val json = Json { ignoreUnknownKeys = true }
        val settings = json.decodeFromString<WallhavenSettings>(settingsFile.readText())
        api.applySettings(settings)
        println("Loading api params: ${settingsFile.absolutePath}")
    }

}