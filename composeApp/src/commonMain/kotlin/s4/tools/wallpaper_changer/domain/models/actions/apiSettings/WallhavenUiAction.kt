package s4.tools.wallpaper_changer.domain.models.actions.apiSettings

import s4.tools.wallpaper_changer.domain.models.wallpaper.wallhaven.WallhavenSettings

sealed class WallhavenUiAction {

    class ChangeSetting(val newSettings: WallhavenSettings): WallhavenUiAction()
    class SaveSettings: WallhavenUiAction()
    class TokenError: WallhavenUiAction()
    class ChangeWallpaper: WallhavenUiAction()
    class Back: WallhavenUiAction()

}