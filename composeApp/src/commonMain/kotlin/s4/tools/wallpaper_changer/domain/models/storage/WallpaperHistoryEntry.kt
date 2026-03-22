package s4.tools.wallpaper_changer.domain.models.storage

import kotlinx.serialization.Serializable

@Serializable
data class WallpaperHistoryList(
    val list: List<WallpaperHistoryEntry>
)

@Serializable
data class WallpaperHistoryEntry(
    val id: String,
    val apiName: String,
    val thumbUrl: String,
    val pathUrl: String,
)