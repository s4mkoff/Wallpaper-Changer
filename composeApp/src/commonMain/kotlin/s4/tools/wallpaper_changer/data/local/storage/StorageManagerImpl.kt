package s4.tools.wallpaper_changer.data.local.storage

import s4.tools.wallpaper_changer.domain.local.os.FilesManager
import s4.tools.wallpaper_changer.domain.local.storage.StorageManager
import s4.tools.wallpaper_changer.domain.models.wallpaper.WallpaperDetails
import java.io.File

class StorageManagerImpl(
    private val filesManager: FilesManager
): StorageManager {

    override fun getSavedWallpaperDetails(): List<WallpaperDetails> {
        TODO("Not yet implemented")
    }

    override fun getApiRequest(): String {
        TODO("Not yet implemented")
    }

    override fun loadAppSettings(): String? {
        val settingsDirectory = File(filesManager.directory, SETTINGS_FOLDER_NAME)
        if (!settingsDirectory.exists()) settingsDirectory.mkdir()
        val appSettingsFile = File(settingsDirectory, fileNameJson(GENERAL_SETTINGS_FILE_NAME))
        return if (appSettingsFile.exists()) appSettingsFile.readText() else null
    }

    override fun saveSettings(settings: String) {
        val settingsDirectory = File(filesManager.directory, SETTINGS_FOLDER_NAME)
        if (!settingsDirectory.exists()) settingsDirectory.mkdir()
        val appSettingsFile = File(settingsDirectory, fileNameJson(GENERAL_SETTINGS_FILE_NAME))
        appSettingsFile.writeText(settings)
    }

    override fun saveApiSettings(apiName: String, apiSettings: String) {
        val settingsDirectory = File(filesManager.directory, SETTINGS_FOLDER_NAME)
        if (!settingsDirectory.exists()) settingsDirectory.mkdir()
        val apiSettingsFile = File(settingsDirectory, fileNameJson(apiName))
        apiSettingsFile.writeText(apiSettings)
    }

    override fun loadApiSettings(apiName: String): String? {
        val settingsDirectory = File(filesManager.directory, SETTINGS_FOLDER_NAME)
        if (!settingsDirectory.exists()) settingsDirectory.mkdir()
        val apiSettingsFile = File(settingsDirectory, fileNameJson(apiName))
        return if (apiSettingsFile.exists()) apiSettingsFile.readText() else null
    }

    companion object {
        const val SETTINGS_FOLDER_NAME = "settings"
        const val GENERAL_SETTINGS_FILE_NAME = "general_settings"

        fun fileNameJson(fileName: String): String {
            return "$fileName.json"
        }
    }
}