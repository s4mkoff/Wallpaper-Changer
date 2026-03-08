package s4.tools.wallpaper_changer.domain.local.os

import s4.tools.wallpaper_changer.domain.models.wallpaper.Wallpaper
import java.io.File

interface FilesManager {

    val directory: File

    fun lookForWallpapers(): List<Wallpaper>?

    fun removeWallpapers(wallpapers: List<Wallpaper>)

    fun createWallpaperFile(name: String, extension: String): File

}