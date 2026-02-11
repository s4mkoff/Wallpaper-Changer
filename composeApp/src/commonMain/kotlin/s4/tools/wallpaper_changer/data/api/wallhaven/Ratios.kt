package s4.tools.wallpaper_changer.data.api.wallhaven

import s4.tools.wallpaper_changer.data.api.wallhaven.Sorting.DateAdded
import s4.tools.wallpaper_changer.data.api.wallhaven.Sorting.Favorites
import s4.tools.wallpaper_changer.data.api.wallhaven.Sorting.Random
import s4.tools.wallpaper_changer.data.api.wallhaven.Sorting.Relevance
import s4.tools.wallpaper_changer.data.api.wallhaven.Sorting.Toplist
import s4.tools.wallpaper_changer.data.api.wallhaven.Sorting.Views

enum class Ratios(
    val value: String
) {
    Portrait("portrait"),
    Landscape("landscape")

//    companion object {
//        val entries = listOf(Portrait, Landscape)
//    }

}