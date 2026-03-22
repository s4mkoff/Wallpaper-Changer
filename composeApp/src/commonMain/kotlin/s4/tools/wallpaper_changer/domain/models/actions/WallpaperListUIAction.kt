package s4.tools.wallpaper_changer.domain.models.actions

import s4.tools.wallpaper_changer.domain.remote.WallpaperResponse

sealed class WallpaperListUIAction {

    class ChangeWallpaperFromList(val wallpaper: WallpaperResponse): WallpaperListUIAction()
    class LoadMoreWallpapers(val currentPage: Int): WallpaperListUIAction()

}