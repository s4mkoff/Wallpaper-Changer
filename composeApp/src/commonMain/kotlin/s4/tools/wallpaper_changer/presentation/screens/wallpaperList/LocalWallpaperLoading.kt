package s4.tools.wallpaper_changer.presentation.screens.wallpaperList

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import s4.tools.wallpaper_changer.domain.models.LocalLoadingWallpaperImage
import s4.tools.wallpaper_changer.domain.models.actions.HistoryUIAction

@Composable
fun LocalWallpaperLoading(
    loadingWallpaper: LocalLoadingWallpaperImage,
    onHold: () -> Unit,
    onClick: () -> Unit
) {
    when (loadingWallpaper) {
        is LocalLoadingWallpaperImage.Error, is LocalLoadingWallpaperImage.NotSet -> {
            Text(
                text = loadingWallpaper.message
            )
        }

        is LocalLoadingWallpaperImage.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is LocalLoadingWallpaperImage.Success -> {
            Image(
                modifier = Modifier
                    .pointerInput(
                        true
                    ) {
                        detectTapGestures(
                            onTap = { onClick() },
                            onLongPress = { onHold() }
                        )
                    }
                    .fillMaxWidth(),
                bitmap = loadingWallpaper.image,
                contentDescription = "Current wallpaper image"
            )
        }

    }
}