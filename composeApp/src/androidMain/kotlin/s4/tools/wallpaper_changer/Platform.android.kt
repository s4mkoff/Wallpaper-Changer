package s4.tools.wallpaper_changer

import android.Manifest
import android.app.WallpaperManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresPermission
import java.io.File

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

object AppContext{
    lateinit var appContext: Context
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual fun changeWallpaper(dir: String, wallpaperName: String) {
    val file = File(dir + wallpaperName)
    val context = AppContext.appContext
    val wallpaperManager = WallpaperManager.getInstance(context)
    wallpaperManager.setStream(file.inputStream())
}

actual fun getFilesDirectory(): String {
    val directory = AppContext.appContext.filesDir.absolutePath
    println("directory: $directory")
    return "$directory/"
}