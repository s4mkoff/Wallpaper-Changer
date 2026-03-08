package s4.tools.wallpaper_changer

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.LifecycleOwner
import s4.tools.wallpaper_changer.domain.local.os.FilesManager
import s4.tools.wallpaper_changer.domain.local.os.WallpaperChanger
import s4.tools.wallpaper_changer.domain.local.storage.StorageManager
import s4.tools.wallpaper_changer.domain.remote.WallpaperNetwork
import java.io.File

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun scheduleWorkManager()

expect fun cancelWorkManager()

expect fun getFilesManager(): FilesManager

expect fun getStorageManager(): StorageManager

expect fun getWallpaperChanger(): WallpaperChanger

expect fun getWallpaperNetwork(): WallpaperNetwork

//expect fun getResourceDelegation(): ResourceDelegation

expect fun File.toBitmap(): ImageBitmap?

expect fun observeWorkManagerState(
    lifecycleOwner: LifecycleOwner,
    observerStatus: (String) -> Unit
)

expect fun exportFilesInExternal(
    callback: (String) -> Unit
)