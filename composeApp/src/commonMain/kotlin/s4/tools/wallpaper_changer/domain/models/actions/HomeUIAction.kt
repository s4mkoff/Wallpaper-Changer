package s4.tools.wallpaper_changer.domain.models.actions

sealed class HomeUIAction {


    class ChooseFromWallpaperList: HomeUIAction()
    class RandomWallpaper: HomeUIAction()
    class ToApiSettings: HomeUIAction()

}