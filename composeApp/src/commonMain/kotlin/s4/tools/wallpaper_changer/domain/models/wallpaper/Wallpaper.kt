package s4.tools.wallpaper_changer.domain.models.wallpaper

import java.io.File

class Wallpaper(
    val file: File
) {

    fun trimNameToIndex(): Long {
        val name = file.nameWithoutExtension
        return name.toLong()
    }

}