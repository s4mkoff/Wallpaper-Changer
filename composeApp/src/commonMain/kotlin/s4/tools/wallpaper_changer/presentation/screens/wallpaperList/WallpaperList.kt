package s4.tools.wallpaper_changer.presentation.screens.wallpaperList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import s4.tools.wallpaper_changer.domain.remote.WallpaperResponse

@Composable
fun WallpaperList(
    wallpapers: List<WallpaperResponse>?,
    onWallpaperClick: (WallpaperResponse) -> Unit
) {
    wallpapers?.let { nonNullList ->
        LazyVerticalStaggeredGrid(
            modifier = Modifier
                .fillMaxSize(),
            columns = StaggeredGridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalItemSpacing = 8.dp
        ) {
            items(nonNullList) {
                AsyncImage(
                    modifier = Modifier
                        .clickable(
                            onClick = {
                                onWallpaperClick(it)
                            }
                        ),
                    model = it.path,
                    contentDescription = null
                )
            }
        }
    } ?: Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Api call processing"
        )
    }
}