package s4.tools.wallpaper_changer.domain.local.storage

import s4.tools.wallpaper_changer.domain.models.AppSettings
import s4.tools.wallpaper_changer.presentation.theme.Theme
import s4.tools.wallpaper_changer.domain.models.wallpaper.WallpaperDetails

interface StorageManager {

    fun getSavedWallpaperDetails(): List<WallpaperDetails>

    fun getApiRequest(): String

    fun saveApiSettings(apiName: String, apiSettings: String)

    fun loadApiSettings(apiName: String): String?

    fun loadAppSettings(): String?

    fun saveSettings(settings: String)

}