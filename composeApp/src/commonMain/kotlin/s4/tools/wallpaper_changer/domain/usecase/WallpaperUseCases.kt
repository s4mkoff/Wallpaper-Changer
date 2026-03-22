package s4.tools.wallpaper_changer.domain.usecase

import s4.tools.wallpaper_changer.data.local.AppManagers
import s4.tools.wallpaper_changer.data.utils.JsonHelper
import s4.tools.wallpaper_changer.domain.models.LocalLoadingWallpaperImage
import s4.tools.wallpaper_changer.domain.models.storage.WallpaperHistoryEntry
import s4.tools.wallpaper_changer.domain.models.storage.WallpaperHistoryList
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
        AppManagers.storageManager.addEntryToWallpaperHistory(wallpaper)
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
        AppManagers.storageManager.addEntryToWallpaperHistory(wallpaperResponse)
        api.saveApiSettings()
    }

    suspend fun removeWallpaperFromHistory(wallpaper: WallpaperHistoryEntry) {
        AppManagers.storageManager.removeEntryFromWallpaperHistory(wallpaper)
    }

    suspend fun findFileOrCreate(wallpaper: WallpaperResponse): File {
        val localWallpaperList = AppManagers.filesManager.lookForWallpapers() ?: emptyList()
        var wallpaperFile: File? = null
        localWallpaperList.find {
            val condition = it.id == wallpaper.id && it.apiName == wallpaper.apiName
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

    suspend fun loadWallpapersHistory(): List<Pair<WallpaperHistoryEntry, Wallpaper?>> {
        val history = AppManagers.storageManager.loadWallpaperHistory()?.let { JsonHelper.fromJsonStringToClass(it, WallpaperHistoryList.serializer()) }?.list ?: emptyList()
        val localWallpapers = AppManagers.filesManager.lookForWallpapers() ?: emptyList()
        if (history.isEmpty()) return history.map { it to null }
        return history.map { historyEntry ->
            val localWallpaper = localWallpapers.find { it.id==historyEntry.id && it.apiName==historyEntry.apiName }
            localWallpaper?.let { historyEntry to it } ?: (historyEntry to null)
        }.sortedByDescending {
            it.second?.time
        }
    }

    suspend fun clearWallpapers() {
        val wallpaperList = AppManagers.filesManager.lookForWallpapers()
        wallpaperList?.let { AppManagers.filesManager.removeWallpapers(it) }
    }

    suspend fun loadLastWallpaperImage(): LocalLoadingWallpaperImage {
        return try {
            val wallpaperList = AppManagers.filesManager.lookForWallpapers()
            wallpaperList?.let { list ->
                print("Looked for wallpapers")
                val lastDownloadedWallpaper = list.maxBy { it.time ?: 0 }
                print("Last wallpaper")
                lastDownloadedWallpaper.file.toBitmap()?.let { LocalLoadingWallpaperImage.Success(it) }
                    ?: LocalLoadingWallpaperImage.Error()
            } ?: LocalLoadingWallpaperImage.NotSet()
        } catch (e: Exception) {
            print("Exception: ${e.message}")
            e.message?.let { LocalLoadingWallpaperImage.Error(it) } ?: LocalLoadingWallpaperImage.Error()
        }
    }

}