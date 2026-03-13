package s4.tools.wallpaper_changer.domain.models

import kotlinx.serialization.Serializable
import s4.tools.wallpaper_changer.domain.models.wallpaper.Wallpaper
import s4.tools.wallpaper_changer.domain.models.wallpaper.WallpaperDetails
import s4.tools.wallpaper_changer.domain.models.wallpaper.wallhaven.WallpaperData
import s4.tools.wallpaper_changer.domain.remote.WallpaperResponse
import s4.tools.wallpaper_changer.presentation.theme.Theme

@Serializable
data class AppSettings(
    var theme: Theme = Theme.SYSTEM,
    var singleWallpaper: Boolean = false,
    var currentWallpaper: WallpaperResponse? = null
)