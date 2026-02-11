package s4.tools.wallpaper_changer.data.api.wallhaven

enum class Sorting(
    val value: String
) {
    DateAdded("date_added"),
    Relevance("relevance"),
    Random("random"),
    Views("views"),
    Favorites("favorites"),
    Toplist("toplist"),

//    companion object {
//        val entries = listOf(DateAdded, Relevance, Random, Views, Favorites, Toplist)
//    }
}