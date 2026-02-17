package s4.tools.wallpaper_changer

import androidx.lifecycle.LifecycleOwner
import java.io.File

class JVMPlatform : Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()

actual fun changeWallpaper(dir: String, wallpaperName: String) {
    val file = File(dir + wallpaperName)
    println("yeah eyah yeah: ${file.absolutePath}")
    val process = ProcessBuilder(
        "hyprctl",
        "hyprpaper",
        "wallpaper",
        ",${file.absolutePath},"
    )
        .redirectErrorStream(true)
        .start()
    println(process.inputStream.bufferedReader().readText())
    process.waitFor()
}

actual fun getFilesDirectory(): String {
    val userDirectory = System.getenv("HOME")
    val directory = File(userDirectory, ".wallpaperChanger")
    if (!directory.exists()) directory.mkdir()
    println("directory: $directory")
    return "$directory/"
}

actual fun cancelWorkManager() = Unit
actual fun scheduleWorkManager() = Unit
actual fun observeWorkManagerState(
    lifecycleOwner: LifecycleOwner,
    observerStatus: (String) -> Unit
) = Unit

actual fun exportFilesInExternal(callback: (String) -> Unit) = Unit