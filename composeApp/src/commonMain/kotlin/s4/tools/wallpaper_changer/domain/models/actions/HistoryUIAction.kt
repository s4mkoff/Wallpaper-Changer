package s4.tools.wallpaper_changer.domain.models.actions

import s4.tools.wallpaper_changer.domain.models.storage.WallpaperHistoryEntry
import s4.tools.wallpaper_changer.domain.models.wallpaper.Wallpaper

sealed class HistoryUIAction {

    class ChangeSelectedWallpaper(val wallpaper: Wallpaper): HistoryUIAction()
    class DownloadAndChangeSelectedWallpaper(val wallpaperHistoryEntry: WallpaperHistoryEntry): HistoryUIAction()
    class RemoveSelectedWallpaper(val wallpaperHistoryEntry: WallpaperHistoryEntry): HistoryUIAction()

}