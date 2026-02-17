package s4.tools.wallpaper_changer.data.api.wallhaven

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import s4.tools.wallpaper_changer.data.LinkBuilder
import s4.tools.wallpaper_changer.data.api.GeneralApi

class WallhavenApi: GeneralApi {

    var categories by mutableStateOf(Categories())
    var purity by mutableStateOf(Purity())
    var resolution by mutableStateOf("3440x1440")
    var sorting: Sorting by mutableStateOf(Sorting.Toplist)
    var ratios: Ratios by mutableStateOf(Ratios.Landscape)

    var token by mutableStateOf("")

    var color by mutableStateOf("")
    

    override fun buildLink(): String {
        return LinkBuilder("wallhaven.cc/api/v1/search").apply {
            append("categories", categories.toValue())
            append("purity", purity.toValue())
            append("atleast", resolution)
            append("sorting", sorting.value)
            append("ratios", ratios.value)
//            append("colors", "660000")
            if (color.isNotEmpty()) append("color", color)
            if (token.isNotEmpty()) append("apikey", token)
        }.build()
    }

    fun toSettings(): WallhavenSettings {
        return WallhavenSettings(
            categories = categories,
            purity = purity,
            resolution = resolution,
            sorting = sorting,
            ratios = ratios,
            token = token
        )
    }

    fun applySettings(settings: WallhavenSettings) {
        categories = settings.categories
        purity = settings.purity
        resolution = settings.resolution
        sorting = settings.sorting
        ratios = settings.ratios
        token = settings.token
    }


}