package s4.tools.wallpaper_changer.presentation.screens.wallpaperList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LoadingIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import coil3.compose.AsyncImage
import coil3.compose.SubcomposeAsyncImage
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
                var minWidth by remember { mutableIntStateOf(200) }
                var minHeight by remember { mutableIntStateOf(200) }
                SubcomposeAsyncImage(
                    modifier = Modifier
                        .sizeIn(
                            minHeight = minHeight.dp,
                            minWidth = minWidth.dp
                        )
                        .clickable(
                            onClick = {
                                onWallpaperClick(it)
                            }
                        ),
                    onSuccess = {
                        minHeight = 0
                        minWidth = 0
                    },
                    loading = {
                        CircularProgressIndicator()
                    },
                    error = {
                        Text(
                            text = "Error loading image"
                        )
                    },
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