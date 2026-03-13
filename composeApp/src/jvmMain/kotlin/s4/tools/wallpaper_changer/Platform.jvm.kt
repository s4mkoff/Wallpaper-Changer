package s4.tools.wallpaper_changer

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.lifecycle.LifecycleOwner
import org.jetbrains.skia.Image
import s4.tools.wallpaper_changer.domain.local.os.FilesManager
import s4.tools.wallpaper_changer.data.local.os.FilesManagerDesktop
import s4.tools.wallpaper_changer.data.local.os.WallpaperChangerImpl
import s4.tools.wallpaper_changer.domain.local.os.WallpaperChanger
import s4.tools.wallpaper_changer.domain.local.storage.StorageManager
import s4.tools.wallpaper_changer.domain.remote.WallpaperNetwork
import java.io.File

class JVMPlatform : Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()

actual fun cancelWorkManager() = Unit
actual fun scheduleWorkManager() = Unit
actual fun observeWorkManagerState(
    lifecycleOwner: LifecycleOwner,
    observerStatus: (String) -> Unit
) = Unit

actual fun exportFilesInExternal(callback: (String) -> Unit) = Unit

actual fun getFilesManager(): FilesManager {
    return FilesManagerDesktop()
}

actual fun getStorageManager(): StorageManager {
    TODO("Not yet implemented")
}

actual fun File.toBitmap(): ImageBitmap? {
    val bytes = this.readBytes()
    return Image.makeFromEncoded(bytes).toComposeImageBitmap()
}

@Composable
actual fun BackHandling(callback: (finish: () -> Unit) -> Unit) {

}

actual fun getWallpaperChanger(): WallpaperChanger {
    return WallpaperChangerImpl()
}

actual fun getWallpaperNetwork(): WallpaperNetwork {
    TODO("Not yet implemented")
}