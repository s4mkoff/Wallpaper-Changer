package s4.tools.wallpaper_changer

import androidx.lifecycle.LifecycleOwner
import java.io.File

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun changeWallpaper(dir: String, wallpaperName: String)

expect fun getFilesDirectory(): String

expect fun scheduleWorkManager()

expect fun cancelWorkManager()

expect fun observeWorkManagerState(
    lifecycleOwner: LifecycleOwner,
    observerStatus: (String) -> Unit
)

expect fun exportFilesInExternal(
    callback: (String) -> Unit
)