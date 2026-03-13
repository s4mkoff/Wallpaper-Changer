package s4.tools.wallpaper_changer.navigation

sealed class Screens(
    val route: String
) {

    object Home: Screens("Home")
    object ApiSettings: Screens("Api Settings")
    object WallpaperList: Screens("Wallpaper List")
    object Settings: Screens("Settings")
    object History: Screens("History")

}