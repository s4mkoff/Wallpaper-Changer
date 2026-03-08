package s4.tools.wallpaper_changer.data.local.os

import s4.tools.wallpaper_changer.AppContext
import s4.tools.wallpaper_changer.domain.local.os.FilesManager
import s4.tools.wallpaper_changer.domain.models.wallpaper.Wallpaper
import java.io.File

class FilesManagerAndroid: FilesManager {


    override val directory: File = AppContext.appContext.filesDir

    init {
        if (!directory.exists()) directory.mkdir()
    }

    override fun lookForWallpapers(): List<Wallpaper>? {
        val wallpaperDirectory = File(directory, WALLPAPER_FOLDER_NAME)
        if (!wallpaperDirectory.exists()) return null
        return wallpaperDirectory.listFiles().map { Wallpaper(it) }
    }

    override fun removeWallpapers(wallpapers: List<Wallpaper>) {
        wallpapers.forEach {
            if (it.file.exists()) it.file.delete()
        }
    }

    override fun createWallpaperFile(name: String, extension: String): File {
        val wallpaperDirectory = File(directory, WALLPAPER_FOLDER_NAME)
        if (!wallpaperDirectory.exists()) wallpaperDirectory.mkdir()
        return File(wallpaperDirectory, "$name.$extension")
    }

    companion object {
        const val WALLPAPER_FOLDER_NAME = "wallpapers"
    }

}