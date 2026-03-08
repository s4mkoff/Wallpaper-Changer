package s4.tools.wallpaper_changer.domain.remote

import s4.tools.wallpaper_changer.domain.models.wallpaper.WallpaperDetails

interface WallpaperApi {

    val apiName: String

    fun buildLink(): String

    suspend fun getWallpaperDetails(): List<WallpaperDetails>

    fun saveApiSettings()
    fun loadApiSettings()

    suspend fun searchWallpapers(url: String): List<WallpaperResponse>

}