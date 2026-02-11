package s4.tools.wallpaper_changer

import java.io.File

class JVMPlatform : Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()

actual fun changeWallpaper(dir: String, wallpaperName: String) {
    val file = File(dir + wallpaperName)
    println("yeah eyah yeah: ${file.absolutePath}")
    Runtime.getRuntime().exec("plasma-apply-wallpaperimage ${file.absolutePath}")
}

actual fun getFilesDirectory(): String {
    val directory = System.getProperty("user.dir")
    println("directory: $directory")
    return "$directory/"
}