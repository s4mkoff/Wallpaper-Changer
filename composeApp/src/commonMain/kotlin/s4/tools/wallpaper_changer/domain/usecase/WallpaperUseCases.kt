package s4.tools.wallpaper_changer.domain.usecase

import s4.tools.wallpaper_changer.data.local.AppManagers
import s4.tools.wallpaper_changer.domain.models.CurrentWallpaperImage
import s4.tools.wallpaper_changer.domain.models.wallpaper.Wallpaper
import s4.tools.wallpaper_changer.domain.remote.WallpaperApi
import s4.tools.wallpaper_changer.domain.remote.WallpaperResponse
import s4.tools.wallpaper_changer.toBitmap
import java.io.File

class WallpaperUseCases(
    val api: WallpaperApi
) {

    suspend fun randomWallpaper(singleWallpaper: Boolean) {
        val link = api.buildLink()
        val wallpaper = api.searchWallpapers(link).random()
        val wallpaperFile = findFileOrCreate(wallpaper)
        if (singleWallpaper) clearWallpapers()
        AppManagers.wallpaperNetwork.downloadWallpaper(wallpaper.path, wallpaperFile)
        AppManagers.wallpaperChanger.changeWallpaper(wallpaperFile)
        api.saveApiSettings()
    }

    suspend fun getWallpaperList(): List<WallpaperResponse> {
        val link = api.buildLink()
        return api.searchWallpapers(link)
    }

    suspend fun changeWallpaperFromApi(wallpaperResponse: WallpaperResponse, singleWallpaper: Boolean) {
        val wallpaperFile = findFileOrCreate(wallpaperResponse)
        if (singleWallpaper) clearWallpapers()
        AppManagers.wallpaperNetwork.downloadWallpaper(wallpaperResponse.path, wallpaperFile)
        AppManagers.wallpaperChanger.changeWallpaper(wallpaperFile)
        api.saveApiSettings()
    }

    suspend fun findFileOrCreate(wallpaper: WallpaperResponse): File {
        val localWallpaperList = loadWallpapersHistory()
        var wallpaperFile: File? = null
        localWallpaperList.find {
            val condition = it.id == wallpaper.id
            if (condition) {
                wallpaperFile = it.file
            }
            condition
        }
        return wallpaperFile ?: AppManagers.filesManager.createWallpaperFile(
            "${api.apiName}_${wallpaper.id}_${System.currentTimeMillis()}",
            wallpaper.extension
        )
    }

    suspend fun changeWallpaperFromStorage(wallpaper: Wallpaper) {
        AppManagers.wallpaperChanger.changeWallpaper(wallpaper.file)
        api.saveApiSettings()
    }

    suspend fun loadWallpapersHistory(): List<Wallpaper> {
        val localWallpapers = AppManagers.filesManager.lookForWallpapers() ?: emptyList()
//        val remoteWallpapers = App
        return localWallpapers.sortedByDescending { it.time }
    }

    suspend fun clearWallpapers() {
        val wallpaperList = AppManagers.filesManager.lookForWallpapers()
        wallpaperList?.let { AppManagers.filesManager.removeWallpapers(it) }
    }

    suspend fun loadLastWallpaperImage(): CurrentWallpaperImage {
        return try {
            val wallpaperList = AppManagers.filesManager.lookForWallpapers()
            wallpaperList?.let { list ->
                print("Looked for wallpapers")
                val lastDownloadedWallpaper = list.maxBy { it.time ?: 0 }
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