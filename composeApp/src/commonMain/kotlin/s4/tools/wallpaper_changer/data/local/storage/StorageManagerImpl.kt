package s4.tools.wallpaper_changer.data.local.storage

import s4.tools.wallpaper_changer.data.utils.JsonHelper
import s4.tools.wallpaper_changer.domain.local.os.FilesManager
import s4.tools.wallpaper_changer.domain.local.storage.StorageManager
import s4.tools.wallpaper_changer.domain.models.storage.WallpaperHistoryEntry
import s4.tools.wallpaper_changer.domain.models.storage.WallpaperHistoryList
import s4.tools.wallpaper_changer.domain.remote.WallpaperResponse
import java.io.File

class StorageManagerImpl(
    private val filesManager: FilesManager
): StorageManager {

    override fun loadWallpaperHistory(): String? {
        val settingsDirectory = File(filesManager.directory, SETTINGS_FOLDER_NAME)
        if (!settingsDirectory.exists()) settingsDirectory.mkdir()
        val historyListFile = File(settingsDirectory, fileNameJson(HISTORY_LIST_FILE_NAME))
        return if (historyListFile.exists()) historyListFile.readText() else null
    }

    override fun saveWallpaperHistory(history: String) {
        val settingsDirectory = File(filesManager.directory, SETTINGS_FOLDER_NAME)
        if (!settingsDirectory.exists()) settingsDirectory.mkdir()
        val historyListFile = File(settingsDirectory, fileNameJson(HISTORY_LIST_FILE_NAME))
        historyListFile.writeText(history)
    }

    override fun getApiRequest(): String {
        TODO("Not yet implemented")
    }

    override fun addEntryToWallpaperHistory(wallpaperResponse: WallpaperResponse) {
        println("In entry to wallpaper history")
        val history = loadWallpaperHistory()?.let { JsonHelper.fromJsonStringToClass(it, WallpaperHistoryList.serializer()) } ?: WallpaperHistoryList(emptyList())
        val historyList = history.list.toMutableList()
        val existingEntry = historyList.find { it.apiName==wallpaperResponse.apiName&&it.id==wallpaperResponse.id }
        println("Adding new entry with id: ${wallpaperResponse.id} and apiName: ${wallpaperResponse.apiName}")
        historyList.forEach {
            println("Existing history entry: ${it.id} ${it.apiName}")
        }
        existingEntry?.let {
            historyList.remove(it)
            historyList.add(it)
        } ?: historyList.add(
            WallpaperHistoryEntry(
                id = wallpaperResponse.id,
                apiName = wallpaperResponse.apiName,
                thumbUrl = wallpaperResponse.thumbUrl,
                pathUrl = wallpaperResponse.path
            )
        )
        historyList.forEach {
            println("saving id: ${it.id}")
        }
        saveWallpaperHistory(JsonHelper.fromClassToJsonString(WallpaperHistoryList(historyList), WallpaperHistoryList.serializer()))
    }

    override fun removeEntryFromWallpaperHistory(wallpaperHistoryEntry: WallpaperHistoryEntry) {
        val history = loadWallpaperHistory()?.let { JsonHelper.fromJsonStringToClass(it, WallpaperHistoryList.serializer()) } ?: WallpaperHistoryList(emptyList())
        val historyList = history.list.toMutableList()
        historyList.remove(wallpaperHistoryEntry)
        saveWallpaperHistory(JsonHelper.fromClassToJsonString(WallpaperHistoryList(historyList), WallpaperHistoryList.serializer()))
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
        const val HISTORY_LIST_FILE_NAME = "history"

        fun fileNameJson(fileName: String): String {
            return "$fileName.json"
        }
    }
}