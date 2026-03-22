package s4.tools.wallpaper_changer.domain.models

import androidx.compose.ui.graphics.ImageBitmap

sealed class LocalLoadingWallpaperImage(
    val message: String = ""
) {

    class Success(val image: ImageBitmap): LocalLoadingWallpaperImage()
    class Error(error: String = "Something went wrong, image cannot be loaded"): LocalLoadingWallpaperImage(error)
    class NotSet: LocalLoadingWallpaperImage("Wallpaper changer not set any wallpaper yet")
    class Loading: LocalLoadingWallpaperImage()

}