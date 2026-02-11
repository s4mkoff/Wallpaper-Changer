package s4.tools.wallpaper_changer.data.api.wallhaven

import kotlinx.serialization.Serializable

@Serializable
data class WallhavenSettings(
    val categories: Categories = Categories(),
    val purity: Purity = Purity(),
    val resolution: String = "3440x1440",
    val sorting: Sorting = Sorting.Toplist,
    val ratios: Ratios = Ratios.Landscape,
    val token: String = ""
)