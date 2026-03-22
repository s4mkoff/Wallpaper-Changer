package s4.tools.wallpaper_changer.domain.remote

import kotlinx.serialization.Serializable

@Serializable
data class WallpaperResponse(
    val id: String,
    val thumbUrl: String,
    val extension: String,
    val path: String,
    val apiName: String
)