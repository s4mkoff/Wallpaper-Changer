package s4.tools.wallpaper_changer.domain.models.wallpaper.wallhaven.remoteModels

enum class Sorting(
    val value: String
) {
    DateAdded("date_added"),
    Relevance("relevance"),
    Random("random"),
    Views("views"),
    Favorites("favorites"),
    Toplist("toplist"),

}