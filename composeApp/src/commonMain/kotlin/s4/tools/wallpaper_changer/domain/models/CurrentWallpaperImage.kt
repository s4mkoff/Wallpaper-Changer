package s4.tools.wallpaper_changer.domain.models

import androidx.compose.ui.graphics.ImageBitmap

sealed class CurrentWallpaperImage(
    val message: String = ""
) {

    class Success(val image: ImageBitmap): CurrentWallpaperImage()
    class Error(error: String = "Something went wrong, image cannot be loaded"): CurrentWallpaperImage(error)
    class NotSet: CurrentWallpaperImage("Wallpaper changer not set any wallpaper yet")
    class Loading: CurrentWallpaperImage()

}