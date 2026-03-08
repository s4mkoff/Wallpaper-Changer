package s4.tools.wallpaper_changer.domain.models.wallpaper.wallhaven

import kotlinx.serialization.Serializable
import s4.tools.wallpaper_changer.domain.models.wallpaper.wallhaven.remoteModels.Categories
import s4.tools.wallpaper_changer.domain.models.wallpaper.wallhaven.remoteModels.Purity
import s4.tools.wallpaper_changer.domain.models.wallpaper.wallhaven.remoteModels.Ratios
import s4.tools.wallpaper_changer.domain.models.wallpaper.wallhaven.remoteModels.Sorting

@Serializable
data class WallhavenSettings(
    val categories: Categories = Categories(),
    val purity: Purity = Purity(),
    val resolution: String = "3440x1440",
    val sorting: Sorting = Sorting.Toplist,
    val ratios: Ratios = Ratios.Landscape,
    val token: String = "",
    val color: String = ""
)