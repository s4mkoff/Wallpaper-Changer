package s4.tools.wallpaper_changer.domain.usecase

import s4.tools.wallpaper_changer.data.local.AppManagers
import s4.tools.wallpaper_changer.domain.models.CurrentWallpaperImage
import s4.tools.wallpaper_changer.domain.remote.WallpaperApi
import s4.tools.wallpaper_changer.domain.remote.WallpaperResponse
import s4.tools.wallpaper_changer.toBitmap

class WallpaperApiUseCases(
    val api: WallpaperApi
) {

    suspend fun randomWallpaper() {
        val link = api.buildLink()
        val wallpaper = api.searchWallpapers(link).random()
        val wallpaperFile = AppManagers.filesManager.createWallpaperFile(System.currentTimeMillis().toString(), wallpaper.extension)
        AppManagers.wallpaperNetwork.downloadWallpaper(wallpaper.path, wallpaperFile)
        AppManagers.wallpaperChanger.changeWallpaper(wallpaperFile)
        api.saveApiSettings()
    }

    suspend fun getWallpaperList(): List<WallpaperResponse> {
        val link = api.buildLink()
        return api.searchWallpapers(link)
    }

    suspend fun changeWallpaperFromApi(wallpaperResponse: WallpaperResponse) {
        val wallpaperFile = AppManagers.filesManager.createWallpaperFile(System.currentTimeMillis().toString(), wallpaperResponse.extension)
        AppManagers.wallpaperNetwork.downloadWallpaper(wallpaperResponse.path, wallpaperFile)
        AppManagers.wallpaperChanger.changeWallpaper(wallpaperFile)
        api.saveApiSettings()
    }

    suspend fun clearWallpapers() {
        val wallpaperList = AppManagers.filesManager.lookForWallpapers()
        wallpaperList?.let{ AppManagers.filesManager.removeWallpapers(it) }
    }

    suspend fun loadLastWallpaperImage(): CurrentWallpaperImage {
        return try {
            val wallpaperList = AppManagers.filesManager.lookForWallpapers()
            wallpaperList?.let { list ->
                print("Looked for wallpapers")
                val lastDownloadedWallpaper = list.maxBy { it.trimNameToIndex() }
                print("Last wallpaper")
                lastDownloadedWallpaper.file.toBitmap()?.let { CurrentWallpaperImage.Success(it) }
                    ?: CurrentWallpaperImage.Error()
            } ?: CurrentWallpaperImage.NotSet()
        } catch (e: Exception) {
            print("Exception: ${e.message}")
            e.message?.let { CurrentWallpaperImage.Error(it) } ?: CurrentWallpaperImage.Error()
        }
    }

}