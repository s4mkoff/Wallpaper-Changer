package s4.tools.wallpaper_changer.presentation.screens.history

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import s4.tools.wallpaper_changer.domain.models.CurrentWallpaperImage
import s4.tools.wallpaper_changer.domain.models.actions.HistoryUIAction
import s4.tools.wallpaper_changer.domain.models.wallpaper.Wallpaper
import s4.tools.wallpaper_changer.toBitmap

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun History(
    wallpapersFromHistory: List<Wallpaper>,
    action: (HistoryUIAction) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "History"
                    )
                }
            )
        }
    ) { padding ->
        if (wallpapersFromHistory.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "History is empty"
                )
            }
        } else {
            LazyVerticalStaggeredGrid(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalItemSpacing = 8.dp,
                columns = StaggeredGridCells.Fixed(2)
            ) {
                items(wallpapersFromHistory) { wallpaperFile ->
                    var loadingWallpaper by remember(wallpaperFile.file) { mutableStateOf<CurrentWallpaperImage>(CurrentWallpaperImage.Loading()) }
                    LaunchedEffect(wallpaperFile.file) {
                        loadingWallpaper = try {
                            wallpaperFile.file.toBitmap()?.let { CurrentWallpaperImage.Success(it) }
                                ?: CurrentWallpaperImage.Error()
                        } catch (e: Exception) {
                            print("Exception: ${e.message}")
                            CurrentWallpaperImage.Error()
                        }
                    }
                    when (loadingWallpaper) {
                        is CurrentWallpaperImage.Error, is CurrentWallpaperImage.NotSet -> {
                            Text(
                                text = loadingWallpaper.message
                            )
                        }

                        is CurrentWallpaperImage.Loading -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(150.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }

                        is CurrentWallpaperImage.Success -> {
                            (loadingWallpaper as? CurrentWallpaperImage.Success)?.let {
                                Image(
                                    modifier = Modifier
                                        .clickable(
                                            onClick = {
                                                action(HistoryUIAction.ChangeSelectedWallpaper(wallpaperFile))
                                            }
                                        )
                                        .fillMaxWidth(),
                                    bitmap = it.image,
                                    contentDescription = "Current wallpaper image"
                                )
                            }
                        }

                    }
                }
            }
        }
    }
}