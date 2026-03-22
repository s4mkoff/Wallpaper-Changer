package s4.tools.wallpaper_changer.presentation.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import s4.tools.wallpaper_changer.domain.models.LocalLoadingWallpaperImage
import s4.tools.wallpaper_changer.domain.models.actions.HomeUIAction
import wallpaper_changer.composeapp.generated.resources.Res
import wallpaper_changer.composeapp.generated.resources.api_settings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    currentApi: String,
    currentWallpaperImage: LocalLoadingWallpaperImage,
    action: (HomeUIAction) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .fillMaxWidth(),
                title = {
                    Text(
                        text = "Current api: $currentApi"
                    )
                },
                actions = {
                    Icon(
                        modifier = Modifier
                            .clickable(
                                onClick = {
                                    action(
                                        HomeUIAction.ToApiSettings()
                                    )
                                }
                            ),
                        painter = painterResource(Res.drawable.api_settings),
                        contentDescription = "Api settings icon"
                    )
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
        ) {
            item {
                when (currentWallpaperImage) {
                    is LocalLoadingWallpaperImage.Error, is LocalLoadingWallpaperImage.NotSet -> {
                        Text(
                            text = currentWallpaperImage.message
                        )
                    }
                    is LocalLoadingWallpaperImage.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(100.dp)
                        )
                    }
                    is LocalLoadingWallpaperImage.Success -> {
                        Image(
                            modifier = Modifier
                                .fillMaxWidth(),
                            bitmap = currentWallpaperImage.image,
                            contentDescription = "Current wallpaper image"
                        )
                    }
                }
            }
            item {
                Spacer(
                    modifier = Modifier
                        .size(50.dp)
                )
            }
            item {
                Button(
                    onClick = {
                        action(
                            HomeUIAction.RandomWallpaper()
                        )
                    }
                ) {
                    Text(
                        text = "Random wallpaper"
                    )
                }
            }
            item {
                Button(
                    onClick = {
                        action(
                            HomeUIAction.ChooseFromWallpaperList()
                        )
                    }
                ) {
                    Text(
                        text = "Random list"
                    )
                }
            }
        }
    }
}