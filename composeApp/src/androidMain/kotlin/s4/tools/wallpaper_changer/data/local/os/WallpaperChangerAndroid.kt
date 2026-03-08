package s4.tools.wallpaper_changer.data.local.os

import android.app.WallpaperManager
import s4.tools.wallpaper_changer.AppContext
import s4.tools.wallpaper_changer.domain.local.os.WallpaperChanger
import java.io.File

class WallpaperChangerAndroid: WallpaperChanger {

    override fun changeWallpaper(file: File) {
        val wallpaperManger = WallpaperManager.getInstance(AppContext.appContext)
        wallpaperManger.setStream(file.inputStream())
    }

}