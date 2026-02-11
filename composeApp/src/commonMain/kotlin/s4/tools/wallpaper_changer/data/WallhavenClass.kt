package s4.tools.wallpaper_changer.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class WallhavenResponse(
    val data: List<WallpaperData>
)

@Serializable
data class WallpaperData(
    val id: String,
    val url: String,
    val path: String, // Пряме посилання на зображення
    @SerialName("file_type") val fileType: String,
    val resolution: String
)