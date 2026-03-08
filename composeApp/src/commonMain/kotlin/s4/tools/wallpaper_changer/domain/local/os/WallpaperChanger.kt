package s4.tools.wallpaper_changer.domain.local.os

import java.io.File

interface WallpaperChanger {

    fun changeWallpaper(file: File)

}