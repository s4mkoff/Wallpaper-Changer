package s4.tools.wallpaper_changer

import java.io.File

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun changeWallpaper(dir: String, wallpaperName: String)

expect fun getFilesDirectory(): String

expect fun scheduleWorkManager()

expect fun cancelWorkManager()