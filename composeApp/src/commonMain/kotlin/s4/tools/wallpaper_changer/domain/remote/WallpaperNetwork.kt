package s4.tools.wallpaper_changer.domain.remote

import java.io.File

interface WallpaperNetwork {

    suspend fun downloadWallpaper(url: String, file: File)

}