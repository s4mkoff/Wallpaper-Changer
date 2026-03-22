package s4.tools.wallpaper_changer.domain.local.storage

import s4.tools.wallpaper_changer.domain.models.storage.WallpaperHistoryEntry
import s4.tools.wallpaper_changer.domain.remote.WallpaperResponse

interface StorageManager {

    fun loadWallpaperHistory(): String?

    fun saveWallpaperHistory(history: String)

    fun addEntryToWallpaperHistory(wallpaperResponse: WallpaperResponse)

    fun removeEntryFromWallpaperHistory(wallpaperHistoryEntry: WallpaperHistoryEntry)

    fun getApiRequest(): String

    fun saveApiSettings(apiName: String, apiSettings: String)

    fun loadApiSettings(apiName: String): String?

    fun loadAppSettings(): String?

    fun saveSettings(settings: String)

}