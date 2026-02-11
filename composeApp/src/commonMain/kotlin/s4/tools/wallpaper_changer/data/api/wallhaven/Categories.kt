package s4.tools.wallpaper_changer.data.api.wallhaven

import kotlinx.serialization.Serializable
import s4.tools.wallpaper_changer.toInt

@Serializable
data class Categories(
    var general: Boolean = true,
    var anime: Boolean = true,
    var people: Boolean = true
) {
    fun toValue(): String {
        return "${general.toInt()}${anime.toInt()}${people.toInt()}"
    }
}