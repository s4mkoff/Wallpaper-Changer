package s4.tools.wallpaper_changer.domain.models.actions

import s4.tools.wallpaper_changer.presentation.theme.Theme

sealed class SettingsUIAction {

    class ClearWallpapers: SettingsUIAction()
    class ChangeNightMode(val theme: Theme): SettingsUIAction()
    class ToggleSingleWallpaper: SettingsUIAction()

}