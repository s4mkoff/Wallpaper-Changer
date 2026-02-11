package s4.tools.wallpaper_changer.data.storage

import kotlinx.serialization.json.Json
import s4.tools.wallpaper_changer.data.FileNames
import s4.tools.wallpaper_changer.data.api.wallhaven.WallhavenSettings
import s4.tools.wallpaper_changer.getFilesDirectory
import java.io.File

object WallpaperStorage {


    fun saveApiParams(
        workDir: String,
        apiSettings: WallhavenSettings
    ) {
        val json = Json { prettyPrint = true }
        val text = json.encodeToString(apiSettings)
        val settingsFile = File(workDir+FileNames.SETTINGS_FILE_NAME)
        settingsFile.writeText(text)
        println("Saving api params: ${settingsFile.absolutePath}")
    }

    fun loadApiParams(
        workDir: String,
        applyApiSettings: (WallhavenSettings) -> Unit
    ) {
        val settingsFile = File(workDir+FileNames.SETTINGS_FILE_NAME)
        if (!settingsFile.exists()) return
        val json = Json { ignoreUnknownKeys = true }
        val settings = json.decodeFromString<WallhavenSettings>(settingsFile.readText())
        applyApiSettings(settings)
        println("Loading api params: ${settingsFile.absolutePath}")
    }

}