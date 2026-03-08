package s4.tools.wallpaper_changer.data.local.os

import s4.tools.wallpaper_changer.domain.local.os.WallpaperChanger
import java.io.File

class WallpaperChangerImpl: WallpaperChanger {

    override fun changeWallpaper(file: File) {
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

}