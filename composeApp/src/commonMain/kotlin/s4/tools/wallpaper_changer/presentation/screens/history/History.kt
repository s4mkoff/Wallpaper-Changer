package s4.tools.wallpaper_changer.presentation.screens.history

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import s4.tools.wallpaper_changer.domain.models.LocalLoadingWallpaperImage
import s4.tools.wallpaper_changer.domain.models.actions.HistoryUIAction
import s4.tools.wallpaper_changer.domain.models.storage.WallpaperHistoryEntry
import s4.tools.wallpaper_changer.domain.models.wallpaper.Wallpaper
import s4.tools.wallpaper_changer.presentation.screens.wallpaperList.LocalWallpaperLoading
import s4.tools.wallpaper_changer.toBitmap

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun History(
    wallpapersFromHistory: List<Pair<WallpaperHistoryEntry, Wallpaper?>>,
    action: (HistoryUIAction) -> Unit
) {
    var wallpaperHistoryEntryHolded: WallpaperHistoryEntry? by remember {
        mutableStateOf(null)
    }
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
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                LazyVerticalStaggeredGrid(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalItemSpacing = 8.dp,
                    columns = StaggeredGridCells.Fixed(2)
                ) {
                    items(wallpapersFromHistory) { (wallpaperEntry: WallpaperHistoryEntry, wallpaperFile: Wallpaper?) ->
                        val isLocal = wallpaperFile!=null
                        var loadingWallpaper by remember(wallpaperFile?.file) { mutableStateOf<LocalLoadingWallpaperImage>(LocalLoadingWallpaperImage.Loading()) }
                        if (isLocal) {
                            LaunchedEffect(wallpaperFile.file) {
                                loadingWallpaper = try {
                                    wallpaperFile.file.toBitmap()?.let { LocalLoadingWallpaperImage.Success(it) }
                                        ?: LocalLoadingWallpaperImage.Error()
                                } catch (e: Exception) {
                                    print("Exception: ${e.message}")
                                    LocalLoadingWallpaperImage.Error()
                                }
                            }
                        }
                        if (isLocal) {
                            LocalWallpaperLoading(
                                loadingWallpaper,
                                onClick = {
                                    action(HistoryUIAction.ChangeSelectedWallpaper(wallpaperFile))
                                },
                                onHold = {
                                    wallpaperHistoryEntryHolded = wallpaperEntry
                                }
                            )
                        } else {
                            AsyncImage(
                                modifier = Modifier
                                    .clickable(
                                        onClick = {
                                            wallpaperHistoryEntryHolded = wallpaperEntry
                                        }
                                    )
                                    .pointerInput(
                                        true
                                    ) {
                                        detectTapGestures(
                                            onLongPress = {
                                                action(HistoryUIAction.RemoveSelectedWallpaper(wallpaperEntry))
                                            }
                                        )
                                    },
                                model = wallpaperEntry.thumbUrl,
                                contentDescription = "WallpaperDescription"
                            )
                        }
                    }
                }
                AnimatedVisibility(
                    visible = wallpaperHistoryEntryHolded!=null
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0x80000000)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = "Choose action"
                            )
                            Spacer(
                                modifier = Modifier
                                    .width(300.dp)
                            )
                            Button(
                                onClick = {
                                    wallpaperHistoryEntryHolded?.let { action(HistoryUIAction.RemoveSelectedWallpaper(it)) }
                                    wallpaperHistoryEntryHolded = null
                                }
                            ) {
                                Text(
                                    text = "Remove from history"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}