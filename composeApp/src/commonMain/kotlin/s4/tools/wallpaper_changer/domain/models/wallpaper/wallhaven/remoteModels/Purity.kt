package s4.tools.wallpaper_changer.domain.models.wallpaper.wallhaven.remoteModels

import kotlinx.serialization.Serializable
import s4.tools.wallpaper_changer.toInt

@Serializable
data class Purity(
    var sfw: Boolean = true,
    var sketchy: Boolean = true,
    var nsfw: Boolean = false
) {
    fun toValue(): String {
        return "${sfw.toInt()}${sketchy.toInt()}${nsfw.toInt()}"
    }
}