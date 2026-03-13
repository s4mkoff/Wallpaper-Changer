package s4.tools.wallpaper_changer.domain.models.wallpaper

import java.io.File
import kotlin.String
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.component3

class Wallpaper(
    val file: File
) {


    private val parts = file.nameWithoutExtension.split("_")

    val apiName = parts.getOrNull(0)
    val id = parts.getOrNull(1)
    val time = parts.getOrNull(2)?.toLongOrNull()

}